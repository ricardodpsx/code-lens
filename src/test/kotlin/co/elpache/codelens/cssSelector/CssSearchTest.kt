package co.elpache.codelens.cssSelector

import co.elpache.codelens.codeTree
import co.elpache.codelens.codeTreeNode
import co.elpachecode.codelens.cssSelector.finder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CssSearchTest {
  val tree =
    codeTree(
      "1",
      codeTreeNode("type" to "a", "name" to "parent"),
      codeTree(
        "1.1",
        codeTreeNode("type" to "b"),
        codeTree("1.1.1", codeTreeNode("type" to "c", "lines" to 3)),
        codeTree(
          "1.1.2",
          codeTreeNode("type" to "d", "lines" to 4),
          codeTree("1.1.2.1", codeTreeNode("type" to "d"))
        ),
        codeTree("1.1.3", codeTreeNode("type" to "b"))
      ),
      codeTree("1.2", codeTreeNode("type" to "b")),
      codeTree(
        "1.3",
        codeTreeNode("type" to "a"),
        codeTree("1.3.1", codeTreeNode("type" to "d"))
      )
    )

  fun search(query: String) = tree.finder().find(query).map { it.vid }

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
  fun `Test direct descendant search`() {
    assertThat(search("a>b")).containsExactlyInAnyOrder("1.1", "1.2")
    assertThat(search("a>b>d")).containsExactlyInAnyOrder("1.1.2")
    assertThat(search("a>d")).containsExactlyInAnyOrder("1.3.1")
  }

}
