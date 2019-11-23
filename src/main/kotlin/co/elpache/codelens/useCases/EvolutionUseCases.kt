package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.app.database.AstRecord
import co.elpachecode.codelens.cssSelector.search.finder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging

class EvolutionUseCases(private val factory: Factory = Factory()) {
  val mapper = ObjectMapper().registerModule(KotlinModule())

  private val logger = KotlinLogging.logger {}


  fun collectHistory(query: String, param: String, maxCommits: Int) =
    collectHistory(query, param, factory.repo.lastCommits(maxCommits))

  fun collectHistory(query: String, param: String, commits: List<String>): Map<String, DescriptiveStatistics> {

    logger.info { "Starting collecting history of $commits" }
    val history = LinkedHashMap<String, DescriptiveStatistics>()
    commits.forEach {
      val code = factory.createBaseCode(it)
      history[it] = statistics(code.finder().find(query).paramsValues(param))
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
    preloadCommits(factory.repo.lastCommits(maxCommits))
  }

  fun preloadCommits(commits: List<String>) {
    logger.info { "Preloading ${commits}" }
    commits
      .filter { factory.getAstDatabase().findByCommit(it) == null }
      .forEach {
        val code = factory.createBaseCode(it)
        factory.getAstDatabase().save(AstRecord(it, mapper.writeValueAsString(code)))
      }
  }

}