package codelens

import co.elpachecode.codelens.cssSelector.search.finder
import org.junit.Test


class JsSupportTest : LanguageSupportTests("js", "../code-examples/js/fixtures/") {

  @Test
  fun `can print tree`() {
    tree.finder().find("#classes")[0].printTree()
  }

}