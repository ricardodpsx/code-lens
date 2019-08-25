package codelens

import co.elpache.codelens.CodeBase
import co.elpache.codelens.buildCodeTree
import co.elpache.codelens.printTree
import co.elpache.codelens.selectCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

//Todo: Change tests to be more independent using utils graph
class JavaScriptSupport {

  private val codeBase =
    buildCodeTree(CodeBase.load("src/../frontend/src/"))

  @Test
  fun `Can select files`() {

    System.out.println(printTree(codeBase))

    assertThat(selectCode(codeBase, "file[name*='Example']"))
      .extracting("name")
      .containsExactly("ExampleClass.kt", "ExampleClassB.kt")
  }


}
