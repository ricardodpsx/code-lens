package co.elpache.codelens.codeSearch

import co.elpache.codelens.codeSearch.parser.SelectorFunction
import co.elpache.codelens.codeSearch.search.find
import co.elpache.codelens.codeTree
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.verticeOf
import co.elpache.codelens.tree.vids
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class SearchTest {
  val tree =
    codeTree(
      verticeOf("1", "type" to "a AA", "name" to "parent"),
      codeTree(
        verticeOf("1.1", "type" to "b"),
        codeTree(
          verticeOf("1.1.1", "type" to "c", "lines" to 3),
          codeTree(verticeOf("1.1.1.1", "type" to "e"))
        ),
        codeTree(
          verticeOf("1.1.2", "type" to "d", "lines" to 4),
          codeTree(verticeOf("1.1.2.1", "type" to "d"))
        ),
        codeTree(verticeOf("1.1.3", "type" to "b"))
      ),
      codeTree(verticeOf("1.2", "type" to "b")),
      codeTree(
        verticeOf("1.3", "type" to "a"),
        codeTree(verticeOf("1.3.1", "type" to "d"))
      )
    )

  val treeWithFunctions =
    codeTree(
      verticeOf("1", "type" to "file", "name" to "code.kt"),
      codeTree(
        verticeOf("1.1", "type" to "fun"),
        codeTree(
          verticeOf("1.1.1", "type" to "param"),
          codeTree(
            verticeOf("1.1.1.1", "type" to "int")
          )
        ),
        codeTree(
          verticeOf("1.1.2", "type" to "param")
        )
      ),
      codeTree(
        verticeOf("2.1", "type" to "fun"),
        codeTree(
          verticeOf("2.1.1", "type" to "param")
        )
      )
    )

  private fun search(query: String) = tree.find(query).map { it.vid }

  @Test
  fun `Test Single child`() {
    assertThat(search("a")).containsExactlyInAnyOrder("1", "1.3")
  }

  @Test
  fun `Search in a map`() {
    val tree = CodeTree()
    tree.addVertice(verticeOf("1", "sum" to 2))
    tree.addVertice(verticeOf("2", "sum" to 3))
    tree.addVertice(verticeOf("3", "sum" to 4))

    assertThat(tree.find("*[sum=2]")[0].toMap()).isEqualTo(mapOf("vid" to "1", "sum" to 2))
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
  fun `Can set metrics into nodes with as`() {
    val res = treeWithFunctions.find("fun[{param|count()} as paramCount]")

    assertThat(res.vids()).containsExactly("1.1", "2.1")

    assertThat(treeWithFunctions.v("1.1").getInt("paramCount")).isEqualTo(2)
    assertThat(treeWithFunctions.v("2.1").getInt("paramCount")).isEqualTo(1)
  }

  @Test
  fun `Test nested query`() {
    val res = treeWithFunctions.find("fun[{param int}]")
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
    tree.find("b").first().let {
      assertThat(tree.find("$>c>e", it)).hasSize(1)
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
    assertThat(search("b-friends>e")).containsExactlyInAnyOrder("1.1.1.1")
  }

  @Test
  @Ignore
  fun `Pseudo elements search`() {
    tree.v("1.1")[":childDs"] = "$ d"

    assertThat(tree.find("b :childDs").vids())
      .containsExactlyInAnyOrder("1.1.2", "1.1.2.1")
  }

  @Test
  @Ignore
  fun `Pseudoelements run`() {
    tree.v("1.1")[":childDs"] = "$ d"

    assertThat(tree.find("b :childDs").vids())
      .containsExactlyInAnyOrder("1.1.2", "1.1.2.1")
  }

  @Test
  @Ignore
  fun `Pseudo elements search error`() {
    assertThat(tree.find("d :unregistered").vids()).isEmpty()
  }

  @Test
  fun `Evaluate functions`() {
    SelectorFunction.addFunction("sayMyName", "d") { _, _ ->
      "DeeDee"
    }
    assertThat(tree.find("d[sayMyName() as myName]").first()["myName"]).isEqualTo("DeeDee")
  }
}
