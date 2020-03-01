package co.elpache.codelens.codeSearch.search

import co.elpache.codelens.codeSearch.parser.parseQuery
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.Vid

fun CodeTree.finder() = ContextNode(rootVid(), this)

fun CodeTree.find(query: String) = ContextNode(rootVid(), this).find(query)

fun CodeTree.printTree() = ContextNode(rootVid(), this).printTree()

fun Collection<ContextNode>.paramsValues(key: String, param: String): List<Pair<Vid, Double>> {
  return filter { it.vertice.contains(param) }
    .map { Pair(it[key].toString(), it.vertice.getDouble(param)) }
}

open class ContextNode(val vid: Vid, val tree: CodeTree) {

  companion object {
    val pseudoElementsRegistry = HashMap<String, String>()
  }

  open fun find(css: String): List<ContextNode> {
    return (parseQuery(css).evaluate(this) as List<ContextNode>)
  }

  operator fun get(key: String) = tree.v(vid)[key]

  operator fun set(key: String, value: Any?) {
    if (value != null) tree.v(vid)[key] = value
  }

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

  open val vertice: Vertice get() = tree.v(vid)
  val parent: Vertice? get() = tree.parentNode(vid)

  open val children: List<ContextNode> get() = tree.children(vid).map { ContextNode(it, tree) }

  fun adj(): List<ContextNode> = tree.adj(vid).map { ContextNode(it, tree) }

  fun adj(relation: String): List<ContextNode> = tree.adj(vid, relation).map { ContextNode(it, tree) }

  override fun toString() = vertice.toString()

  fun printTree() = println(tree.subTree(vid).asString())
  fun containsKey(name: String) = vertice.containsKey(name)
  fun toMap() = vertice.toMap()
}

class EmptySearchNode : ContextNode("--Empty--", CodeTree()) {
  override val code = ""
  override val vertice: Vertice = Vertice()
  override val children = emptyList<ContextNode>()
  override fun find(css: String) = listOf<ContextNode>()
  override fun findValue(css: String) = 0
}

fun Collection<ContextNode>.firstNode() = firstOrNull() ?: EmptySearchNode()
fun Collection<ContextNode>.vids() = map { it.vertice.vid }
