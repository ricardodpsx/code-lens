package co.elpache.codelens.codeSearch

import co.elpache.codelens.codeSearch.search.finder
import co.elpache.codelens.codeSearch.search.vids
import co.elpache.codelens.codeTree
import co.elpache.codelens.tree.vDataOf
import co.elpachecode.codelens.cssSelector.SelectorFunction
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CssSearchTest {
  val tree =
    codeTree(
      "1", vDataOf("type" to "a AA", "name" to "parent"),
      codeTree(
        "1.1", vDataOf("type" to "b"),
        codeTree(
          "1.1.1", vDataOf("type" to "c", "lines" to 3),
          codeTree("1.1.1.1", vDataOf("type" to "e"))
        ),
        codeTree(
          "1.1.2",
          vDataOf("type" to "d", "lines" to 4),
          codeTree("1.1.2.1", vDataOf("type" to "d"))
        ),
        codeTree("1.1.3", vDataOf("type" to "b"))
      ),
      codeTree("1.2", vDataOf("type" to "b")),
      codeTree(
        "1.3",
        vDataOf("type" to "a"),
        codeTree("1.3.1", vDataOf("type" to "d"))
      )
    )

  val treeWithFunctions =
    codeTree(
      "1", vDataOf("type" to "file", "name" to "code.kt"),
      codeTree(
        "1.1", vDataOf("type" to "fun"),
        codeTree(
          "1.1.1", vDataOf("type" to "param"),
          codeTree(
            "1.1.1.1", vDataOf("type" to "int")
          )
        ),
        codeTree(
          "1.1.2", vDataOf("type" to "param")
        )
      ),
      codeTree(
        "2.1", vDataOf("type" to "fun"),
        codeTree(
          "2.1.1", vDataOf("type" to "param")
        )
      )
    )

  private fun search(query: String) = tree.finder().find(query).map { it.vid }

  @Test
  fun `Test Single child`() {
    assertThat(search("a")).containsExactlyInAnyOrder("1", "1.3")
  }


  @Test
  fun `Test children search`() {
    println(tree.asString())
    assertThat(search("a b")).containsExactlyInAnyOrder("1.1", "1.2", "1.1.3")

    assertThat(search("a[name='parent'] d d")).containsExactlyInAnyOrder("1.1.2.1")
  }

  @Test
  fun `support multiple types`() {
    assertThat(search("AA b")).containsExactlyInAnyOrder("1.1", "1.2", "1.1.3")
  }

  @Test
  fun `Test attribute search`() {
    println(tree.asString())
    assertThat(search("a[name='parent']")).containsExactlyInAnyOrder("1")
  }

  @Test
  fun `Aggregator search`() {
    assertThat(tree.finder().findValue("a b | count()")).isEqualTo(3)
  }

  @Test
  fun `Can set metrics into nodes with as`() {
    val res = treeWithFunctions.finder().find("fun[{param|count()} as paramCount]")

    assertThat(res.vids()).containsExactly("1.1", "2.1")

    assertThat(treeWithFunctions.v("1.1").getInt("paramCount")).isEqualTo(2)
    assertThat(treeWithFunctions.v("2.1").getInt("paramCount")).isEqualTo(1)
  }

  @Test
  fun `Test nested query`() {
    val res = treeWithFunctions.finder().find("fun[{param int}]")
    assertThat(res.vids()).containsExactly("1.1")
  }

  @Test
  fun `Test direct descendant search`() {
    assertThat(search("a>b")).containsExactlyInAnyOrder("1.1", "1.2")
    assertThat(search("a>b>d")).containsExactlyInAnyOrder("1.1.2")
    assertThat(search("a>d")).containsExactlyInAnyOrder("1.3.1")
  }

  @Test
  fun `Mixing selectors`() {
    assertThat(search("a c > e")).containsExactlyInAnyOrder("1.1.1.1")
  }

  @Test
  fun `Search from current item`() {
    assertThat(search("a c>e")).containsExactlyInAnyOrder("1.1.1.1")
    tree.finder().find("b").first().let {
      assertThat(it.find("$>c>e")).hasSize(1)
    }
  }

  @Test
  fun `Search by attribute presence`() {
    assertThat(search("c[lines]")).containsExactlyInAnyOrder("1.1.1")
  }

  @Test
  fun `Search wildcard`() {
    assertThat(search("a *[lines]")).containsExactlyInAnyOrder("1.1.1", "1.1.2")
  }

  @Test
  fun `Search by relations`() {
    assertThat(search("b>e")).isEmpty()
    tree.addRelation("friends", "1.2", "1.1.1.1")
    assertThat(search("b>e")).containsExactlyInAnyOrder("1.1.1.1")
  }

  @Test
  fun `Pseudo elements search`() {
    tree.v("1.1")[":childDs"] = "$ d"

    assertThat(tree.finder().find("b :childDs").map { it.vid })
      .containsExactlyInAnyOrder("1.1.2", "1.1.2.1")
  }

  @Test
  fun `Pseudoelements run`() {
    tree.v("1.1")[":childDs"] = "$ d"

    assertThat(tree.finder().find("b :childDs").map { it.vid })
      .containsExactlyInAnyOrder("1.1.2", "1.1.2.1")
  }

  @Test
  fun `Pseudo elements search error`() {
    assertThat(tree.finder().find("d :unregistered").map { it.vid }).isEmpty()
  }

  @Test
  fun `Evaluate functions`() {
    SelectorFunction.addFunction("sayMyName", "d") { _, _ ->
      "DeeDee"
    }

    assertThat(tree.finder().find("d[sayMyName() as myName]").first().vertice["myName"]).isEqualTo("DeeDee")
  }
}
