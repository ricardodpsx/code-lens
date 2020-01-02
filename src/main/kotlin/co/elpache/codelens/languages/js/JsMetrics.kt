package co.elpache.codelens.languages.js

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.firstLine
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import kotlin.math.max

fun addType(astType: String, parent: VData?): String? {
  val type1 = when (astType) {
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
    else -> null
  }

  val type2 = if (parent != null && parent.isA("params")) "param"
  else if (parent != null && parent.isA("args")) "arg"
  else null

  return listOfNotNull(type1, type2).joinToString(" ")
}
fun applyJsMetrics(code: ContextNode) {

  code.find("file[lang='js']").forEach { fileNode ->

    with(fileNode) {

      find("*").forEach {
        val id = it.find("$>Identifier").firstOrNull()
        if (id != null)
          it.data["name"] = id.data.getString("name")

        addType(it.data.getString("type").split(" ").first(), it.parent)?.let { extraType ->
          it.data["type"] = extraType
        }

        it.data["firstLine"] = it.code.firstLine()
      }

      find("fun").forEach {
        it.find("$>Identifier")
        it.data["textLines"] = it.code.split("\n").size
        it.data["lines"] = it.code.relevantCodeLines() - 1
        it.data["depth"] = depth(it.tree, it.vid) - 1
        it.data[":params"] = "$>params>param"
      }


      setQuery("SET {call} args = {$>args>arg | count}")
      setQuery("SET {fun} params = {$ :params | count}")
      setQuery("SET {class} properties = {$>binding | count}")
      setQuery("SET {class} constructors = {$ fun[kind='constructor'] | count}")


      find("class").forEach {
        it.data["lines"] = it.code.relevantCodeLines()

        val body = it.find("$>ClassBody>body").first()
        it.data["methods"] = body.findValue("$ fun[kind='method' || kind='get' || kind='constructor'] | count") as Int

        it.data["lines"] = body.code.relevantCodeLines()
      }
    }

    fileNode.data["lines"] = fileNode.code.relevantCodeLines()
    fileNode.data["textLines"] = fileNode.code.split("\n").size
    fileNode.data["functions"] = fileNode.find("$>body>fun").size
    fileNode.data["classes"] = fileNode.find("$>body>class").size
    fileNode.data["bindings"] = fileNode.find("$>body>binding").size
  }
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
