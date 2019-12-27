package co.elpache.codelens.useCases;

import co.elpache.codelens.Factory
import co.elpache.codelens.codeTree
import co.elpache.codelens.createCodeExplorerUseCases
import co.elpache.codelens.createCommits
import co.elpache.codelens.tree.vDataOf
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.Test

class AnalyticsTest {
  @Test
  fun `Can get Int Params`() {
    val uc = createCodeExplorerUseCases(
      codeTree(
        "1",
        vDataOf("type" to "file"),
        codeTree(
          "2",
          vDataOf("type" to "files"),
          codeTree(
            "3",
            vDataOf("type" to "fun", "lines" to 6, "depth" to 5)
          ),
          codeTree("4", vDataOf("type" to "fun", "lines" to 6)),
          codeTree(
            "5",
            vDataOf("type" to "fun", "lines" to 4, "complexity" to 9)
          )
        ),
        codeTree("6", vDataOf("type" to "fun", "lines" to 4)),
        codeTree("7", vDataOf("type" to "fun"))
      )
    )

    val results = uc.getPossibleIntParams("fun")

    assertThat(results).containsExactlyInAnyOrder("depth", "complexity", "lines")
  }


  @Test
  fun `Can get Frequencies`() {
    val uc = createCodeExplorerUseCases(
      codeTree(
        "1",
        vDataOf("type" to "file"),
        codeTree(
          "2",
          vDataOf("type" to "files"),
          codeTree("3", vDataOf("type" to "fun", "lines" to 6)),
          codeTree("4", vDataOf("type" to "fun", "lines" to 6)),
          codeTree("5", vDataOf("type" to "fun", "lines" to 4))
        ),
        codeTree("6", vDataOf("type" to "fun", "lines" to 4)),
        codeTree("7", vDataOf("type" to "fun", "lines" to 6))
      )
    )

    val results = uc.getParamDistribution("fun", "lines")

    assertThat(results.rows).containsExactly(
      ParamFrequencyRow(4, 2, listOf("5", "6")),
      ParamFrequencyRow(6, 3, listOf("3", "4", "7"))
    )
  }

  @Test
  fun `Can get evolution of a given metric`() {
    val uc = createCodeExplorerUseCases(
      codeTree(
        "1",
        vDataOf("type" to "file"),
        codeTree(
          "2",
          vDataOf("type" to "files"),
          codeTree("3", vDataOf("type" to "fun", "lines" to 1)),
          codeTree("4", vDataOf("type" to "fun", "lines" to 3)),
          codeTree("5", vDataOf("type" to "fun", "lines" to 5))
        ),
        codeTree("6", vDataOf("type" to "fun", "lines" to 2)),
        codeTree("7", vDataOf("type" to "fun", "lines" to 4)),
        codeTree("8", vDataOf("type" to "fun", "lines" to 10))
      )
    )

    val statistics = uc.getMetricEvolution("fun", "lines")

    assertThat(statistics.median).isCloseTo(3.5, Offset.offset(0.01))
    assertThat(statistics.max).isCloseTo(10.0, Offset.offset(0.01))
    assertThat(statistics.min).isCloseTo(1.0, Offset.offset(0.01))
    assertThat(statistics.mean).isCloseTo(4.16, Offset.offset(0.01))

  }

  @Test
  fun `can see evolution of frequency`() {

    //I.E Have the functions with more than certain lines size increased?

    val comm1 =
      codeTree(
        "1",
        vDataOf("type" to "file"),
        codeTree(
          "2",
          vDataOf("type" to "files"),
          codeTree("3", vDataOf("type" to "fun", "lines" to 6)),
          codeTree("4", vDataOf("type" to "fun", "lines" to 3))
        )
      )

    val comm2 =
      codeTree(
        "1",
        vDataOf("type" to "file"),
        codeTree(
          "2",
          vDataOf("type" to "files"),
          codeTree("3", vDataOf("type" to "fun", "lines" to 7)),
          codeTree("4", vDataOf("type" to "fun", "lines" to 8)),
          codeTree("5", vDataOf("type" to "fun", "lines" to 9))
        )
      )

    val current =
      codeTree(
        "1",
        vDataOf("type" to "file"),
        codeTree(
          "2",
          vDataOf("type" to "files"),
          codeTree("3", vDataOf("type" to "fun", "lines" to 6)),
          codeTree("4", vDataOf("type" to "fun", "lines" to 4)),
          codeTree("5", vDataOf("type" to "fun", "lines" to 9))
        )
      )
    val factory = mockk<Factory>()

    every {
      factory.createBaseCode("commit1")
    } returns comm1

    every {
      factory.createBaseCode("commit2")
    } returns comm2

    every {
      factory.createBaseCode("commit3")
    } returns current


    val uc = EvolutionUseCases(factory)
    val results = uc.collectFrequency(
      "fun[lines>=6]",
      createCommits("commit1", "commit2", "commit3")
    )

    assertThat(results.map { it.commit.id to it.frequency }).containsExactly(
      "commit1" to 1,
      "commit2" to 3,
      "commit3" to 2
    )

  }
}
