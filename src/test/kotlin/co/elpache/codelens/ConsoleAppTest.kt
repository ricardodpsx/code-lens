package co.elpache.codelens

import co.elpache.codelens.tree.verticeOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class ConsoleAppTest {

  @Test
  fun `can load a list of functions`() {
    val uc = createCodeExplorerUseCases(
      codeTree(
        verticeOf("1", "type" to "file"),
        codeTree(
          verticeOf("2", "type" to "fun"),
          codeTree(
            verticeOf("3", "type" to "fun"),
            codeTree(verticeOf("4", "type" to "fun"))
          ),
          codeTree(verticeOf("5", "type" to "fun")),
          codeTree(verticeOf("6", "type" to "X"))
        ),
        codeTree(
          verticeOf("7", "type" to "fun"),
          codeTree(verticeOf("8", "type" to "X", "value" to 1))
        )
      )
    )

    assertThat(uc.search("X").map { it.data.toMap() })
      .contains(
        mapOf("vid" to "6", "type" to "X"),
        mapOf("vid" to "8", "type" to "X", "value" to 1)
      )

  }

}