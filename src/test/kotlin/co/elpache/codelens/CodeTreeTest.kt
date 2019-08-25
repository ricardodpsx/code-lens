package co.elpache.codelens

import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test


class CodeTreeTest {
  @Test
  @Ignore
  fun testTreeExpansion() {
    expandFullCodeTree(codeTreeNode())
  }

  @Test
  fun `Can Select items by type`() {
    val uc = UseCases(
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
       {type=file}
         - {type=fun}
         -- {type=X}
         - {type=fun}
         -- {type=X}"""
    )

  }

}