package co.elpache.codelens.codeSearch.search

import co.elpache.codelens.codeSearch.parser.Query
import co.elpache.codelens.codeSearch.parser.RelationType
import co.elpache.codelens.codeSearch.parser.TypeSelector
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.Vid

val funcRegistry2 = HashMap<String, (res: PathFinder, params: List<String>) -> Any>()

class PathFinder(val tree: CodeTree, private val from: Vid) {
  fun find(query: Query): List<Vertice> {
    return findRec(tree, from, query.selectors).map { tree.v(it) }
  }
}

fun findRec(tree: CodeTree, vid: Vid, selectors: List<TypeSelector>): Set<Vid> {
  if (selectors.isEmpty()) return emptySet()
  val currentSelector = selectors.first()

  val matches = currentSelector.evaluate(ContextNode(vid, tree)) || currentSelector.name == "$"

  if (matches && selectors.size == 1) {
    return setOf(vid).plus(tree.children(vid).flatMap { findRec(tree, it, selectors) })
  }

  if (!matches) {
    //Optimization, Don't look for files inside files
    if (tree.v(vid).isA("codeFile") && selectors.first().name == "codeFile")
      return emptySet()

    val res = mutableSetOf<Vid>()
    tree.children(vid).forEach { res.addAll(findRec(tree, it, selectors)) }
    return res
  }

  val res = mutableSetOf<Vid>()

  if (currentSelector.relation.type == RelationType.FOLLOW_RELATION) {
    tree.children(vid).forEach { res.addAll(findRec(tree, it, selectors.drop(1))) }
  } else
    tree.adj(vid, currentSelector.relation.name).forEach {
      var found = findNext(tree, it, selectors.drop(1))

      if (found.isEmpty()) found = findNext(tree, it, selectors)

      res.addAll(found)
    }

  return res

}

fun findNext(tree: CodeTree, vid: Vid, selectors: List<TypeSelector>): Set<Vid> {
  if (selectors.isEmpty()) return emptySet()
  val currentSelector = selectors.first()
  val matches = currentSelector.evaluate(ContextNode(vid, tree)) || currentSelector.name == "$"

  if (matches && selectors.size == 1)
    return setOf(vid)

  if (!matches) return emptySet()

  val res = mutableSetOf<Vid>()

  if (currentSelector.relation.type == RelationType.FOLLOW_RELATION)
    tree.children(vid).forEach { res.addAll(findRec(tree, it, selectors.drop(1))) }
  else
    tree.adj(vid, currentSelector.relation.name).forEach {
      res.addAll(findNext(tree, it, selectors.drop(1)))
    }

  return res
}

