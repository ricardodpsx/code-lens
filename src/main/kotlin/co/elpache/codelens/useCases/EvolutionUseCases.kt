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

  val repo by lazy { factory.repo }

  data class ParamEvolutionRow(
    val commit: Commit,
    val statistics: DescriptiveStatistics
  )

  fun collectHistory(query: String, param: String, days: Int) =
    collectHistory(query, param, repo.perDaySampling(days))

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

  fun preloadCommits(maxCommits: Int) {
    factory.preloadCommits(repo.perDaySampling(maxCommits))
  }


  fun collectFrequency(query: String, maxCommits: Int): List<EvolutionOfFrequency> =
    collectFrequency(query, repo.perDaySampling(maxCommits))

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