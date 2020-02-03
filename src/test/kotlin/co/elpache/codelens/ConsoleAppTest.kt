package co.elpache.codelens

import co.elpache.codelens.tree.vDataOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class ConsoleAppTest {

  @Test
  fun `can load a list of functions`() {
    val uc = createCodeExplorerUseCases(
      codeTree(
        vDataOf("1", "type" to "file"),
        codeTree(
          vDataOf("2", "type" to "fun"),
          codeTree(
            vDataOf("3", "type" to "fun"),
            codeTree(vDataOf("4", "type" to "fun"))
          ),
          codeTree(vDataOf("5", "type" to "fun")),
          codeTree(vDataOf("6", "type" to "X"))
        ),
        codeTree(
          vDataOf("7", "type" to "fun"),
          codeTree(vDataOf("8", "type" to "X", "value" to 1))
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