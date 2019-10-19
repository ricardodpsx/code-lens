package codelens

import co.elpache.codelens.codetree.CodeFolder
import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.selectCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

//Todo: Change tests to be more independent using utils graph
class KotlinSupportTest {

  private val codeBase =
    CodeTree().expandFullCodeTree(CodeFolder.load("tmp/kotlin/subpackage/"))

  @Test
  fun `can print tree`() {

    println(codeBase.printTree())
  }

  @Test
  fun `Can select files`() {
    assertThat(selectCode(codeBase, "file[name*='Example']"))
      .extracting("name")
      .containsExactlyInAnyOrder("ExampleClass", "ExampleClassB")
  }

  @Test
  fun `Can select by name`() {
    println(selectCode(codeBase, "#ExampleClassB"))
    assertThat(selectCode(codeBase, "#ExampleClassB"))
      .extracting("name")
      .containsExactlyInAnyOrder("ExampleClassB")
  }


  @Test
  fun `Can select Functions whitin files`() {
    val list = selectCode(codeBase, "file[name*='Example'] fun[name^='my']")
    assertThat(list).extracting("name").containsExactlyInAnyOrder("myFunction1", "myFunction2", "myFunction3")
  }

  @Test
  fun `Can select Functions by line`() {
    val list = selectCode(codeBase, "fun[textLines=6]")
    assertThat(list).extracting("name").contains("methodWithSixLines")
  }

  @Test
  fun `Looking for nested functions`() {
    val list = selectCode(codeBase, "file[name*='ExampleClassB'] fun fun fun")
    assertThat(list).extracting("name").containsExactlyInAnyOrder("b", "c")
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

