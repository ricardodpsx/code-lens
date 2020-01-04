package co.elpache.codelens.extensions.js

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.firstLine
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import kotlin.math.max


fun applyJsMetrics(code: ContextNode) {

  code.find("file[lang='js']").forEach { fileNode ->

    with(fileNode) {
      find("*").forEach {
        it.find("$>Identifier").firstOrNull()?.let { id ->
          it.data["name"] = id.data.getString("name")
        }

        setSimplerType(it)
        it.data["firstLine"] = it.code.firstLine()
      }

      find("fun").forEach {
        it.data["textLines"] = it.code.split("\n").size
        it.data["lines"] = it.code.relevantCodeLines() - 1
        it.data["depth"] = depth(it.tree, it.vid) - 1
        it.data[":params"] = "$>params>param"
      }

      setQuery("SET {call} args = {$>args>arg | count()}")
      setQuery("SET {fun} params = {$ :params | count()}")
      setQuery("SET {class} properties = {$>binding | count()}")
      setQuery("SET {class} constructors = {$ fun[kind='constructor'] | count()}")
      setQuery("SET {class} methods = {$ fun[kind='method' || kind='get' || kind='constructor'] | count()}")

      find("class").forEach {
        it.data["lines"] = it.code.relevantCodeLines()
      }
    }

    fileNode.data["lines"] = fileNode.code.relevantCodeLines()
    fileNode.data["textLines"] = fileNode.code.split("\n").size
    fileNode.data["functions"] = fileNode.find("$>body>fun").size
    fileNode.data["classes"] = fileNode.find("$>body>class").size
    fileNode.data["bindings"] = fileNode.find("$>body>binding").size
  }
}

//Todo: loops, args?
fun setSimplerType(node: ContextNode) {
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

  simplerTypes.forEach {
    if (node.data.isA(it.key)) node.data["type"] = it.value
  }

  if (node.parent != null && node.parent!!.isA("params"))
    node.data["type"] = "param"

  if (node.parent != null && node.parent!!.isA("args"))
    node.data["type"] = "arg"

}

private fun depth(tree: CodeTree, vid: Vid): Int {
  var maxDepth = 0
  for (cVid in tree.children(vid))
    maxDepth = max(depth(tree, cVid), maxDepth)

  return (if (increasesNesting(tree.v(vid))) 1 else 0) + maxDepth
}

fun increasesNesting(item: VData) = listOf("fun", "if", "loop").any { item.isA(it) }

fun String.relevantCodeLines() =
  split("\n")
    .map { it.trim() }
    .filter { !it.isBlank() }
    .filter { it.length > 1 }
    .size
