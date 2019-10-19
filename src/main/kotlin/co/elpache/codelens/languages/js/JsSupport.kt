package co.elpache.codelens.languages.js

import co.elpache.codelens.codetree.CodeEntity
import co.elpache.codelens.codetree.CodeFile
import co.elpache.codelens.codetree.LanguageCodeEntity
import co.elpache.codelens.codetree.NodeData
import co.elpache.codelens.codetree.addAll
import co.elpache.codelens.codetree.buildAstFile
import co.elpache.codelens.codetree.nodeDataOf
import co.elpache.codelens.firstLine
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File


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
    }.filterNot {
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


class JsFile(val file: File) : CodeFile(file) {
  override val lang = "js"
  override fun expand(): List<CodeEntity> {
    val code = contents()

    try {
      return toJson(parseFile(file))
        .child("program")!!
        .getList("body")
        .map {
          toJsCodeEntity(it.asNode()!!, code, this)
        }
    } catch (e: Exception) {

      super.error = e.message ?: e.stackTrace.contentToString()

      return listOf(
        toJsCodeEntity(
          mapOf(
            "type" to "error",
            "start" to 0,
            "end" to error.length
          ), code, this
        ),
        toJsCodeEntity(
          mapOf(
            "type" to "code",
            "start" to error.length,
            "end" to error.length + file.length()
          ), code, this
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
  code: String,
  val node: JsNode,
  val extra: NodeData
) : LanguageCodeEntity(
  name = name, type = type, astType = astType, startOffset = startOffset, endOffset = endOffset, code = code
) {


  override fun expand() =
    node.children()
      .map {
        toJsCodeEntity(it, code = code, parent = this)
      }

  init {
    data.addAll(

    )
    node.jsonValues().forEach {
      data.addAll(it.key to it.value)
    }
  }

}

fun toJson(js: String): Map<String, Any> {
  return ObjectMapper().readValue(js, Map::class.java) as Map<String, Any>
}


fun parseFile(file: File): String {
  val e = Runtime.getRuntime().exec(
    "node JavaScriptSupport.js ${file.absolutePath}", null,
    File("frontend")
  );

  val programOutput = e.inputStream.bufferedReader().readText()
  val programError = e.errorStream.bufferedReader().readText()

  //Todo: Process error output to ease troubleshooting
  if (programError.isNotEmpty())
    throw RuntimeException("Error Parsing File:\n $programError")

  return programOutput
}


//block, fun, class, if
fun simplifyType(astType: String, parent: CodeEntity) = when (astType) {
  "arguments" -> "args"
  "ClassDeclaration" -> "class"
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
  else -> if (astType == "Identifier" && parent.type == "params") "param"
  else if (astType == "Identifier" && parent.type == "args") "arg"
  else astType
}

fun getName(c: JsNode, type: String): String? {
  var name = c.getString("name")
  val id = c.child("id")

  if (id != null) return id.getString("name")

  if (type == "ClassMethod") return c.child("key")?.get("name") as? String

  return name
}

private fun toJsCodeEntity(c: JsNode, code: String, parent: CodeEntity): CodeEntity {

  val astType = c.getString("type")!!
  var type = simplifyType(astType, parent)


  return JsCodeEntity(
    name = getName(c, astType),
    astType = astType,
    type = type,
    node = c,
    code = code,
    startOffset = c.getInt("start")!!,
    endOffset = c.getInt("end")!!,
    extra = nodeDataOf(
      "keys" to c.keys,
      "firstLine" to code.firstLine()
    )
  )
}