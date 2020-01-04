package co.elpache.codelens.useCases;

import co.elpache.codelens.Commit
import co.elpache.codelens.extensions.percentOfChanges
import co.elpache.codelens.extensions.today
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage
import org.junit.Test


class TemporalMetricsTest {

  @Test
  fun `can percent of change in last N days`() {
    val commits = listOf(
      Commit("012", "", commitTime = today(7)),
      Commit("123", "", commitTime = today(2)),
      Commit("123-2", "", commitTime = today(2)),
      Commit("234", "", commitTime = today(1)),
      Commit("345", "", commitTime = today()),
      Commit("345-2", "", commitTime = today()),
      Commit("345-3", "", commitTime = today())
    )

    assertThat(percentOfChanges(commits, 3)).isCloseTo(1.0, Percentage.withPercentage(1.0))

    //In a week
    assertThat(percentOfChanges(commits, 7)).isCloseTo(0.428, Percentage.withPercentage(1.0))

    assertThat(percentOfChanges(commits, 8)).isCloseTo(0.5, Percentage.withPercentage(1.0))
  }

}

