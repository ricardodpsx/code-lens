package co.elpache.codelens

import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.CssSearch
import co.elpachecode.codelens.cssSelector.parseCssSelector
import org.nield.kotlinstatistics.countBy
import org.nield.kotlinstatistics.descriptiveStatistics
import org.nield.kotlinstatistics.median

data class SearchResults(
  val treeWithDescendants: CodeTree,
  val results: List<Vid> = listOf()
)

data class NodeContentsResults(
  val text: String,
  val ast: Map<String, Any>
)

data class AnalyticsResults(val rows: List<List<Int>>)

interface Analytic

val frequency = { values: List<Pair<Vid, Int>>, _: CodeTree ->
  values
    .countBy { it.second }
    .map { listOf(it.key, it.value) }
    .sortedBy { it[0] }
}

data class DescriptiveStatistics(
  val mean: Double,
  val median: Double,
  val std: Double,
  val min: Double,
  val max: Double,
  val quartiles: List<Double>
)

val statistics = { values: List<Pair<Vid, Int>>, _: CodeTree ->
  with(values.map { it.second }) {
    val ds = descriptiveStatistics
    DescriptiveStatistics(
      mean = ds.mean,
      median = median(),
      std = ds.standardDeviation,
      min = ds.min,
      max = ds.max,
      quartiles = listOf(
        ds.percentile(25.0),
        ds.percentile(50.0),
        ds.percentile(75.0)
      )
    )
  }
}

class UseCases(private val factory: Factory = Factory()) {

  private var codeBase = factory.createBaseCode()

  //Todo: Optimize with memoization
  private fun selectBy(
    query: String
  ) = CssSearch(parseCssSelector(query), codeBase).search()

  //Todo: Separate into two use cases
  fun selectCodeWithParents(query: Vid): SearchResults {
    //Todo: Refactor, make it handle parse exception specifically
    return try {
      val res = selectBy(query)
      SearchResults(
        codeBase.treeFromChildren(res),
        res
      )
    } catch (e: Exception) {
      SearchResults(codeBase)
    }
  }

  //Todo: Move to CodeBase itself or some extension, this is error prone
  private fun paramValues(param: String, vids: List<Vid>, code: CodeTree = codeBase) =
    vids.filter { code.node(it).contains(param) }
      .map { Pair(it, code.node(it).value<Int>(param)) }

  fun getFrequencyByParam(query: Vid, param: String) =
    AnalyticsResults(
      frequency(paramValues(param, selectBy(query)), codeBase)
    )


  fun getStatistics(query: String, param: String): DescriptiveStatistics {
    return statistics(paramValues(param, selectBy(query), codeBase), codeBase)
  }

  fun loadNodeContents(vid: String) =
    NodeContentsResults(
      codeBase.file(vid).contents(),
      codeBase.toMap().plus("rootVid" to vid)
    )

  fun getPossibleIntParams(query: String) =
    try {
      selectBy(query)
        .map { codeBase.data(it).keys }
        .flatten()
        .filter { !listOf("name", "type").contains(it) }
        .distinct()
    } catch (e: Exception) {
      arrayListOf<String>()
    }

  fun collectHistory(query: String, param: String, commits: List<String>): List<DescriptiveStatistics> {
    //Todo: make sure this happens in an own directory
    val history = ArrayList<DescriptiveStatistics>()
    commits.forEach {
      codeBase = factory.createBaseCode(it)
      history.add(statistics(paramValues(param, selectBy(query), codeBase), codeBase))
    }

    codeBase = factory.createBaseCode()
    history.add(statistics(paramValues(param, selectBy(query), codeBase), codeBase))
    return history
  }

}