package co.elpache.codelens.extensions.js

import co.elpache.codelens.codeLoader.FileLoader
import co.elpache.codelens.codeLoader.LanguageIntegration
import co.elpache.codelens.codeLoader.ignorePatterns
import co.elpache.codelens.codeLoader.languageSupportRegistry
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.streams.toList

fun jsInit() {
  languageSupportRegistry[".*\\.js"] = jsLanguageIntegration
}

val jsLanguageIntegration = LanguageIntegration(
  fileLoaderBuilder = ::JsFileLoader,
  applyMetrics = ::applyJsMetrics,
  onBaseCodeLoad = ::preloadParsedFiles
)

val parsedCache = ConcurrentHashMap<String, JsonNode>()

class JsFileLoader(file: File, basePath: File) : FileLoader<JsonNode>(file, "js", basePath) {

  override fun getValues(node: JsonNode): Map<String, String> =
    if (node.isArray)
      node.asSequence().withIndex()
        .filter { it.value.isValueNode }
        .associateBy({ it.index.toString() }, { it.value.asText() })
    else
      node.fields().asSequence()
        .filter { it.value.isValueNode }
        .associateBy({ it.key }, { it.value.asText("") })

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

