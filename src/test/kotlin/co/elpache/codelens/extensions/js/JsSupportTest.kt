package codelens

import co.elpache.codelens.codeSearch.search.find
import org.junit.Test

class JsSupportTest : LanguageSupportTests("js", "../code-examples/js/fixtures/") {
  @Test
  fun `can print js tree`() {
    tree.find("#functions").forEach {
      tree.print(it.vid)
    }
  }
}