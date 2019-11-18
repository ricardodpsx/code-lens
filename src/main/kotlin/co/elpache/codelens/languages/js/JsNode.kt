package co.elpache.codelens.languages.js

import co.elpache.codelens.codeLoader.codeNodeBase
import co.elpache.codelens.tree.VData
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
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
  "NewExpression" -> "call"
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

  if (type == "ClassMethod")
    return c.at("/key/name").asText()

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
  ).addAll(
    "keys" to c.fieldNames().asSequence().toList(),
    "kind" to c.get("kind")?.asText(),
    "async" to c.get("async")?.asText(),
    "async" to c.get("generator")?.asText()
  )
}

fun JsonNode.astChildren(): List<JsonNode> {

  if (this.get("astChildren") == null)
    this.fields().asSequence().forEach {
      if (!it.value.at("/0/type").isMissingNode) {
        val syntheticNode = JsonNodeFactory.instance.objectNode()
        val children = it.value.asSequence().toList()
        syntheticNode.put("type", it.key)
        syntheticNode.put("start", children.first().get("start").asInt())
        syntheticNode.put("end", children.last().get("end").asInt())

        val wrappedChildren = JsonNodeFactory.instance.arrayNode()
        children.forEach { c ->
          val wrap = JsonNodeFactory.instance.objectNode()
          wrap.put("type", "${it.key}Child")
          if (c.get("name") != null) wrap.put("name", c.get("name").asText())
          wrap.put("start", c.get("start").asInt())
          wrap.put("end", c.get("end").asInt())
          wrap.put("element", c)
          wrappedChildren.add(wrap)
        }
        syntheticNode.put("astChildren", wrappedChildren)


        (this as ObjectNode).replace(it.key, syntheticNode)
      }
    }

  if (this.get("astChildren") != null) return this.get("astChildren").asSequence().toList()

  return this.fields()
    .asSequence()
    .filter { it.value.get("type") != null }
    .map { it.value }
    .toList()
}