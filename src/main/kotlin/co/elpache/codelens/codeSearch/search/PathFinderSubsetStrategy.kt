package co.elpache.codelens.codeSearch.search

import co.elpachecode.codelens.cssSelector.Query
import co.elpachecode.codelens.cssSelector.RelationType
import co.elpachecode.codelens.cssSelector.TypeSelector
import co.elpachecode.codelens.cssSelector.parseQuery

val funcRegistry = HashMap<String, (res: PathFinder, params: List<String>) -> Any>()

class PathFinder(private val ctx: ContextNode) {
  val tree = ctx.tree
  val vid = ctx.vid

  fun find(query: Query): List<ContextNode> {
    return findMatchingPathsFromSubSet(
      listOf(ctx).plus(ctx.descendants()).distinctBy { it.vid }, query.selectors
    )
  }

  private fun find(selectors: List<TypeSelector>) =
    findMatchingPathsFromSubSet(ctx.descendants(), selectors)

  private fun findNext(selectors: List<TypeSelector>) = findMatchingPathsFromSubSet(
    ctx.adj(), selectors
  )

  private fun expandForPseudoElements(path: List<TypeSelector>, subset: List<ContextNode>):
      Pair<List<TypeSelector>, List<ContextNode>> {
    if (path.first().isPseudoElement()) {
      val pseudoElementQuery = ctx.vertice.getString(path.first().name)
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

