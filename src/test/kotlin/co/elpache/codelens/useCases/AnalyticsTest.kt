package co.elpache.codelens.useCases;

import co.elpache.codelens.Factory
import co.elpache.codelens.codeTree
import co.elpache.codelens.codeTreeNode
import co.elpache.codelens.createUseCases
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.Test

class AnalyticsTest {
  @Test
  fun `Can get Int Params`() {
    val uc = createUseCases(
      codeTree(
        "1",
        codeTreeNode("type" to "file"),
        codeTree(
          "2",
          codeTreeNode("type" to "files"),
          codeTree(
            "3",
            codeTreeNode("type" to "fun", "lines" to 6, "depth" to 5)
          ),
          codeTree("4", codeTreeNode("type" to "fun", "lines" to 6)),
          codeTree(
            "5",
            codeTreeNode("type" to "fun", "lines" to 4, "complexity" to 9)
          )
        ),
        codeTree("6", codeTreeNode("type" to "fun", "lines" to 4)),
        codeTree("7", codeTreeNode("type" to "fun"))
      )
    )

    val results = uc.getPossibleIntParams("fun")

    assertThat(results).containsExactlyInAnyOrder("depth", "complexity", "lines")
  }


  @Test
  fun `Can get Frequencies`() {
    val uc = createUseCases(
      codeTree(
        "1",
        codeTreeNode("type" to "file"),
        codeTree(
          "2",
          codeTreeNode("type" to "files"),
          codeTree("3", codeTreeNode("type" to "fun", "lines" to 6)),
          codeTree("4", codeTreeNode("type" to "fun", "lines" to 6)),
          codeTree("5", codeTreeNode("type" to "fun", "lines" to 4))
        ),
        codeTree("6", codeTreeNode("type" to "fun", "lines" to 4)),
        codeTree("7", codeTreeNode("type" to "fun", "lines" to 6))
      )
    )

    val results = uc.getFrequencyByParam("fun", "lines")

    assertThat(results.rows).containsExactly(
      listOf(4, 2), listOf(6, 3)
    )
  }

  @Test
  fun `Can get statistics`() {
    val uc = createUseCases(
      codeTree(
        "1",
        codeTreeNode("type" to "file"),
        codeTree(
          "2",
          codeTreeNode("type" to "files"),
          codeTree("3", codeTreeNode("type" to "fun", "lines" to 1)),
          codeTree("4", codeTreeNode("type" to "fun", "lines" to 3)),
          codeTree("5", codeTreeNode("type" to "fun", "lines" to 5))
        ),
        codeTree("6", codeTreeNode("type" to "fun", "lines" to 2)),
        codeTree("7", codeTreeNode("type" to "fun", "lines" to 4)),
        codeTree("8", codeTreeNode("type" to "fun", "lines" to 10))
      )
    )

    val statistics = uc.getStatistics("fun", "lines")

    assertThat(statistics.median).isCloseTo(3.5, Offset.offset(0.01))
    assertThat(statistics.max).isCloseTo(10.0, Offset.offset(0.01))
    assertThat(statistics.min).isCloseTo(1.0, Offset.offset(0.01))
    assertThat(statistics.mean).isCloseTo(4.16, Offset.offset(0.01))

  }


  @Test
  fun `can see evolution of median and average of a metric`() {

    val comm1 =
      codeTree(
        "1",
        codeTreeNode("type" to "file"),
        codeTree(
          "2",
          codeTreeNode("type" to "files"),
          codeTree("3", codeTreeNode("type" to "fun", "lines" to 6)),
          codeTree("4", codeTreeNode("type" to "fun", "lines" to 10))
        )
      )

    val comm2 =
      codeTree(
        "1",
        codeTreeNode("type" to "file"),
        codeTree(
          "2",
          codeTreeNode("type" to "files"),
          codeTree("3", codeTreeNode("type" to "fun", "lines" to 7)),
          codeTree("4", codeTreeNode("type" to "fun", "lines" to 7))
        )
      )

    val current =
      codeTree(
        "1",
        codeTreeNode("type" to "file"),
        codeTree(
          "2",
          codeTreeNode("type" to "files"),
          codeTree("3", codeTreeNode("type" to "fun", "lines" to 3)),
          codeTree("4", codeTreeNode("type" to "fun", "lines" to 4))
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
      factory.createBaseCode()
    } returns current


    val uc = EvolutionUseCases(factory)
    val results = uc.collectHistory("fun", "lines", listOf("commit1", "commit2"))

    assertThat(
      results
    ).extracting("max").containsExactly(10.0, 7.0)

  }


}
