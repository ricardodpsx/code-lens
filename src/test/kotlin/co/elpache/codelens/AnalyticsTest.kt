package co.elpache.codelens;

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AnalyticsTest {


  @Test
  fun `Can get Int Params`() {
    val uc = UseCases(
      codeTree(
        "1",
        codeTreeNode("type" to "file"),
        codeTree(
          "2",
          codeTreeNode("type" to "files"),
          codeTree("3", codeTreeNode("type" to "fun", "lines" to 6, "depth" to 5)),
          codeTree("4", codeTreeNode("type" to "fun", "lines" to 6)),
          codeTree("5", codeTreeNode("type" to "fun", "lines" to 4, "complexity" to 9))
        ),
        codeTree("6", codeTreeNode("type" to "fun", "lines" to 4)),
        codeTree("7", codeTreeNode("type" to "fun"))
      )
    )
    val results = uc.getPossibleIntParams("fun")

    assertThat(results).containsExactlyInAnyOrder("depth", "complexity", "lines")
  }


  @Test
  fun `Can get Chart view`() {
    val uc = UseCases(
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



}
