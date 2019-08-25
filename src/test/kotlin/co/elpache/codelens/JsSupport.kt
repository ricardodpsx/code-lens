package codelens

import co.elpache.codelens.CodeBase
import co.elpache.codelens.expandFullCodeTree
import co.elpache.codelens.printTree
import org.junit.Test

//Todo: Change tests to be more independent using utils graph
class JsSupport {

  private val codeBase =
    expandFullCodeTree(CodeBase.load("src/../frontend/src/"))

  @Test
  fun `Can select files`() {

    System.out.println(printTree(codeBase))

    //Todo: Pending assertion
  }


}
