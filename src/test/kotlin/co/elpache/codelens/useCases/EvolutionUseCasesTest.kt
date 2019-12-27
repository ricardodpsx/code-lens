package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.app.CodeLensApp
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit4.SpringRunner
import kotlin.system.measureTimeMillis


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [CodeLensApp::class])
class EvolutionUseCasesTest {

  @Autowired
  lateinit var context: ApplicationContext

  /**
   *
   * Todo: This needs a discussion, because if the base code is too big, saving the ast's of the whole projects may be too much
   * Althought this depends on the uses cases, right now I can think on two:
   * 1- You want to see how your next commit is doing with respect to the trends or thresholds: In this case you just need
   *  to look back one commit or to compare against master for Branch based development
   *
   * 2- You want to see the evolution of metrics over a long period: In this case probably one or two commits per day will do.
   *
   * In Any case an alternative to save the whole code asts would be to save only the metrics for a given (Query, commit)
   * but then evolutive search can not be so interactive
   */

  @Test
  fun `Preload commits in DB so that queries can be done faster later`() {
    val factory = Factory(context = context)
    val uc = EvolutionUseCases(factory)

    val commits = factory.repo.lastCommits(6)

    val preloadTime = measureTimeMillis {
      factory.preloadCommits(commits)
    }

    val loadTime = measureTimeMillis {
      assertThat(commits.size).isEqualTo(6)
      uc.collectHistory("fun", "lines", commits)
    }

    println(
      "Preload Time: ${preloadTime / 1000.0}\n" +
          "Load time ${loadTime / 1000.0} Secs"
    )

    assertThat(loadTime).isLessThan(4000)
  }
}