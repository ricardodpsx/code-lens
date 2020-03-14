package co.elpache.codelens.codeSearch.search

import co.elpache.codelens.codeSearch.parser.parseQuery
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.vids
import co.elpache.codelens.useCases.SearchResults

fun CodeTree.find(query: String, from: Vertice? = null) =
  findValue(query, from).results

fun CodeTree.findValue(query: String, from: Vertice? = null): SearchResults {
  val res = parseQuery(query).evaluate(ContextNode(from?.vid ?: rootDirVid!!, this))
  return if (res is SearchResults) res
  else {
    val res = res as List<Vertice>
    SearchResults(treeFromChildren(res.vids()), res)
  }
}

data class ContextNode(val vid: Vid, val tree: CodeTree) {
  val vertice get() = tree.v(vid)
}


