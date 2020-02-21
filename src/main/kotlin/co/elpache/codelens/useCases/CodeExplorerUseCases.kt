package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.codeSearch.search.finder
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
  val codeTreee = factory.createBaseCode()

  fun find(query: String) = codeTreee.finder().find(query)

  fun selectCodeWithParents(query: Vid): SearchResults {
    //Todo: Refactor, make it handle parse exception specifically
    return try {
      val res = find(query).map { it.vertice }
      SearchResults(
        codeTreee.treeFromChildren(res),
        res
      )
    } catch (e: Exception) {
      SearchResults()
    }
  }

  fun getSearchResultsWithParams(query: String): SearchResultsWithParams {
    return with(selectCodeWithParents(query)) {
      SearchResultsWithParams(
        treeWithDescendants,
        results,
        getPossibleIntParams(query)
      )
    }
  }

  //Gets the distribution of parameters in nodes,
  //This make it easy to answer questions like "how many functions have more than 5 params" etc
  fun getParamDistribution(query: String, param: String) =
    AnalyticsResults(
      frequency(
        find(query).filter { it[param] != null }
          .map { it.vertice to it.vertice.getDouble(param) })
    )


  fun getMetricStatistics(query: String, param: String): DescriptiveStatistics {
    return statistics(find(query).filter { it[param] != null }
      .map { it.vertice.vid to it.vertice.getInt(param) })
  }

  fun loadNodeContents(vid: String) =
    NodeContentsResults(
      ContextNode(vid, codeTreee).code,
      codeTreee.subTree(vid)
    )

  fun getPossibleIntParams(query: String) =
    try {
      find(query)
        .map { it.vertice.params() }
        .flatten()
        .distinct()
    } catch (e: Exception) {
      arrayListOf<String>()
    }


  fun search(query: String): List<Vertice> {
    return codeTreee.finder().find(query)
      .map {
        it.vertice
      }
  }

}

