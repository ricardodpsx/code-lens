package co.elpache.codelens.useCases

import co.elpache.codelens.Factory

class EvolutionUseCases(private val factory: Factory = Factory()) {
  var code = factory.createBaseCode()

  fun collectHistory(query: String, param: String, commits: List<String>): List<DescriptiveStatistics> {
    //Todo: make sure this happens in an own directory
    val history = ArrayList<DescriptiveStatistics>()
    commits.forEach {
      code = factory.createBaseCode(it)
      history.add(statistics(code.selectBy(query).paramsValues(param)))
    }
    code = factory.createBaseCode()
    return history
  }

  fun collectFakeHistory(query: String, param: String, maxCommits: Int): Map<String, DescriptiveStatistics> {
    val commits = factory.repo.lastCommits(maxCommits)
    val history = HashMap<String, DescriptiveStatistics>()
    /*commits.forEach {
      codeBase = factory.createBaseCode(it)
      history[it] = statistics(paramValues(param, selectBy(query), codeBase), codeBase)
    }*/
    val commitOneStats =
      DescriptiveStatistics(1.0, 1.0, 1.0, 1.0, 1.0, listOf(1.0, 1.0, 1.0))
    val commitTwoStats =
      DescriptiveStatistics(3.0, 3.0, 3.0, 3.0, 3.0, listOf(3.0, 3.0, 3.0))
    val totalStats = DescriptiveStatistics(2.0, 2.0, 2.0, 1.0, 3.0, listOf(2.0, 2.0, 2.0))

    return mapOf("commit1" to commitOneStats, "commit2" to commitTwoStats, "Avg" to totalStats)
  }

  fun historyOfLastCommits(query: String, params: String, n: Int = 20) =
    collectHistory(query, params, factory.repo.init().logs().map { it.id }.take(n).reversed())

}