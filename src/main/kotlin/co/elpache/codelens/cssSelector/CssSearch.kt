package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.codetree.CodeEntity
import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.codetree.LanguageCodeEntity
import co.elpache.codelens.codetree.NodeData
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.ancestors
import org.jetbrains.kotlin.utils.addIfNotNull
import java.util.LinkedList



fun CodeTree.finder() = NodeResult(tree.rootVid(), this)

open class NodeResult(val vid: Vid, val codeBase: CodeTree) {
  val tree = codeBase.tree

  fun codeNode() = tree.v(vid) as CodeEntity

  open val type: String get() = codeNode().type
  open val code: String get() = (codeNode() as LanguageCodeEntity).code
  open val data: NodeData get() = tree.v(vid).data
  open val children: List<NodeResult> get() = tree.children(vid).map { NodeResult(it, codeBase) }

  fun children(selector: String? = null) =
    children.filter { selector == null || it.matches(selector) }

  open fun firstChildren(selector: String) =
    children(selector).firstOrNull() ?: EmptyResult()

  open fun first(selector: String) =
    find(selector).firstOrNull() ?: EmptyResult()

  open fun find(css: String): List<NodeResult> {
    return CssSearch(parseCssSelector(css), codeBase)
      .search(vid).map {
        NodeResult(it, codeBase)
      }
  }

  fun data(css: String) = find(css).map { it.data }

  fun byType(selector: String) =
    codeBase
      .descendants(vid)
      .map { NodeResult(it, codeBase) }
      .filter { it.matches(selector) }

  fun printTree() {
    println(codeBase.subTreeFrom(vid).asString())
  }

  override fun toString() = data.toString()

  fun matches(css: String) = matches(tree.v(vid).data, parseCssSelector(css).selectors.first())
}

class EmptyResult : NodeResult("--Empty--", CodeTree()) {
  override val code = ""
  override val data: NodeData = NodeData()
  override val children = emptyList<NodeResult>()
  override fun firstChildren(css: String) = this
  override fun find(css: String) = emptyList<NodeResult>()
}

data class CssSearch(val selectors: CssSelectors, val code: CodeTree) {
  val found = arrayListOf<Vid>()
  val tree = code.tree

  fun search(fromVertice: Vid = tree.rootVid()): List<Vid> {
    dfs(fromVertice)
    return found
  }

  fun find(fromVertice: Vid = tree.rootVid()): List<NodeResult> {
    dfs(fromVertice)
    return found.map { NodeResult(it, code) }
  }


  private fun dfs(vid: Vid) {
    if (matches(code.data(vid), selectors.selectors.last()))
      found.addIfNotNull(TypeSelectorSearch(vid, selectors.selectors, code).find())

    for (cVid in tree.children(vid))
      dfs(cVid)
  }

}

private typealias Match = Pair<TypeSelector, Vid>

class TypeSelectorSearch(val from: Vid, selectors: List<TypeSelector>, val code: CodeTree) {
  val tree = code.tree
  private val nodes = LinkedList<Vid>()
  private val parentSelectors = LinkedList(selectors)
  private var current: TypeSelector? = null


  fun find(): Vid? {
    nodes.addAll(ancestors(tree, from).reversed())
    parentSelectors.pollLast()
    nodes.pollLast()

    while (nextMatch() != null);
    return if (parentSelectors.isEmpty()) from else null
  }

  private fun nextMatch(): Match? {
    if (parentSelectors.isEmpty()) return null

    return when (parentSelectors.peekLast().relationType.type) {
      RelationTypes.CHILDREN -> takeChildren()
      RelationTypes.DIRECT_DESCENDANT -> takeDescendants()
    }
  }

  private fun takeDescendants(): Match? {
    return if (nodeMatchesSelector()) takeSelectorAndNode()
    else null
  }

  private fun takeChildren(): Match? {
    while (hasMore())
      if (nodeMatchesSelector()) return takeSelectorAndNode()
      else discardNode()

    return null
  }

  private fun hasMore() = nodes.isNotEmpty() && parentSelectors.isNotEmpty()

  private fun discardNode() = nodes.pollLast()

  private fun nodeMatchesSelector() =
    matches(
      tree.v(nodes.peekLast()).data,
      parentSelectors.peekLast()
    )

  private fun takeSelectorAndNode() = parentSelectors.pollLast() to nodes.pollLast()
}