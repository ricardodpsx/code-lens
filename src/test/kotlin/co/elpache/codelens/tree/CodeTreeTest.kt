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

    ct.addVertice(verticeOf("a", "value" to "Giovanny"))
    ct.addVertice(verticeOf("b", "value" to "Candela"))

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
  fun `Can create subtree from children`() {

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

    tree.addRelation("friend", "b_3", "a")

    val res = tree.treeFromChildren(listOf("b_1", "b_3", "e"))
    assertThat(res.edgeOf("b_3", "a", "friend")).isNotNull
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

  @Test
  fun `No double edges with same relation`() {
    val tree = tree(
      "a", tree("b"), tree("c")
    )

    tree.addRelation("x", "b", "c")
    tree.edges["b"]!!.forEach { it.data["x"] = 1 }

    tree.addRelation("x", "b", "c")

    assertThat(tree.adj("b", "x")).containsExactly("c")
  }

  @Test
  fun `Accept only one parent`() {
    val tree = tree(
      "a", tree("b"), tree("c")
    )

    tree.addChild("c", "b")


    assertThat(tree.inorder()).containsExactly("a", "b", "c")
  }

  @Test
  fun `Can collapse nodes`() {
    val tree = tree(
      "a",
      tree(
        "b",
        tree("b_1"),
        tree("b_2")
      ),
      tree("c"),
      tree("d"),
      tree("e")
    )

    tree.collapse("b")

    assertThat(tree.inorder()).isEqualTo(listOf("a", "b", "c", "d", "e"))
  }

  @Test
  fun `Can add transitive relationships in the  parent based on the children`() {
    val tree = tree(
      "a",
      tree(
        "b",
        tree("b_1"),
        tree("b_2")
      ),
      tree("c"),
      tree("d"),
      tree("e")
    )
    tree.addRelation("rel", "b_1", "c")
    tree.addRelation("rel", "b_2", "c")
    tree.addRelation("backwards", "e", "b_2")

    tree.addTransitiveRelationships("b")

    assertThat(tree.relationData("b", "c", "rel")["relCount"]).isEqualTo(2)
    assertThat(tree.adj("e", "backwards")).contains("b")
  }


}