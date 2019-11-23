package co.elpache.codelens.useCases

import co.elpache.codelens.codeTree
import co.elpache.codelens.compareTreeOutputs
import co.elpache.codelens.tree.vDataOf
import co.elpache.codelens.createCodeExplorerUseCases
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class CodeLoaderTest {

  @Test
  fun `Can Select items by type (Regression)`() {
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
          codeTree("8", vDataOf("type" to "X"))
        )
      )
    )

    val (treeWithDescendants, results) = uc.selectCodeWithParents("X")

    assertThat(results).containsExactlyInAnyOrder("8", "6")

    compareTreeOutputs(
      treeWithDescendants,
      """
      1: {type=file}
      - 2: {type=fun}
      -- 6: {type=X}
      - 7: {type=fun}
      -- 8: {type=X}"""
    )

  }

}