package co.elpache.codelens.useCases

import co.elpache.codelens.codeTree
import co.elpache.codelens.compareTreeOutputs
import co.elpache.codelens.createCodeExplorerUseCases
import co.elpache.codelens.tree.verticeOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class CodeExplorerUseCasesTest {

  @Test
  fun `Can Select items by type (Regression)`() {
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
          codeTree(verticeOf("8", "type" to "X"))
        )
      )
    )

    val (treeWithDescendants, results) = uc.selectCodeWithParents("X")

    assertThat(results).extracting("vid").containsExactlyInAnyOrder("8", "6")

    compareTreeOutputs(
      treeWithDescendants!!,
      """
        {type=file, code=<Excluded>}
         - {type=fun}
         -- {type=X}
         - {type=fun}
         -- {type=X}"""
    )

  }

}