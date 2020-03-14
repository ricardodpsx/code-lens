package codelens

import co.elpache.codelens.codeLoader.FolderLoader
import co.elpache.codelens.codeSearch.search.find
import co.elpache.codelens.extensions.js.jsLanguageIntegration
import co.elpache.codelens.extensions.kotlin.kotlinLanguageIntegration
import org.assertj.core.api.SoftAssertions
import org.junit.After
import org.junit.Test

abstract class LanguageSupportTests(val ext: String, path: String) : SoftAssertions() {
  val tree = FolderLoader.loadDir(path)
    .extensions(jsLanguageIntegration, kotlinLanguageIntegration).load()


  val search = { css: String ->
    tree.find(css).map { it.toMap() }
  }

  val getValue = { funName: String, metric: String ->
    search(funName).first { it.containsKey(metric) }[metric]
  }

  @After
  fun after() {
    assertAll()
  }

  @Test
  fun `Can get the code of a node`() {
    tree.find("#functions fun").first()
    val n = tree.find("#functions fun").first()
    assertThat(tree.code(n.vid)).isNotEmpty
  }

  @Test
  fun `Test Functions`() {
    val funs = search("fun")

    assertThat(funs).extracting("name")
      .contains("functionWith2NestingLevels", "functionWith2Lines", "functionWith3Params")

    funs.forEach {
      assertThat(it["start"]).isNotNull
      assertThat(it["end"]).isNotNull
      assertThat(it["lines"]).isNotNull
      assertThat(it["params"]).isNotNull
      assertThat(it["type"]).isNotNull
      assertThat(it["depth"]).isNotNull
    }

    assertThat(getValue("#functionWith2Lines", "lines") as Int).isEqualTo(2)
    assertThat(getValue("#functionWith3Params", "params") as Int).isEqualTo(3)
    assertThat(getValue("#functionWith2NestingLevels", "depth") as Int).isEqualTo(2)
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

    assertThat(getValue("#Rectangle4", "methods")).isEqualTo(3)
    assertThat(getValue("#Rectangle4", "constructors")).isEqualTo(1)
  }

  @Test
  fun `can import`() {
    val res = tree.find("#withImports").first()

    assertThat(tree.find("file-imports>file[name='functions']", res).first()["name"]).isEqualTo("functions")
    assertThat(tree.find("file[{$-imports>file[name='functions']}]", res).first()["name"]).isEqualTo("withImports")
  }
}