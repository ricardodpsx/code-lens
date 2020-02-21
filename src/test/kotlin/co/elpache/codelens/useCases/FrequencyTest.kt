package co.elpache.codelens.useCases;

import co.elpache.codelens.Factory
import co.elpache.codelens.codeTree
import co.elpache.codelens.createCodeExplorerUseCases
import co.elpache.codelens.createCommit
import co.elpache.codelens.createCommits
import co.elpache.codelens.tree.verticeOf
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.Test

class FrequencyTest {
  @Test
  fun `Can get Int Params`() {
    val uc = createCodeExplorerUseCases(
      codeTree(

        verticeOf("1", "type" to "file"),
        codeTree(

          verticeOf("2", "type" to "files"),
          codeTree(

            verticeOf("3", "type" to "fun", "lines" to 6, "depth" to 5)
          ),
          codeTree(verticeOf("4", "type" to "fun", "lines" to 6)),
          codeTree(

            verticeOf("5", "type" to "fun", "lines" to 4, "complexity" to 9)
          )
        ),
        codeTree(verticeOf("6", "type" to "fun", "lines" to 4)),
        codeTree(verticeOf("7", "type" to "fun"))
      )
    )

    val results = uc.getPossibleIntParams("fun")

    assertThat(results).containsExactlyInAnyOrder("depth", "complexity", "lines")
  }


  @Test
  fun `Can get Frequencies`() {
    val uc = createCodeExplorerUseCases(
      codeTree(

        verticeOf("1", "type" to "file"),
        codeTree(

          verticeOf("2", "type" to "files"),
          codeTree(verticeOf("3", "type" to "fun", "lines" to 6)),
          codeTree(verticeOf("4", "type" to "fun", "lines" to 6)),
          codeTree(verticeOf("5", "type" to "fun", "lines" to 4))
        ),
        codeTree(verticeOf("6", "type" to "fun", "lines" to 4)),
        codeTree(verticeOf("7", "type" to "fun", "lines" to 6))
      )
    )

    val results = uc.getParamDistribution("fun", "lines")

    assertThat(results.rows).extracting("paramValue").containsExactly(4.0, 6.0)
    assertThat(results.rows.find { it.paramValue == 4.0 }!!.nodes).extracting("vid").contains("5", "6")
    assertThat(results.rows.find { it.paramValue == 6.0 }!!.nodes).extracting("vid").contains("3", "4", "7")

//      ParamFrequencyRow(4.0, 2, listOf("5", "6")),
//      ParamFrequencyRow(6.0, 3, listOf("3", "4", "7"))
//    )
  }

  @Test
  fun `Can get evolution of a given metric`() {
    val uc = createCodeExplorerUseCases(
      codeTree(
        verticeOf("1", "type" to "file"),
        codeTree(

          verticeOf("2", "type" to "files"),
          codeTree(verticeOf("3", "type" to "fun", "lines" to 1)),
          codeTree(verticeOf("4", "type" to "fun", "lines" to 3)),
          codeTree(verticeOf("5", "type" to "fun", "lines" to 5))
        ),
        codeTree(verticeOf("6", "type" to "fun", "lines" to 2)),
        codeTree(verticeOf("7", "type" to "fun", "lines" to 4)),
        codeTree(verticeOf("8", "type" to "fun", "lines" to 10))
      )
    )

    val statistics = uc.getMetricStatistics("fun", "lines")

    assertThat(statistics.median).isCloseTo(3.5, Offset.offset(0.01))
    assertThat(statistics.max).isCloseTo(10.0, Offset.offset(0.01))
    assertThat(statistics.min).isCloseTo(1.0, Offset.offset(0.01))
    assertThat(statistics.mean).isCloseTo(4.16, Offset.offset(0.01))

  }

  @Test
  fun `can see evolution of frequency`() {

    //I.E Have the functions with more than certain lines size increased?

    //Todo: Can be more clear
    val comm1 =
      codeTree(

        verticeOf("1", "type" to "file"),
        codeTree(

          verticeOf("2", "type" to "files"),
          codeTree(verticeOf("3", "type" to "fun", "lines" to 6)),
          codeTree(verticeOf("4", "type" to "fun", "lines" to 3))
        )
      )

    val comm2 =
      codeTree(

        verticeOf("1", "type" to "file"),
        codeTree(

          verticeOf("2", "type" to "files"),
          codeTree(verticeOf("3", "type" to "fun", "lines" to 7)),
          codeTree(verticeOf("4", "type" to "fun", "lines" to 8)),
          codeTree(verticeOf("5", "type" to "fun", "lines" to 9))
        )
      )

    val current =
      codeTree(

        verticeOf("1", "type" to "file"),
        codeTree(
          verticeOf("2", "type" to "files"),
          codeTree(verticeOf("3", "type" to "fun", "lines" to 6)),
          codeTree(verticeOf("4", "type" to "fun", "lines" to 4)),
          codeTree(verticeOf("5", "type" to "fun", "lines" to 9))
        )
      )
    val factory = mockk<Factory>()

    every {
      factory.loadCodeFromCommits(any())
    } returns mapOf(
      createCommit("commit1") to comm1,
      createCommit("commit2") to comm2,
      createCommit("commit3") to current
    )

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
