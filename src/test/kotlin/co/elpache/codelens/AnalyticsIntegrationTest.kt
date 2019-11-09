package co.elpache.codelens;

import co.elpache.codelens.useCases.CodeExplorerUseCases
import co.elpache.codelens.useCases.EvolutionUseCases
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AnalyticsIntegrationTest {
  val factory = Factory("tmp", "tmp/kotlin/subpackage/")

  @Test
  fun `Can get Int Params`() {

    val uc = CodeExplorerUseCases(factory)

    val results = uc.getPossibleIntParams("fun")

    assertThat(results).contains("depth", "lines")
  }

  @Test
  fun `Can see change in a function`() {
    val uc = EvolutionUseCases(factory)
    val statistics = uc.collectHistory(
      "#ExampleClass #functionWithParams", "params",
      listOf("d37fb4b", "a1e3958")
    )

    assertThat(statistics[0].max).isEqualTo(2.0)
    assertThat(statistics[1].max).isEqualTo(3.0)
  }

  @Test
  fun `Can see change of methods in a class`() {
    val uc = EvolutionUseCases(factory)
    val statistics = uc.collectHistory(
      "#ExampleClass","methods",
      listOf("e3b714c", "e323c18"))

    assertThat(statistics[0].max).isEqualTo(1.0)
    assertThat(statistics[1].max).isEqualTo(2.0)
  }

  @Test //(timeout = 9000)
  fun `Performance- Collecting history of 6 commits shouldn't take more than 15 seconds`() {
    val uc = EvolutionUseCases(Factory("tmp", "tmp"))

    //println(uc.codeBase.asString())

    val commits = factory.repo.init().logs().map { it.id }.takeLast(6)

    uc.collectHistory(
      "#ExampleClass #functionWithParams", "params",
      commits
    )
  }

}
