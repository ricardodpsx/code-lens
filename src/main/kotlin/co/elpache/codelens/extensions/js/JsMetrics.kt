package co.elpache.codelens.extensions.js

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.firstLine
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.Vid
import kotlin.math.max


fun applyJsMetrics(code: ContextNode) {

  code.find("file[lang='js']").forEach { fileNode ->

    with(fileNode) {
      find("*").forEach {
        it.find("$>Identifier").firstOrNull()?.let { id ->
          it["name"] = id.vertice.getString("name")
        }

        setSimplerType(it)
        it["firstLine"] = it.code.firstLine()
      }

      find("fun").forEach {
        it["textLines"] = it.code.split("\n").size
        it["lines"] = it.code.relevantCodeLines() - 1
        it["depth"] = depth(it.tree, it["vid"].toString()) - 1
        it[":params"] = "$>params>param"
      }

      find("call[{$>args>arg | count()} as args]")
      find("fun[{$ :params | count()} as params]")
      find("class[{$>binding | count()} as properties]")
      find("class[{$ fun[kind='constructor'] | count()} as constructors]")
      find("class[{$ fun[kind='method' || kind='get' || kind='constructor'] | count()} as methods]")

      find("class").forEach {
        it["lines"] = it.code.relevantCodeLines()
      }
    }

    fileNode["lines"] = fileNode.code.relevantCodeLines()
    fileNode["textLines"] = fileNode.code.split("\n").size
    fileNode["functions"] = fileNode.find("$>body>fun").size
    fileNode["classes"] = fileNode.find("$>body>class").size
    fileNode["bindings"] = fileNode.find("$>body>binding").size
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
    if (node.vertice.isA(it.key)) node["type"] = it.value
  }

  if (node.parent != null && node.parent!!.isA("params"))
    node["type"] = "param"

  if (node.parent != null && node.parent!!.isA("args"))
    node["type"] = "arg"

}

private fun depth(tree: CodeTree, vid: Vid): Int {
  var maxDepth = 0
  for (cVid in tree.children(vid))
    maxDepth = max(depth(tree, cVid), maxDepth)

  return (if (increasesNesting(tree.v(vid))) 1 else 0) + maxDepth
}

fun increasesNesting(item: Vertice) = listOf("fun", "if", "loop").any { item.isA(it) }

fun String.relevantCodeLines() =
  split("\n")
    .map { it.trim() }
    .filter { !it.isBlank() }
    .filter { it.length > 1 }
    .size
