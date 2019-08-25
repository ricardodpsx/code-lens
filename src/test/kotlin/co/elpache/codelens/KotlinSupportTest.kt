package codelens

import co.elpache.codelens.CodeBase
import co.elpache.codelens.buildCodeTree
import co.elpache.codelens.printTree
import co.elpache.codelens.selectCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

//Todo: Change tests to be more independent using utils graph
class KotlinSupportTest {

  private val codeBase =
    buildCodeTree(CodeBase.load("src/test/kotlin/co/elpache/codelens/subpackage/"))

  @Test
  fun `can print tree`() {

    println(printTree(codeBase))
  }

  @Test
  fun `Can select files`() {
    assertThat(selectCode(codeBase, "file[name*='Example']"))
      .extracting("name")
      .containsExactly("ExampleClass.kt", "ExampleClassB.kt")
  }

  @Test
  fun `Can select Functions whitin files`() {
    val list = selectCode(codeBase, "file[name*='Example'] fun[name^='my']")
    assertThat(list).extracting("name").containsExactly("myFunction1", "myFunction2", "myFunction3")
  }

  @Test
  fun `Can select Functions by line`() {
    val list = selectCode(codeBase, "fun[textLines='6']")
    assertThat(list).extracting("name").contains("methodWithSixLines")
  }

  @Test
  fun `Looking for nested functions`() {
    val list = selectCode(codeBase, "file[name*='ExampleClassB'] fun fun fun")
    assertThat(list).extracting("name").containsExactly("b", "c")
  }

  @Test
  fun `Can select without File`() {
    val list = selectCode(codeBase, "class")
    assertThat(list).extracting("name").contains("ExampleClass", "ExampleClassB", "AnAnnotation")
  }

  @Test
  fun `Can use direct descendant selectors`() {
    val list = selectCode(codeBase, "class[name='ExampleClassB'] classBody>fun")
    assertThat(list).extracting("name").containsExactly("method1", "method2")
  }

  @Test
  fun `Can use direct child character`() {
    val list = selectCode(codeBase, "class[name='ExampleClassB'] classBody>fun fun")
    assertThat(list).extracting("name").containsExactly("x", "y")
  }

  @Test
  fun `Can search code inside subfolders`() {
    val res = selectCode(codeBase, "dir file")
    assertThat(res).extracting("name").contains("AClass.kt")
  }

}

