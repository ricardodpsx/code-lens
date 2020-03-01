package co.elpache.codelens.codeSearch.search

import co.elpache.codelens.codeSearch.parser.Query
import co.elpache.codelens.codeSearch.parser.RelationType
import co.elpache.codelens.codeSearch.parser.TypeSelector
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vid

val funcRegistry2 = HashMap<String, (res: PathFinder, params: List<String>) -> Any>()

class PathFinder(val tree: CodeTree, val from: Vid? = null) {
  fun find(query: Query): List<ContextNode> {
    return if (from == null) {
      val res = tree.vertices.filter { query.selectors.first().evaluate(ContextNode(it.key, tree)) }
      val result = mutableSetOf<Vid>()
      res.forEach {
        result.addAll(findRec(ContextNode(it.key, tree), query.selectors))
      }
      result.map { ContextNode(it, tree) }
    } else
      findRec(ContextNode(from, tree), query.selectors).map { ContextNode(it, tree) }
  }
}

fun findRec(ctx: ContextNode, selectors: List<TypeSelector>): Set<Vid> {
  if (selectors.isEmpty()) return emptySet()
  val currentSelector = selectors.first()

  val matches = currentSelector.evaluate(ctx) || currentSelector.name == "$"

  if (matches && selectors.size == 1) {
    return setOf(ctx.vid).plus(ctx.children.flatMap { findRec(it, selectors) })
  }

  if (!matches) {
    //Optimization, Don't look for files inside files
    if (ctx.vertice.isA("codeFile") && selectors.first().name == "codeFile")
      return emptySet()

    val res = mutableSetOf<Vid>()
    ctx.children.forEach { res.addAll(findRec(it, selectors)) }
    return res
  }

  val res = mutableSetOf<Vid>()

  if (currentSelector.relation.type == RelationType.FOLLOW_RELATION) {
    ctx.children.forEach { res.addAll(findRec(it, selectors.drop(1))) }
  } else
    ctx.adj(currentSelector.relation.name).forEach {
      var found = findNext(it, selectors.drop(1))

      //if (found.isEmpty()) found = findNext(it, selectors)

      res.addAll(found)
    }

  return res

}

fun findNext(ctx: ContextNode, selectors: List<TypeSelector>): Set<Vid> {
  if (selectors.isEmpty()) return emptySet()
  val currentSelector = selectors.first()
  val matches = currentSelector.evaluate(ctx) || currentSelector.name == "$"

  if (matches && selectors.size == 1)
    return setOf(ctx.vid)

  if (!matches) return emptySet()

  val res = mutableSetOf<Vid>()

  if (currentSelector.relation.type == RelationType.FOLLOW_RELATION)
    ctx.children.forEach { res.addAll(findRec(it, selectors.drop(1))) }
  else
    ctx.adj(currentSelector.relation.name).forEach {
      res.addAll(findNext(it, selectors.drop(1)))
    }

  return res
}

