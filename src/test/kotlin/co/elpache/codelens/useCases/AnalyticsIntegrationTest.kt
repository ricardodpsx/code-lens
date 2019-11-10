package co.elpache.codelens.useCases;

import co.elpache.codelens.Factory
import co.elpache.codelens.app.CodeLensApp
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [CodeLensApp::class])
class AnalyticsIntegrationTest {

  @Autowired
  lateinit var applicationContext: ApplicationContext

  var factory: Factory = Factory()

  @Before
  fun setup() {
    factory = Factory(path = "tmp", currentCodePath = "../code-examples/", context = applicationContext)
  }


  @Test
  fun `Can get Int Params`() {

    val uc = CodeExplorerUseCases(factory!!)

    val results = uc.getPossibleIntParams("fun")

    assertThat(results).contains("depth", "lines")
  }

  @Test
  fun `Can see change in a function`() {
    val uc = EvolutionUseCases(factory!!)
    uc.preloadCommits(listOf("d37fb4b", "a1e3958"))

    uc.collectHistory(
      "#kotlin #ExampleClass #functionWithParams", "params",
      listOf("d37fb4b", "a1e3958")
    )

    uc.collectHistory(
      "#kotlin #ExampleClass #functionWithParams", "lines",
      listOf("d37fb4b", "a1e3958")
    )

    val statistics = uc.collectHistory(
      "#kotlin #ExampleClass #functionWithParams", "params",
      listOf("d37fb4b", "a1e3958")
    )

    assertThat(statistics[0].max).isEqualTo(2.0)
    assertThat(statistics[1].max).isEqualTo(3.0)
  }

  @Test
  fun `Can see change of methods in a class`() {
    val uc = EvolutionUseCases(factory!!)
    val statistics = uc.collectHistory(
      "#ExampleClass", "methods",
      listOf("e3b714c", "e323c18")
    )

    assertThat(statistics[0].max).isEqualTo(1.0)
    assertThat(statistics[1].max).isEqualTo(2.0)
  }

}
