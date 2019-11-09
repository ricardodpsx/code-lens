package co.elpachecode.codelens.cssSelector.search

import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.codetree.NodeData
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.RelationTypes
import co.elpachecode.codelens.cssSelector.TypeSelector
import co.elpachecode.codelens.cssSelector.matches
import co.elpachecode.codelens.cssSelector.parseCssSelector

fun CodeTree.finder() = NodeResult(tree.rootVid(), this)

open class NodeResult(val vid: Vid, val codeBase: CodeTree) {
  val tree = codeBase.tree

  fun codeNode() = codeBase.node(vid)

  open val type: String get() = codeNode()["type"] as String
  open val code: String get() = codeBase.code(vid)
  open val data: NodeData get() = tree.v(vid)
  open val children: NodeResultSet
    get() = tree.children(vid).map {
      NodeResult(
        it,
        codeBase
      )
    }.toResultSet()

  private fun descendants() = codeBase.descendants(vid).map {
    NodeResult(
      it,
      codeBase
    )
  }.toResultSet()

  fun printTree() = println(codeBase.subTreeFrom(vid).asString())

  override fun toString() = data.toString()

  private fun matches(selector: TypeSelector) =
    matches(codeBase.data(vid), selector)

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
  override val data: NodeData = NodeData()
  override val children = emptyList<NodeResult>().toResultSet()
  override fun find(css: String) = emptyList<NodeResult>().toResultSet()
}