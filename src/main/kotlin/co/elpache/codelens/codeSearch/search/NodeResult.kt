package co.elpache.codelens.codeSearch.search

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.Func
import co.elpachecode.codelens.cssSelector.Query
import co.elpachecode.codelens.cssSelector.RelationTypes
import co.elpachecode.codelens.cssSelector.TypeSelector
import co.elpachecode.codelens.cssSelector.parseCssSelector
import co.elpachecode.codelens.cssSelector.parseSetQuery

fun CodeTree.finder() = NodeResult(rootVid(), this)

val funcRegistry = HashMap<String, (res: NodeResult, params: List<String>) -> Any>()

open class NodeResult(val vid: Vid, val tree: CodeTree) {

  fun codeNode() = tree.v(vid)

  //Todo: This should be cached and go in VData
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
  }

  fun printTree() = println(tree.subTree(vid).asString())

  override fun toString() = data.toString()

  private fun matches(selector: TypeSelector) =
    matches(tree.v(vid), selector)
        && (selector.pseudoAttribute == null || find(selector.pseudoAttribute.query).size > 0)

  fun data(css: String) = find(css).map { it.data }

  open fun find(css: String): NodeResultSet {
    val query = parseCssSelector(css)
    return find(query)
  }

  private fun find(query: Query): NodeResultSet {
    return findMatchingPathsFromSubSet(
      listOf(this).plus(descendants()), query.selectors
    ).toResultSet(query)
  }

  fun setQuery(str: String): NodeResultSet {
    val query = parseSetQuery(str)
    val nodesToSet = find(query.nodesToSet)
    nodesToSet.forEach { node ->
      query.paramSetters.forEach { (paramName, setQuery) ->
        if (setQuery is Query)
          node.data[paramName] = node.find(setQuery).value()
        else
          node.data[paramName] = funcRegistry[(setQuery as Func).op]!!(this, (setQuery as Func).params)
      }
    }
    return nodesToSet
  }

  private fun find(selectors: List<TypeSelector>) = findMatchingPathsFromSubSet(descendants(), selectors)

  private fun findNext(selectors: List<TypeSelector>) = findMatchingPathsFromSubSet(children, selectors)

  /**
   * subSet: Subset of Nodes from where to start to search the items that match the path
   * path: The path defined by the query language. example class fun>if
   */
  private fun findMatchingPathsFromSubSet(
    subSet: List<NodeResult>,
    path: List<TypeSelector>
  ): List<NodeResult> {
    if (path.isEmpty()) return listOf(this)

    return subSet.filter {
      if (path.first().name == "$") it == this
      else it.matches(path.first())
    }.map {
      if (path.first().relationType.type == RelationTypes.CHILDREN)
        it.find(path.drop(1))
      else
        it.findNext(path.drop(1))
    }.flatten().distinctBy { it.vid }
  }
}

class EmptyResult : NodeResult("--Empty--", CodeTree()) {
  override val code = ""
  override val data: VData = VData()
  override val children = emptyList<NodeResult>().toResultSet()
  override fun find(css: String) = emptyList<NodeResult>().toResultSet()
}