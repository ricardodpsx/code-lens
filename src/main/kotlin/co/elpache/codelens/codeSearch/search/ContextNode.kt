package co.elpache.codelens.codeSearch.search

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.Query
import co.elpachecode.codelens.cssSelector.parseQuery
import co.elpachecode.codelens.cssSelector.parseSetQuery

fun CodeTree.finder() = ContextNode(rootVid(), this)
fun Collection<ContextNode>.paramsValues(param: String): List<Pair<Vid, Double>> {
  return filter { it.data.contains(param) }
    .map { Pair(it.vid, it.data.getDouble(param)) }
}

open class ContextNode(val vid: Vid, val tree: CodeTree) {

  companion object {
    val pseudoElementsRegistry = HashMap<String, String>()
  }

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
      return if (tree.v(vid).isA("file"))
        tree.v(vid).getString("code")
      else {
        val fileNode = tree.ancestors(vid).find { tree.v(it).isA("file") } as Vid

        val contents = tree.v(fileNode).getString("code")
        contents.substring(tree.v(vid).start, tree.v(vid).end)
      }
    }

  open val data: VData get() = tree.v(vid)
  val parent: VData? get() = tree.parentNode(vid)

  open val children: List<ContextNode>
    get() = tree.children(vid).map { ContextNode(it, tree) }

  fun adj(): List<ContextNode> = tree.adj(vid).map { ContextNode(it, tree) }

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


  fun printTree() = println(asString())

  fun asString() = tree.subTree(vid).asString()

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
