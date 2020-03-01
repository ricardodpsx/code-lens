package codelens

import co.elpache.codelens.codeLoader.FolderLoader
import co.elpache.codelens.codeSearch.search.find
import co.elpache.codelens.extensions.kotlin.kotlinLanguageIntegration
import co.elpache.codelens.selectCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

private val codeBase = FolderLoader.loadDir("../code-examples/kotlin/subpackage")
  .extensions(kotlinLanguageIntegration).load()

//Todo: Change tests to be more independent using utils graph
class KotlinSupportTest {
  @Test
  fun `can print tree`() {
    println(codeBase.asString())
  }

  @Test
  fun `Can select files`() {
    codeBase.find("file[name*='Example']")
    assertThat(selectCode(codeBase, "file[name*='Example']"))
      .extracting("name")
      .containsExactlyInAnyOrder("ExampleClass", "ExampleClassB")
  }

  @Test
  fun `Can select by name`() {
    assertThat(selectCode(codeBase, "#ExampleClassB"))
      .extracting("name")
      .containsExactlyInAnyOrder("ExampleClassB", "ExampleClassB")
  }


  @Test
  fun `Can select Functions whitin files`() {
    val list = selectCode(codeBase, "file[name*='Example'] fun[name^='my']")
    assertThat(list).extracting("name").containsExactlyInAnyOrder("myFunction1", "myFunction2", "myFunction3")
  }

  @Test
  fun `Can select Functions by line`() {
    val list = codeBase.find("fun[lines=4]").map { it.vertice.toMap() }
    assertThat(list).extracting("name").contains("methodWithFourLines")
  }

  @Test
  fun `Looking for nested functions`() {
    val list = selectCode(codeBase, "file[name*='ExampleClassB'] fun fun fun")
    assertThat(list).extracting("name").containsExactlyInAnyOrder("c", "b")
  }

  @Test
  fun `Can select without File`() {
    val list = selectCode(codeBase, "class")
    assertThat(list).extracting("name").contains("ExampleClass", "ExampleClassB", "AnAnnotation")
  }

  @Test
  fun `Can use direct descendant selectors`() {
    val list = selectCode(codeBase, "class[name='ExampleClassB'] classBody>fun")
    assertThat(list).extracting("name").containsExactlyInAnyOrder("method1", "method2")
  }

  @Test
  fun `Can use direct child character`() {
    val list = selectCode(codeBase, "class[name='ExampleClassB'] classBody>fun fun")
    assertThat(list).extracting("name").containsExactlyInAnyOrder("x", "y")
  }

  @Test
  fun `Can search code inside subfolders`() {
    val res = selectCode(codeBase, "dir file")
    assertThat(res).extracting("name").contains("AClass")
  }

}

