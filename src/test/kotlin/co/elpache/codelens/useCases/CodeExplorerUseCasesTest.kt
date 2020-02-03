package co.elpache.codelens.useCases

import co.elpache.codelens.codeTree
import co.elpache.codelens.compareTreeOutputs
import co.elpache.codelens.createCodeExplorerUseCases
import co.elpache.codelens.tree.vDataOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class CodeExplorerUseCasesTest {

  @Test
  fun `Can Select items by type (Regression)`() {
    val uc = createCodeExplorerUseCases(
      codeTree(
        vDataOf("1","type" to "file"),
        codeTree(
          vDataOf("2","type" to "fun"),
          codeTree(
            vDataOf("3","type" to "fun"),
            codeTree(vDataOf("4","type" to "fun"))
          ),
          codeTree(vDataOf("5","type" to "fun")),
          codeTree(vDataOf("6","type" to "X"))
        ),
        codeTree(
          vDataOf("7","type" to "fun"),
          codeTree(vDataOf("8","type" to "X"))
        )
      )
    )

    val (treeWithDescendants, results) = uc.selectCodeWithParents("X")

    assertThat(results).containsExactlyInAnyOrder("8", "6")

    compareTreeOutputs(
      treeWithDescendants,
      """
        {type=file, code=<Excluded>}
         - {type=fun}
         -- {type=X}
         - {type=fun}
         -- {type=X}"""
    )

  }

}