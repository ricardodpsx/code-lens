package co.elpache.codelens.languages.js

import co.elpache.codelens.codeLoader.codeNodeBase
import co.elpache.codelens.tree.VData
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
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


fun JsonNode.astChildren(): List<JsonNode> {

  if (this.get("isListWrapper") != null) return this.get("children").asSequence().toList()

  this.fields().asSequence()
    .filter { it.value.isListOfAstNodes() }
    .forEach {
      wrapListNode(it.key, it.value)
    }

  return this.fields()
    .asSequence()
    .filter { it.value.get("type") != null }
    .map { it.value }
    .toList()
}

private fun JsonNode.isListOfAstNodes() = !at("/0/type").isMissingNode

/**
 * This transforms a bit the JsonTree, making JsonNodes that look like {type:"function", params: [{type:"integer", ...}], ...}
 * to look like {type:"function", {type: "params", "children": [{ type: "paramsChild", "element": { type: "integer" } }] }
 * This makes the AST more manipulable, searchable and compatible with the expected AST for example:
 * fun > params > param
 */
private fun JsonNode.wrapListNode(key: String, value: JsonNode) {
  val listWrapperNode = JsonNodeFactory.instance.objectNode()
  val children = value.asSequence().toList()
  listWrapperNode.put("type", key)
  listWrapperNode.put("start", children.first().get("start").asInt())
  listWrapperNode.put("end", children.last().get("end").asInt())

  listWrapperNode.put("children", wrapListItem(key, value))
  listWrapperNode.put("isListWrapper", true)
  (this as ObjectNode).replace(key, listWrapperNode)
}

private fun wrapListItem(key: String, value: JsonNode): ArrayNode? {
  val children = value.asSequence().toList()
  val itemsList = JsonNodeFactory.instance.arrayNode()
  children.forEach { c ->
    val listItemWrapper = JsonNodeFactory.instance.objectNode()
    listItemWrapper.put("type", "${key}Child")
    if (c.get("name") != null) listItemWrapper.put("name", c.get("name").asText())
    listItemWrapper.put("start", c.get("start").asInt())
    listItemWrapper.put("end", c.get("end").asInt())
    listItemWrapper.put("element", c)
    itemsList.add(listItemWrapper)
  }
  return itemsList
}