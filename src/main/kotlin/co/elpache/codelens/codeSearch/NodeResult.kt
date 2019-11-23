package co.elpachecode.codelens.cssSelector.search

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.RelationTypes
import co.elpachecode.codelens.cssSelector.TypeSelector
import co.elpachecode.codelens.cssSelector.matches
import co.elpachecode.codelens.cssSelector.parseCssSelector

fun CodeTree.finder() = NodeResult(rootVid(), this)

open class NodeResult(val vid: Vid, val tree: CodeTree) {

  fun codeNode() = tree.v(vid)

  open val code: String get() = tree.v(vid).code
  open val data: VData get() = tree.v(vid)
  open val children: NodeResultSet
    get() = tree.children(vid).map {
      NodeResult(
        it,
        tree
      )
    }.toResultSet()

  private fun descendants() = tree.descendants(vid).map {
    NodeResult(
      it,
      tree
    )
  }.toResultSet()

  fun printTree() = println(tree.subTree(vid).asString())

  override fun toString() = data.toString()

  private fun matches(selector: TypeSelector) =
    matches(tree.v(vid), selector)

  fun data(css: String) = find(css).map { it.data }

  open fun find(css: String) =
    findMatchingPathsFromSubSet(
      listOf(this).plus(descendants()).toResultSet(),
      parseCssSelector(css).selectors
    )

  private fun find(selectors: List<TypeSelector>) = findMatchingPathsFromSubSet(descendants(), selectors)

  private fun findNext(selectors: List<TypeSelector>) = findMatchingPathsFromSubSet(children, selectors)

  /**
   * subSet: Subset of Nodes from where to start to search the items that match the path
   * selectors: The path defined by the query language. example class fun>if
   */
  private fun findMatchingPathsFromSubSet(
    subSet: NodeResultSet,
    path: List<TypeSelector>
  ): NodeResultSet {
    if (path.isEmpty()) return listOf(this).toResultSet()

    return subSet.filter {
      if (path.first().name == "$") it == this
      else it.matches(path.first())
    }.map {
      if (path.first().relationType.type == RelationTypes.CHILDREN)
        it.find(path.drop(1))
      else
        it.findNext(path.drop(1))
    }.flatten().distinctBy { it.vid }.toResultSet()
  }
}

class EmptyResult : NodeResult("--Empty--", CodeTree()) {
  override val code = ""
  override val data: VData = VData()
  override val children = emptyList<NodeResult>().toResultSet()
  override fun find(css: String) = emptyList<NodeResult>().toResultSet()
}