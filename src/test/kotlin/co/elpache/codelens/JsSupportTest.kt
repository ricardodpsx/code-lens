package codelens

import co.elpache.codelens.CodeBase
import co.elpache.codelens.CodeTree
import co.elpache.codelens.buildCodeTree
import co.elpache.codelens.printTree
import co.elpache.codelens.selectCodeWithParents
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

//Todo: Change tests to be more independent using utils graph
class JsSupportTest {

  private val codeBase =
    buildCodeTree(CodeBase.load("src/../frontend/src/"))

  @Test
  fun `can print tree`() {

    println(printTree(codeBase))

  }

}
