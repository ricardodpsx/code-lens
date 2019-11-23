package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.search.finder

data class SearchResults(
  val treeWithDescendants: CodeTree,
  val results: List<Vid> = listOf()
)

data class NodeContentsResults(
  val text: String,
  val ast: Map<String, Any>
)

data class AnalyticsResults(val rows: List<List<Int>>)

class CodeExplorerUseCases(private val factory: Factory = Factory()) {

  val code = factory.createBaseCode()

  fun find(query: String) = code.finder().find(query)

  //Todo: Separate into two use cases
  fun selectCodeWithParents(query: Vid): SearchResults {
    //Todo: Refactor, make it handle parse exception specifically
    return try {
      val res = find(query).vids()
      SearchResults(
        code.treeFromChildren(res),
        res
      )
    } catch (e: Exception) {
      SearchResults(code)
    }
  }


  fun getFrequencyByParam(query: Vid, param: String) =
    AnalyticsResults(
      frequency(find(query).paramsValues(param))
    )


  fun getStatistics(query: String, param: String): DescriptiveStatistics {
    return statistics(find(query).paramsValues(param))
  }

  fun loadNodeContents(vid: String) =
    NodeContentsResults(
      code.v(vid).code,
      code.subTree(vid).toMap()
    )

  fun getPossibleIntParams(query: String) =
    try {
      find(query)
        .map { it.data.keys }
        .flatten()
        .filter { !listOf("name", "type").contains(it) }
        .distinct()
    } catch (e: Exception) {
      arrayListOf<String>()
    }
}

