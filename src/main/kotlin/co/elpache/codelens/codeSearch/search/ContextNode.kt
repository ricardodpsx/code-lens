package co.elpache.codelens.codeSearch.search

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.Query
import co.elpachecode.codelens.cssSelector.parseQuery
import co.elpachecode.codelens.cssSelector.parseSetQuery

fun CodeTree.finder() = ContextNode(rootVid(), this)
fun Collection<ContextNode>.paramsValues(param: String): List<Pair<Vid, Int>> {
  return filter { it.data.contains(param) }
    .map { Pair(it.vid, it.data[param] as Int) }
}

class EmptySearchNode : ContextNode("--Empty--", CodeTree()) {
  override val code = ""
  override val data: VData = VData()
  override val children = emptyList<ContextNode>()
  override fun find(css: String) = listOf<ContextNode>()
  override fun findValue(css: String) = 0
}

fun Collection<ContextNode>.firstNode() = firstOrNull() ?: EmptySearchNode()
fun Collection<ContextNode>.vids() = map { it.vid }

open class ContextNode(val vid: Vid, val tree: CodeTree) {
  open fun find(css: String): List<ContextNode> {
    return (parseQuery(css).evaluate(this) as List<ContextNode>)
  }

  operator fun get(key: String) = tree.v(vid)[key]

  open fun findValue(css: String): Any? {
    return parseQuery(css).evaluate(this)
  }

  fun codeNode() = tree.v(vid)

  open val code: String
    get() {
      return if (tree.v(vid).type == "file")
        tree.v(vid).getString("code")
      else {
        val fileNode = tree.ancestors(vid).first { tree.v(it).type == "file" }
        val contents = tree.v(fileNode).getString("code")
        contents.substring(tree.v(vid).startOffset, tree.v(vid).endOffset)
      }
    }

  open val data: VData get() = tree.v(vid)
  open val children: List<ContextNode>
    get() = tree.children(vid).map { ContextNode(it, tree) }

  override fun toString() = data.toString()

  fun data(css: String) = find(css).map { it.data }


  fun setQuery(str: String): List<ContextNode> {
    val query = parseSetQuery(str)
    val nodesToSet = PathFinder(this).find(query.nodesToSet)
    nodesToSet.forEach { node ->
      query.paramSetters.forEach { (paramName, setQuery) ->
        if (setQuery is Query)
          node.data[paramName] = setQuery.evaluate(node)!!
        else
          TODO("Functions not supported yet")
      }
    }
    return nodesToSet
  }


  fun printTree() = println(tree.subTree(vid).asString())

}