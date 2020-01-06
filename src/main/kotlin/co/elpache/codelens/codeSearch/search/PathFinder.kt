package co.elpache.codelens.codeSearch.search

import co.elpachecode.codelens.cssSelector.Query
import co.elpachecode.codelens.cssSelector.RelationType
import co.elpachecode.codelens.cssSelector.TypeSelector
import co.elpachecode.codelens.cssSelector.parseQuery

val funcRegistry = HashMap<String, (res: PathFinder, params: List<String>) -> Any>()

class PathFinder(private val ctx: ContextNode) {
  val tree = ctx.tree
  val vid = ctx.vid

  private fun descendants() = tree.descendants(vid).map {
    ContextNode(it, tree)
  }

  private fun allNodes() =
    //When searching from root it should look for all nodes
    if (tree.rootVid == vid)
      tree.vertices.map { ContextNode(it.key, tree) }
    else
      descendants()

  fun find(query: Query): List<ContextNode> {
    return findMatchingPathsFromSubSet(
      listOf(ctx).plus(allNodes()).distinctBy { it.vid }, query.selectors
    )
  }

  private fun find(selectors: List<TypeSelector>) =
    findMatchingPathsFromSubSet(descendants(), selectors)

  private fun findNext(selectors: List<TypeSelector>) = findMatchingPathsFromSubSet(
    ctx.adj(), selectors
  )

  private fun expandForPseudoElements(path: List<TypeSelector>, subset: List<ContextNode>):
      Pair<List<TypeSelector>, List<ContextNode>> {
    if (path.first().isPseudoElement()) {
      val pseudoElementQuery = ctx.data.getString(path.first().name)
      if (pseudoElementQuery.isNotBlank()) {
        val expandedSelectors = parseQuery(pseudoElementQuery).selectors.plus(path.drop(1))
        val ctxWithPseudoElementParent = listOf(ctx).plus(subset)
        return expandedSelectors to ctxWithPseudoElementParent
      }
    }
    return path to subset
  }

  /**
   * subSet: Subset of Nodes from where to start to search the items that match the path
   * path: The path defined by the query language. example class fun>if
   */
  //TODO: This can be greatly improved by using actual sets and VIDs instead of lists of contextNode
  private fun findMatchingPathsFromSubSet(
    subSet: List<ContextNode>,
    path: List<TypeSelector>
  ): List<ContextNode> {
    if (path.isEmpty()) return listOf(ctx)
    val (path, subSet) = expandForPseudoElements(path, subSet)

    return subSet
      .asSequence()
      .map { PathFinder(it) }
      .filter {
        if (path.first().name == "$") it.ctx == ctx
        else path.first().evaluate(it.ctx)
      }.map {
        if (path.first().relation.type == RelationType.FOLLOW_RELATION)
          it.find(path.drop(1))
        else
          it.findNext(path.drop(1))
      }.flatten().distinctBy { it.vid }
      .toList()
  }
}

