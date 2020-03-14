package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.codeSearch.search.find
import co.elpache.codelens.codeSearch.search.findValue
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.Vid

data class SearchResults(
  val treeWithDescendants: CodeTree? = null,
  val results: List<Vertice> = listOf()
)

data class SearchResultsWithParams(
  val codeTree: CodeTree?,
  val results: List<Vertice> = listOf(),
  val analyticsParams: List<String>
)

data class NodeContentsResults(
  val text: String,
  val ast: CodeTree
)

data class ParamFrequencyRow(val paramValue: Double, val frequency: Int, val nodes: List<Vertice>)
data class AnalyticsResults(val rows: List<ParamFrequencyRow>)

class CodeExplorerUseCases(factory: Factory = Factory()) {
  val codeTree by lazy { factory.createBaseCode() }

  fun find(query: String) = codeTree.find(query)

  fun selectCodeWithParents(query: Vid): SearchResults {
    return try {
      codeTree.findValue(query)
    } catch (e: Exception) {
      e.printStackTrace()
      SearchResults()
    }
  }

  fun getSearchResultsWithParams(query: String): SearchResultsWithParams {
    return with(selectCodeWithParents(query)) {
      SearchResultsWithParams(
        treeWithDescendants,
        results,
        getPossibleIntParams(results)
      )
    }
  }

  //Gets the distribution of parameters in nodes,
  //This make it easy to answer questions like "how many functions have more than 5 params" etc
  fun getParamDistribution(query: String, param: String) =
    AnalyticsResults(
      frequency(
        find(query).filter { it[param] != null }
          .map { it to it.getDouble(param) })
    )


  fun getMetricStatistics(query: String, param: String): DescriptiveStatistics {
    return statistics(find(query).filter { it[param] != null }
      .map { it.vid to it.getDouble(param) })
  }

  fun loadNodeContents(vid: String) =
    NodeContentsResults(
      codeTree.code(vid),
      codeTree.subTree(vid)
    )

  fun getPossibleIntParams(results: List<Vertice>) =
    try {
      results
        .map {
          it.params()
        }
        .flatten()
        .distinct()
    } catch (e: Exception) {
      arrayListOf<String>()
    }


  fun search(query: String): List<Vertice> {
    return codeTree.find(query)
  }

}

