package co.elpache.codelens

import co.elpache.codelens.tree.vDataOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class ConsoleAppTest {

  @Test
  fun `can load a list of functions`() {
    val uc = createCodeExplorerUseCases(
      codeTree(
        "1",
        vDataOf("type" to "file"),
        codeTree(
          "2",
          vDataOf("type" to "fun"),
          codeTree(
            "3", vDataOf("type" to "fun"),
            codeTree("4", vDataOf("type" to "fun"))
          ),
          codeTree("5", vDataOf("type" to "fun")),
          codeTree("6", vDataOf("type" to "X"))
        ),
        codeTree(
          "7", vDataOf("type" to "fun"),
          codeTree("8", vDataOf("type" to "X", "value" to 1))
        )
      )
    )

    assertThat(uc.search("X").map { it.toMap() })
      .contains(
        mapOf("vid" to "6", "type" to "X"),
        mapOf("vid" to "8", "type" to "X", "value" to 1)
      )

  }

}