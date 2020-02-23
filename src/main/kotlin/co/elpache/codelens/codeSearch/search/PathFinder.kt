package co.elpache.codelens.codeSearch.search

import co.elpachecode.codelens.cssSelector.Query
import co.elpachecode.codelens.cssSelector.RelationType
import co.elpachecode.codelens.cssSelector.TypeSelector

val funcRegistry2 = HashMap<String, (res: PathFinder, params: List<String>) -> Any>()

class PathFinder2(private val ctx: ContextNode) {
  val tree = ctx.tree
  val vid = ctx.vid

  fun find(query: Query): List<ContextNode> {
    return findRec(ctx, query.selectors)
  }

}

fun findRec(ctx: ContextNode, selectors: List<TypeSelector>): List<ContextNode> {
  if (selectors.isEmpty()) return listOf()
  val currentSelector = selectors.first()

  val matches = currentSelector.evaluate(ctx) || currentSelector.name == "$"

  if (matches && selectors.size == 1)
    return listOf(ctx)

  if (!matches) {
    //Optimization, Don't look for files inside files
    if (ctx.vertice.isA("file") && selectors.first().name == "file")
      return listOf()

    val res = mutableListOf<ContextNode>()
    ctx.children.forEach { res.addAll(findRec(it, selectors)) }
    return res
  }

  val res = mutableListOf<ContextNode>()

  if (currentSelector.relation.type == RelationType.FOLLOW_RELATION)
    ctx.children.forEach { res.addAll(findRec(it, selectors.drop(1))) }
  else
    ctx.adj(currentSelector.relation.name).forEach {
      var found = findNext(it, selectors.drop(1))

      if (found.isEmpty()) found = findNext(it, selectors)

      res.addAll(found)
    }

  return res

}

fun findNext(ctx: ContextNode, selectors: List<TypeSelector>): List<ContextNode> {
  if (selectors.isEmpty()) return listOf()
  val currentSelector = selectors.first()
  val matches = currentSelector.evaluate(ctx) || currentSelector.name == "$"

  if (matches && selectors.size == 1)
    return listOf(ctx)

  if (!matches) return listOf()

  val res = mutableListOf<ContextNode>()

  if (currentSelector.relation.type == RelationType.FOLLOW_RELATION)
    ctx.children.forEach { res.addAll(findRec(it, selectors.drop(1))) }
  else
    ctx.adj(currentSelector.relation.name).forEach {
      res.addAll(findNext(it, selectors.drop(1)))
    }

  return res
}

