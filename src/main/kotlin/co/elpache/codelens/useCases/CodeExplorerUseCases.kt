package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.codetree.string
import co.elpache.codelens.tree.Vid

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

  var code = factory.createBaseCode()

  //Todo: Separate into two use cases
  fun selectCodeWithParents(query: Vid): SearchResults {
    //Todo: Refactor, make it handle parse exception specifically
    return try {
      val res = code.selectBy(query).vids()
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
      frequency(code.selectBy(query).paramsValues(param))
    )


  fun getStatistics(query: String, param: String): DescriptiveStatistics {
    return statistics(code.selectBy(query).paramsValues(param))
  }

  fun loadNodeContents(vid: String) =
    NodeContentsResults(
      code.file(vid).string("code"),
      code.subTreeFrom(vid).toMap()
    )

  fun getPossibleIntParams(query: String) =
    try {
      code.selectBy(query)
        .map { it.data.keys }
        .flatten()
        .filter { !listOf("name", "type").contains(it) }
        .distinct()
    } catch (e: Exception) {
      arrayListOf<String>()
    }
}

