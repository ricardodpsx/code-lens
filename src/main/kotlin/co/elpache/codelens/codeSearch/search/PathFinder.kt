package co.elpache.codelens.codeSearch.search

import co.elpachecode.codelens.cssSelector.Query
import co.elpachecode.codelens.cssSelector.RelationType
import co.elpachecode.codelens.cssSelector.TypeSelector


val funcRegistry = HashMap<String, (res: PathFinder, params: List<String>) -> Any>()


class PathFinder(val ctx: ContextNode) {
  val tree = ctx.tree
  val vid = ctx.vid

  private fun descendants() = tree.descendants(vid).map {
    ContextNode(
      it,
      tree
    )
  }

  fun find(query: Query): List<ContextNode> {
    return findMatchingPathsFromSubSet(
      listOf(ctx).plus(descendants()), query.selectors
    )
  }

  private fun find(selectors: List<TypeSelector>) = findMatchingPathsFromSubSet(descendants(), selectors)

  private fun findNext(selectors: List<TypeSelector>) = findMatchingPathsFromSubSet(ctx.children, selectors)
  /**
   * subSet: Subset of Nodes from where to start to search the items that match the path
   * path: The path defined by the query language. example class fun>if
   */
  private fun findMatchingPathsFromSubSet(
    subSet: List<ContextNode>,
    path: List<TypeSelector>
  ): List<ContextNode> {
    if (path.isEmpty()) return listOf(ctx)

    return subSet
      .map { PathFinder(it) }
      .filter {
        if (path.first().name == "$") it.ctx == ctx
        else path.first().matches(it.ctx)
      }.map {
        if (path.first().relationType == RelationType.CHILDREN)
          it.find(path.drop(1))
        else
          it.findNext(path.drop(1))
      }.flatten().distinctBy { it.vid }
  }
}

