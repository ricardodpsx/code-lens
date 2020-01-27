package co.elpache.codelens.useCases

import co.elpache.codelens.Commit
import co.elpache.codelens.Factory
import co.elpache.codelens.codeSearch.search.finder
import co.elpache.codelens.codeSearch.search.paramsValues
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging

class EvolutionUseCases(val factory: Factory = Factory()) {
  val mapper = ObjectMapper().registerModule(KotlinModule())

  private val logger = KotlinLogging.logger {}


  data class ParamEvolutionRow(
    val commit: Commit,
    val statistics: DescriptiveStatistics
  )

  fun collectHistory(query: String, param: String, maxCommits: Int) =
    collectHistory(query, param, factory.repo.lastCommits(maxCommits))

  fun collectHistory(query: String, param: String, commits: List<Commit>): List<ParamEvolutionRow> {

    logger.info { "Starting collecting history of $commits" }
    val history = ArrayList<ParamEvolutionRow>()
    factory.loadCodeFromCommits(commits)
      .forEach {
        history.add(
          ParamEvolutionRow(
            it.key,
            statistics(
              it.value.finder().find(query).paramsValues(param).map {
                it.first to it.second.toInt()
              }
            )
          )
        )
      }
    logger.info { "Finished collecting history of $commits" }

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

  fun preloadCommits(maxCommits: Int) {
    factory.preloadCommits(factory.repo.lastCommits(maxCommits))
  }


  fun collectFrequency(query: String, maxCommits: Int): List<EvolutionOfFrequency> =
    collectFrequency(query, factory.repo.lastCommits(maxCommits))

  data class EvolutionOfFrequency(val commit: Commit, val frequency: Int)

  fun collectFrequency(query: String, commits: List<Commit>): List<EvolutionOfFrequency> {
    val frequencies = ArrayList<EvolutionOfFrequency>()
    logger.info { "Starting collecting history of $commits" }
    factory.loadCodeFromCommits(commits).forEach {
      frequencies.add(EvolutionOfFrequency(it.key, it.value.finder().find(query).size))
    }
    logger.info { "Finished collecting history of $commits" }
    return frequencies
  }

}