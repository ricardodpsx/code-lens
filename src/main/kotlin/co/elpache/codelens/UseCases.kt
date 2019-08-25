package co.elpache.codelens

import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.buildTreeFromChildren
import co.elpache.codelens.tree.subTree
import co.elpachecode.codelens.cssSelector.CssSearch
import co.elpachecode.codelens.cssSelector.parseCssSelector
import kotlin.math.max

data class SearchResults(
  val treeWithDescendants: CodeTree,
  val results: List<Vid> = listOf()
)

data class NodeContentsResults(
  val text: String,
  val ast: Map<String, Any>
)

data class AnalyticsResults(val rows: List<List<Int>>)

class UseCases(val codeBase: CodeTree = expandFullCodeTree(CodeBase.load("src/test/kotlin/co/elpache/codelens/subpackage/"))) {

  fun selectCodeWithParents(query: Vid): SearchResults {
    //Todo: Refactor, make it handle parse exception specifically
    return try {
      val res = selectBy(query)
      SearchResults(
        buildTreeFromChildren(codeBase, res),
        res
      )
    } catch (e: Exception) {
      SearchResults(codeBase)
    }
  }

  fun getFrequencyByParam(query: Vid, param: String): AnalyticsResults {

    val m = selectBy(query)
      .filter { codeBase.v(it).contains(param) }
      .map { codeBase.v(it).value<Int>(param) }
      .groupBy { it }
      .map { listOf(it.key, it.value.size) }
      .sortedBy { it[0] }

    return AnalyticsResults(m)
  }

  fun loadNodeContents(vid: String) =
    NodeContentsResults(
      (codeBase.v(vid) as CodeFile).contents(),
      toMap(subTree(codeBase, vid))
    )

  fun getPossibleIntParams(query: String) =
    selectBy(query)
      .map { codeBase.v(it).data().keys }
      .flatten()
      .filter{ !listOf("name", "type").contains(it) }
      .distinct()

  private fun selectBy(
    query: Vid
  ) = CssSearch(parseCssSelector(query), codeBase).search()
}

//Todo: Move
fun search(tree: CodeTree, css: String, vid: Vid = tree.rootVid()) =
  CssSearch(parseCssSelector(css), tree).search(vid)

fun depth(tree: CodeTree, vid: Vid): Int {
  var maxDepth = 0
  for (cVid in tree.children(vid))
    maxDepth = max(depth(tree, cVid), maxDepth)

  return (if ((tree.v(vid) as CodeEntity).type == "block") 1 else 0) + maxDepth
}