package co.elpache.codelens.languages.js

import co.elpache.codelens.codetree.FileLoader
import co.elpache.codelens.codetree.buildAstFile
import co.elpache.codelens.firstLine
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import kotlin.streams.toList


val buildJsFile: buildAstFile = { file: File, parent: VData? -> JsFileLoader(file, parent) }

val parsedCache = HashMap<String, JsNode>()

fun buildParseCache(path: File) {
  val files = path.walkTopDown().filter { it.extension == "js" }.toList()

  if (files.isNotEmpty()) {
    val parsed = parseFiles(files)

    files.zip(parsed).forEach {
      parsedCache[it.first!!.path] = toJson(it.second)
    }
  } else {
    println("Not parsing $files")
  }

}

fun traverseChilds(node: JsNode, fileLoader: FileLoader, parent: VData?, visitor: (ce: VData, parent: VData?) -> Unit) {
  val ce = toJsCodeEntity(node, fileLoader, parent)
  visitor(ce, parent)
  node.children().forEach {
    traverseChilds(it.asNode()!!, fileLoader, ce, visitor)
  }
}


class JsFileLoader(file: File, parent: VData?) : FileLoader(file = file, lang = "js") {
  override fun traverse(visitor: (ce: VData, parent: VData?) -> Unit, parent: VData?) {
    val data = vDataOf(
      "language" to "js",
      "startOffset" to startOffset,
      "fileName" to fileName,
      "name" to file.nameWithoutExtension,
      "endOffset" to endOffset,
      "type" to type,
      "lang" to lang,
      "code" to contents()
    )

    visitor(data, parent)

    parsedCache[file.path]!!
      .child("program")!!
      .getList("body")
      .map {
        traverseChilds(it.asNode()!!, this, data, visitor)
      }

  }
}


fun toJson(js: String): Map<String, Any> {
  return ObjectMapper().readValue(js, Map::class.java) as Map<String, Any>
}


fun parseFiles(files: List<File>): List<String> {
  val fileArgs = files.map { it.absolutePath }.joinToString(" ")
  val command = "node JavaScriptSupport.js ${fileArgs}"
  val e = Runtime.getRuntime().exec(command, null, File("frontend"))

  val programOutput = e.inputStream.bufferedReader().lines().toList()
  val programError = e.errorStream.bufferedReader().readText()

  //Todo: Process error output to ease troubleshooting
  if (programError.isNotEmpty()) {
    //throw RuntimeException("Error Parsing File: \n $programError")

  }

  return programOutput
}


//block, fun, class, if
fun simplifyType(astType: String, parent: VData?) = when (astType) {
  "CommentLine" -> "comment"
  "arguments" -> "args"
  "ClassDeclaration", "ClassExpression" -> "class"
  "ClassMethod" -> "fun"
  "FunctionDeclaration" -> "fun"
  "BlockStatement" -> "block"
  "CallExpression" -> "call"
  "ImportDeclaration" -> "import"
  "BinaryExpression" -> "expression"
  "ArrowFunctionExpression" -> "fun"
  "IfStatement" -> "if"
  "ObjectExpression" -> "object"
  "NewExpression" -> "call" //todo: Multitype's object contruction
  "AssignmentExpression" -> "binding"
  "VariableDeclaration" -> "binding"
  "NumericLiteral" -> "number"
  "StringLiteral", "quasis" -> "string"
  else -> if (parent?.type == "params") "param"
  else if (parent?.type == "args") "arg"
  else astType
}

fun getName(c: JsNode, type: String): String? {
  var name = c.getString("name")
  val id = c.child("id")

  if (id != null) return id.getString("name")

  if (type == "ClassMethod") return c.child("key")?.get("name") as? String

  return name
}

private fun toJsCodeEntity(c: JsNode, codeFile: FileLoader, parent: VData?): VData {
  val astType = c.getString("type")!!
  val type = simplifyType(astType, parent)
  val startOffset = c.getInt("start")!!
  val endOffset = c.getInt("end")!!
  val code = codeFile.contents().substring(startOffset, endOffset)
  return vDataOf(
    "name" to getName(c, astType),
    "astType" to astType,
    "type" to type,
    "startOffset" to startOffset,
    "endOffset" to endOffset,
    "firstLine" to code.firstLine(),
    "code" to code
  )
}