package codelens

import co.elpache.codelens.codeSearch.search.finder
import org.junit.Test

class JsSupportTest : LanguageSupportTests("js", "../code-examples/js/fixtures/") {
  @Test
  fun `can print js tree`() {
    tree.finder().find("#functionWith2Lines")[0].printTree()
  }
}