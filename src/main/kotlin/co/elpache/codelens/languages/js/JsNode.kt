package co.elpache.codelens.languages.js

import co.elpache.codelens.codeLoader.codeNodeBase
import co.elpache.codelens.tree.VData
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode

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

fun getName(c: JsonNode): String? {
  val type = c.get("type").asText()
  val name = c.get("name")?.asText()
  val id = c.at("/id/name")

  if (!id.isMissingNode)
    return id.asText()

  if (type == "ClassMethod") return c.get("key.name").asText()

  return name
}

internal fun toJsNode(c: JsonNode, codeFile: JsFileLoader, parent: VData?): VData {
  val astType = c.get("type").asText()
  return codeNodeBase(
    name = getName(c),
    astType = astType,
    type = simplifyType(astType, parent),
    start = c.get("start").asInt(),
    end = c.get("end").asInt(),
    file = codeFile
  )
}

fun JsonNode.astChildren() =
  this.fields().asSequence()
    .map {
      if (!it.value.at("/0/type").isMissingNode) {
        it.value.asSequence().forEach { node -> (node as ObjectNode).put("type", it.key) }
        it.value.asSequence().toList()
      } else if (it.value.get("type") != null) listOf(it.value)
      else listOf()
    }.flatten().toList()