package codelens

import co.elpache.codelens.codetree.CodeFolder
import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.codetree.NodeData
import co.elpachecode.codelens.cssSelector.finder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File

private val n = CodeFolder(
  File("../code-examples/js/fixtures/")
)
private val tree = CodeTree().expandFullCodeTree(n).applyAnalytics()

//Todo: Change tests to be more independent using utils graph
class JsSupportTest {
  val ext = "js"

  @Test
  fun `print tree`() {
    println(tree.asString())
  }


  @Test
  fun `Test Functions`() {
    val funs = search("fun")

    assertThat(funs).extracting("name")
      .contains("functionWith2NestingLevels", "functionWith2Lines", "functionWith3Params")

    funs.forEach {
      assertThat(it["startOffset"]).isNotNull
      assertThat(it["endOffset"]).isNotNull
      assertThat(it["lines"]).isNotNull
      assertThat(it["params"]).isNotNull
      assertThat(it["type"]).isNotNull
      assertThat(it["depth"]).isNotNull
    }

    assertThat(search("#functionWith3Params>params>param")).hasSize(3)
    assertThat(search("#functionWith3Params>params>param")).extracting("name").containsExactly("x", "y", "z")

    assertThat(getValue("#functionWith2NestingLevels", "depth") as Int).isEqualTo(2)
    assertThat(getValue("#functionWith2Lines", "lines") as Int).isEqualTo(2)
    assertThat(getValue("#functionWith3Params", "params") as Int).isEqualTo(3)
  }


  @Test
  fun `Test Calls`() {
    assertThat(search("call")).extracting("firstLine").contains("functionWith3Params(1, 2, 3)")


    assertThat(search("call[firstLine^='functionWith3Params']>args>arg")).hasSize(3)
    assertThat(getValue("call[firstLine^='functionWith3Params']", "args")).isEqualTo(3)
  }

  @Test
  fun `Test Files`() {
    assertThat(getValue("#functions", "name")).isEqualTo("functions")
    assertThat(getValue("#functions", "fileName")).isEqualTo("functions.$ext")
    assertThat(getValue("#classes", "lines") as Int).isGreaterThanOrEqualTo(13)
    assertThat(getValue("#functions", "functions") as Int).isGreaterThanOrEqualTo(3)
    assertThat(getValue("#classes", "classes") as Int).isGreaterThanOrEqualTo(4)
    assertThat(getValue("#bindings", "bindings") as Int).isGreaterThanOrEqualTo(2)
  }

  @Test
  fun `Test Classes`() {
    assertThat(search("class")).extracting("name").contains("Animal")
    assertThat(search("#Animal fun")).extracting("name").contains("speak")
    assertThat(search("#Animal fun")).extracting("name").contains("speak")

    tree.finder().first("#Rectangle4").printTree()

    assertThat(getValue("#Rectangle4", "methods")).isEqualTo(3)
    assertThat(getValue("#Rectangle4", "constructors")).isEqualTo(1)
  }

  private fun getValue(funName: String, metric: String) = search(funName).first()[metric] as Any


  private fun search(css: String): List<NodeData> {
    val res = tree.finder().find(css)
    return res.map { it.data }
  }

}
