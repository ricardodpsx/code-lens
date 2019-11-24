package co.elpache.codelens.tree

import co.elpache.codelens.inorder
import co.elpache.codelens.tree
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CodeTreeTest {

  @Test
  fun `Test that tree children are built correctly`() {

    val ct = tree(
      "a",
      tree("a.1"),
      tree("a.2")
    )

    println(ct.asString())
    assertThat(ct.children("a").map { ct.v(it)["value"] }).contains("a.1", "a.2")
  }


  @Test
  fun testCodeTreeAsGraph() {
    val ct = CodeTree()

    ct.addIfAbsent("a", vDataOf("value" to "Giovanny"))
    ct.addIfAbsent("b", vDataOf("value" to "Candela"))

    ct.addChild("a", "b")

  }

  @Test
  fun `can get parent`() {
    val tree = tree("a", tree("b"))
    assertThat(tree.parent("b")).isEqualTo("a")
  }

  @Test
  fun `can create subtrees`() {
    val tree = tree(
      "a",
      tree(
        "b",
        tree("b_1"),
        tree(
          "b_2",
          tree("c_3")
        )
      )
    )

    val res = tree.subTree("b")

    assertThat(inorder(res)).isEqualTo(listOf("b", "b_1", "b_2", "c_3"))
  }

  @Test
  fun `Can search code inside subfolders with parents`() {

    val tree = tree(
      "a",
      tree(
        "b",
        tree("b_1"),
        tree("b_2")
      ),
      tree(
        "c",
        tree("d")
      ),
      tree(
        "e",
        tree(
          "f",
          tree("b_3")
        )
      )
    )


    val res = tree.treeFromChildren(listOf("b_1", "b_3"))
    assertThat(inorder(res)).isEqualTo(listOf("a", "b", "b_1", "e", "f", "b_3"))
  }


}