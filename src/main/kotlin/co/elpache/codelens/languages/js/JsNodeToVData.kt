package co.elpache.codelens.languages.js

import co.elpache.codelens.firstLine
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf


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
  val name = c.getString("name")
  val id = c.child("id")

  if (id != null) return id.getString("name")

  if (type == "ClassMethod") return c.child("key")?.get("name") as? String

  return name
}

internal fun toJsCodeEntity(c: JsNode, codeFile: JsFileLoader, parent: VData?): VData {
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