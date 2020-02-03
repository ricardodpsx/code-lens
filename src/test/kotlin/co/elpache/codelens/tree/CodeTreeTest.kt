package co.elpache.codelens.tree

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
  fun `Add Subtree`() {

    val a = tree(
      "a",
      tree("a.1"),
      tree("a.2")
    )

    val b = tree(
      "b",
      tree("b.1"),
      tree("b.2")
    )

    assertThat(a.addSubTree(b, "a.1").inorder()).contains("a", "a.1", "a.2", "b", "b.1", "b.2")
  }

  @Test(expected = Error::class)
  fun `Should not join non-disjoint trees`() {

    val a = tree(
      "a",
      tree("a.1"),
      tree("a.2")
    )

    val b = tree(
      "a",
      tree("b.1"),
      tree("b.2")
    )

    a.addSubTree(b, "a.1")
  }


  @Test
  fun testCodeTreeAsGraph() {
    val ct = CodeTree()

    ct.addIfAbsent(vDataOf("a", "value" to "Giovanny"))
    ct.addIfAbsent(vDataOf("b", "value" to "Candela"))

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

    assertThat(res.inorder()).isEqualTo(listOf("b", "b_1", "b_2", "c_3"))
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
    assertThat(res.inorder()).isEqualTo(listOf("a", "b", "b_1", "e", "f", "b_3"))
  }

  @Test
  fun `Can add relations to a tree`() {
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

    tree.addRelation("friends", "c", "e")

    assertThat(tree.adj("c", "friends")).contains("e")
    assertThat(tree.adj("c")).contains("d", "e")
  }


}