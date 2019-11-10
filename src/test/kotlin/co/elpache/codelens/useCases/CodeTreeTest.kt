package co.elpache.codelens.useCases

import co.elpache.codelens.codeTree
import co.elpache.codelens.codeTreeNode
import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.compareTreeOutputs
import co.elpache.codelens.createUseCases
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test


class CodeTreeTest {
  @Test
  @Ignore
  fun testTreeExpansion() {
    CodeTree().expandFullCodeTree(codeTreeNode())
  }

  @Test
  fun `Can Select items by type (Regression)`() {
    val uc = createUseCases(
      codeTree(
        "1",
        codeTreeNode("type" to "file"),
        codeTree(
          "2",
          codeTreeNode("type" to "fun"),
          codeTree(
            "3", codeTreeNode("type" to "fun"),
            codeTree("4", codeTreeNode("type" to "fun"))
          ),
          codeTree("5", codeTreeNode("type" to "fun")),
          codeTree("6", codeTreeNode("type" to "X"))
        ),
        codeTree(
          "7", codeTreeNode("type" to "fun"),
          codeTree("8", codeTreeNode("type" to "X"))
        )
      )
    )

    val (treeWithDescendants, results) = uc.selectCodeWithParents("X")

    assertThat(results).containsExactlyInAnyOrder("8", "6")

    compareTreeOutputs(
      treeWithDescendants,
      """
      1: {name=, type=file}
      - 2: {name=, type=fun}
      -- 6: {name=, type=X}
      - 7: {name=, type=fun}
      -- 8: {name=, type=X}"""
    )

  }

}