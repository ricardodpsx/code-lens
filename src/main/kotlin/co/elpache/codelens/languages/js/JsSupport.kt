package co.elpache.codelens.languages.js

import co.elpache.codelens.codetree.CodeEntity
import co.elpache.codelens.codetree.CodeFile
import co.elpache.codelens.codetree.LangEntity
import co.elpache.codelens.codetree.addAll
import co.elpache.codelens.codetree.buildAstFile
import co.elpache.codelens.firstLine
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import kotlin.streams.toList


val buildJsFile: buildAstFile = { file: File -> JsFile(file) }

typealias JsNode = Map<String, Any?>

fun JsNode.child(key: String) = (this as Map<String, Any>)[key] as? JsNode

fun JsNode.getMap(key: String) = (this as Map<String, Any>)[key] as Map<String, Any>

fun JsNode.getList(key: String) = (this as Map<String, Any>)[key] as List<Any>

fun JsNode.getString(key: String): String? {
  val map = (this as? Map<String, Any>)

  if (map != null)
    return map.get(key) as? String

  return null
}

fun JsNode.getInt(key: String): Int? {
  val map = (this as? Map<String, Any>)

  if (map != null)
    return map.get(key) as? Int

  return null
}


fun JsNode.children() =
  this.entries
    .map { it.value?.asNode(it.key) }.filterNotNull()

fun JsNode.childrenMap(): Map<String, Any?> {
  val res = HashMap<String, Any?>()
  this.keys
    .forEach {
      res.put(it, this.entries.asNode(it))
    }
  return res
}

fun JsNode.jsonValues() =
  this.entries
    .filter { it.value?.asNode(it.key) == null }.map {
      it.key to it.value
    }
    .filterNot {
      listOf("type", "name").contains(it.first)
    }
    .toMap()

fun Any.asNode(key: String? = null): JsNode? {
  val map = this as? Map<String, Any>
  val li = this as? List<Map<String, Any>>

  return if (map != null && map.containsKey("type"))
    map
  else if (li != null && li.isNotEmpty() && li.first().containsKey("type"))
    mapOf(
      "type" to key!!,
      "start" to li.first()["start"],
      "end" to li.last()["end"]
    ).plus(li.mapIndexed { index, it -> index.toString() to it }).toMap()
  else null
}

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

class JsFile(file: File) : CodeFile(file = file, lang = "js") {
  override fun expand(): List<CodeEntity> {

    try {
      return parsedCache[file.path]!!
        .child("program")!!
        .getList("body")
        .map {
          toJsCodeEntity(it.asNode()!!, this, this)
        }
    } catch (e: Exception) {

      super.error = e.message ?: e.stackTrace.contentToString()

      return listOf(
        toJsCodeEntity(
          mapOf(
            "type" to "error",
            "start" to 0,
            "end" to error.length
          ), this, this
        ),
        toJsCodeEntity(
          mapOf(
            "type" to "code",
            "start" to error.length.toInt(),
            "end" to (error.length + file.length()).toInt()
          ), this, this
        )
      )
    }
  }
}


class JsCodeEntity(
  name: String? = null,
  type: String,
  astType: String,
  startOffset: Int,
  endOffset: Int,
  codeFile: CodeFile,
  val node: JsNode
) : LangEntity(
  name = name, type = type, astType = astType, startOffset = startOffset, endOffset = endOffset, codeFile = codeFile
) {

  override fun expand() =
    node.children()
      .map {
        toJsCodeEntity(it, codeFile = codeFile!!, parent = this)
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
fun simplifyType(astType: String, parent: CodeEntity) = when (astType) {
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
  else -> if (parent.type == "params") "param"
  else if (parent.type == "args") "arg"
  else astType
}

fun getName(c: JsNode, type: String): String? {
  var name = c.getString("name")
  val id = c.child("id")

  if (id != null) return id.getString("name")

  if (type == "ClassMethod") return c.child("key")?.get("name") as? String

  return name
}

private fun toJsCodeEntity(c: JsNode, codeFile: CodeFile, parent: CodeEntity): CodeEntity {

  val astType = c.getString("type")!!
  val type = simplifyType(astType, parent)

  val e = JsCodeEntity(
    name = getName(c, astType),
    astType = astType,
    type = type,
    node = c,
    codeFile = codeFile,
    startOffset = c.getInt("start")!!,
    endOffset = c.getInt("end")!!
  )

  e.data.addAll(
    "firstLine" to e.code.firstLine(),
    "code" to e.code
  )

  return e
}