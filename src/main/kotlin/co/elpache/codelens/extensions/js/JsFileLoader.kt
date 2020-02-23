package co.elpache.codelens.extensions.js

import co.elpache.codelens.codeLoader.FileLoader
import co.elpache.codelens.codeLoader.LanguageIntegration
import co.elpache.codelens.codeLoader.ignorePatterns
import co.elpache.codelens.firstLine
import co.elpache.codelens.tree.Vertice
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.streams.toList

val jsLanguageIntegration = LanguageIntegration(
  fileLoaderBuilder = ::JsFileLoader,
  applyMetrics = ::applyJsMetrics,
  onBaseCodeLoad = ::preloadParsedFiles,
  filePattern = ".*\\.js"
)

val parsedCache = ConcurrentHashMap<String, JsonNode>()

val simplerTypes = mapOf(
  "CommentLine" to "comment",
  "arguments" to "args",
  "ClassDeclaration" to "class",
  "ClassExpression" to "class",
  "ClassMethod" to "fun",
  "FunctionDeclaration" to "fun",
  "BlockStatement" to "block",
  "CallExpression" to "call",
  "ImportDeclaration" to "import",
  "BinaryExpression" to "expression",
  "ArrowFunctionExpression" to "fun",
  "IfStatement" to "if",
  "ObjectExpression" to "object",
  "NewExpression" to "call",
  "AssignmentExpression" to "binding",
  "VariableDeclaration" to "binding",
  "NumericLiteral" to "number",
  "StringLiteral" to "string",
  "quasis" to "string"
)

//Todo: Extract js especifics and make this a general JSON processor
class JsFileLoader(file: File, basePath: File) : FileLoader<JsonNode>(file, "js", basePath) {

  override fun getValues(node: JsonNode): Map<String, String> =
    if (node.isArray)
      node.asSequence().withIndex()
        .filter { it.value.isValueNode }
        .associateBy({ it.index.toString() }, { it.value.asText() })
    else {
      val fields = node.fields().asSequence()
        .filter { it.value.isValueNode }
        .associateBy({ it.key }, { it.value.asText("") })
      fields
    }


  override fun getChildren(node: JsonNode): Map<String, JsonNode> =
    if (node.isArray)
      node.asSequence().withIndex()
        .filterNot { it.value.isValueNode }
        .associateBy({ it.index.toString() }, { it.value })
    else
      node.fields().asSequence()
        .filterNot { it.value.isValueNode }
        .associateBy({ it.key.toString() }, { it.value })

  override fun parseFile(): JsonNode = parseFile(file).at("/program/body")

  override fun postProcessNode(node: Vertice) {
    val code = contents.substring(node.getInt("start"), node.getInt("end"))
    node.addType(simplerTypes.getOrDefault(node.rawType, ""))
    node["firstLine"] = code.firstLine()
  }

}


fun parseFile(file: File): JsonNode {
  return parsedCache.computeIfAbsent(file.path) {
    toJson(parseFiles(listOf(file)).first())
  }
}

/**
 * As Javascript parsing is calling a nodeJs process, parsing files in bundles is more efficient than parsing them one by one.
 * Because initializing nodejs everytime is expensive. If there is a limit on the amount of arguments tha can be passed
 * to the Javascrpt parser program we may need to split the files in batches.
 * Another more memory-efficient approach would be to make the JsParsing process interactive and use pipelines.
 */
fun preloadParsedFiles(path: File) {
  //parsedCache.clear()

  //Todo: Using a recursive file traverser that avoids visiting unnecesary folders may be more efficient
  val files = path.walkTopDown()
    .filterNot { f ->
      ignorePatterns.any { f.path.matches(it.toRegex()) }
    }
    .filter { it.extension == "js" }.toList()

  if (files.isNotEmpty()) {

    files.chunked(50) {
      val parsed = parseFiles(it)
      it.zip(parsed).forEach {
        parsedCache[it.first.path] = toJson(it.second)
      }
    }
  }
}

fun parseFiles(files: List<File>): List<String> {
  val fileArgs = files.map { it.absolutePath }.joinToString(" ")
  val command = "node JavaScriptSupport.js ${fileArgs}"
  val e = Runtime.getRuntime().exec(command, null, File("frontend"))

  val programOutput = e.inputStream.bufferedReader().lines().toList()
  val programError = e.errorStream.bufferedReader().readText()

  if (programError.isNotEmpty())
    System.err.println(programError)

  return programOutput
}

fun toJson(js: String): JsonNode {
  return ObjectMapper().readTree(js)
}

