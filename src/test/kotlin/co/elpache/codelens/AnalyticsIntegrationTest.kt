package co.elpache.codelens;

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AnalyticsIntegrationTest {
  @Test
  fun `Can get Int Params`() {
    val uc = UseCases()

    val results = uc.getPossibleIntParams("fun")

    assertThat(results).contains("depth", "lines")
  }

  @Test
  fun `Can see change in a function`() {
    val uc = UseCases()
    val statistics = uc.collectHistory(
      "#functionWithParams", "params",
      listOf("d37fb4b", "a1e3958"))

    assertThat(statistics[0].max).isEqualTo(2.0)
    assertThat(statistics[1].max).isEqualTo(3.0)
  }

}
