package co.elpache.codelens;

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AnalyticsIntegrationTest {
  val factory = Factory("tmp", "tmp/kotlin/subpackage/")

  @Test
  fun `Can get Int Params`() {

    val uc = UseCases(factory)

    val results = uc.getPossibleIntParams("fun")

    assertThat(results).contains("depth", "lines")
  }

  @Test
  fun `Can see change in a function`() {
    val uc = UseCases(factory)
    val statistics = uc.collectHistory(
      "#ExampleClass #functionWithParams", "params",
      listOf("d37fb4b", "a1e3958"))

    assertThat(statistics[0].max).isEqualTo(2.0)
    assertThat(statistics[1].max).isEqualTo(3.0)
  }

}
