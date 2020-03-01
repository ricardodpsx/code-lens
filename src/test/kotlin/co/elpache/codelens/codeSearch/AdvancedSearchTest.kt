package co.elpache.codelens.codeSearch

import co.elpache.codelens.codeSearch.search.find
import co.elpache.codelens.codeSearch.search.vids
import co.elpache.codelens.codeTree
import co.elpache.codelens.tree.verticeOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class AdvancedSearchTest {

  val tree =
    codeTree(
      verticeOf("1", "type" to "file dir", "name" to "rootPackage"),
      codeTree(
        verticeOf("1.1", "type" to "file dir", "name" to "packageA"),
        codeTree(verticeOf("1.2", "type" to "file codeFile", "name" to "a")),
        codeTree(verticeOf("1.3", "type" to "file codeFile", "name" to "b"))
      ),
      codeTree(
        verticeOf("2", "type" to "file dir", "name" to "packageB"),
        codeTree(verticeOf("2.1", "type" to "file codeFile", "name" to "a")),
        codeTree(verticeOf("2.2", "type" to "file codeFile", "name" to "b"))
      )
    )

  @Test
  fun `Test imports`() {
    tree.addRelation("imports", "1.2", "1.3")

    assertThat(tree.find("codeFile[{$-imports>#b}]").vids())
      .containsExactlyInAnyOrder("1.2")
  }

  @Test
  @Ignore
  fun `Test collapsing`() {
    tree.addRelation("imports", "1.1", "2.1")
    tree.addRelation("imports", "1.1", "2.2")
    tree.addRelation("imports", "1.2", "2.2")

    tree.find("file|collapsing()")
  }

}
