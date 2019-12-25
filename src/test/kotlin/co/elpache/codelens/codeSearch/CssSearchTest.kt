package co.elpache.codelens.codeSearch

import co.elpache.codelens.codeSearch.search.finder
import co.elpache.codelens.codeSearch.search.vids
import co.elpache.codelens.codeTree
import co.elpache.codelens.tree.vDataOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CssSearchTest {
  val tree =
    codeTree(
      "1", vDataOf("type" to "a", "name" to "parent"),
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
  fun `Test attribute search`() {
    println(tree.asString())
    assertThat(search("a[name='parent']")).containsExactlyInAnyOrder("1")
  }

  @Test
  fun `Aggregator search`() {
    assertThat(tree.finder().findValue("a b | count")).isEqualTo(3)
  }

  @Test
  fun `Can set metrics into nodes with SET sintax`() {
    val res = treeWithFunctions.finder().setQuery("SET {fun} paramCount = {param|count}")

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
    assertThat(search("a c>e")).containsExactlyInAnyOrder("1.1.1.1")
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

}
