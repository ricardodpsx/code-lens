package co.elpache.codelens.extensions

import co.elpache.codelens.codeLoader.FolderLoader
import co.elpache.codelens.codeSearch.search.finder
import co.elpache.codelens.extensions.js.jsLanguageIntegration
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File

class FolderLoaderTest {

  val tree = FolderLoader(File("../code-examples/js/"), File("../code-examples/"))
    .extensions(jsLanguageIntegration)
    .doLoad()


  @Test
  fun `can load folders`() {

    assertThat(tree.vertices.map { it.value.data }).extracting("name")
      .contains("fixtures", "api-server", "frontend", "language")

    assertThat(tree.finder().find("#frontend #src dir").map { it.data }).extracting("name")
      .contains("api", "components", "reducer")

  }

  @Test
  fun `can load Files`() {

    assertThat(tree.vertices.map { it.value.data }).extracting("fileName")
      .contains("index.js", "registerServiceWorker.js")

    assertThat(tree.finder().find("#frontend #src #api file").map { it.data }).extracting("name")
      .contains("rest", "postsApi")
  }
}