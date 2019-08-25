package co.elpache.codelens

import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.buildTreeFromChildren
import co.elpachecode.codelens.cssSelector.CssSearch
import co.elpachecode.codelens.cssSelector.parseCssSelector
import kotlin.math.max

data class SearchResult(
  val treeWithDescendants: CodeTree,
  val results: List<Vid> = listOf()
)


fun selectCodeWithParents(tree: CodeTree, cssSelector: Vid): SearchResult {
  //Todo: Refactor, make it handle parse exception specifically
  return try {
    val parsedCss = parseCssSelector(cssSelector)
    val res = CssSearch(parsedCss, tree).search()
    val resTree = buildTreeFromChildren(tree, res)
    SearchResult(resTree, res)
  } catch (e: Exception) {
    SearchResult(tree)
  }
}


fun search(tree: CodeTree, css: String, vid: Vid = tree.rootVid()) =
  CssSearch(parseCssSelector(css), tree).search(vid)

fun depth(tree: CodeTree, vid: Vid): Int {
  var maxDepth = 0
  for (cVid in tree.children(vid))
    maxDepth = max(depth(tree, cVid), maxDepth)

  return (if ((tree.v(vid) as CodeEntity).type == "block") 1 else 0) + maxDepth
}