package co.elpache.codelens;

import co.elpache.codelens.app.CodeLensApp
import co.elpache.codelens.codeSearch.search.find
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [CodeLensApp::class])
@ActiveProfiles(profiles = ["test"])
class GitCommitsExtensionIT {

  @Autowired
  lateinit var applicationContext: ApplicationContext

  var factory: Factory = Factory()


  @Before
  fun setup() {
    //Todo: The git log should be independent of the repository configuration
    factory = Factory(path = "tmp", currentCodePath = "../code-examples/", context = applicationContext)
  }

  @Test
  fun `can see changes in a file`() {
    val code = factory.createBaseCode()

    val commits = createCommits(
      "02e11cf8b0d08be5eb3424274d158505c9ec5045",
      "2a03857cb6bcc37176b5b2e57bccd49bc115b1bb",
      "f13056f78491cab196849f1965fec95264bb1d13"
    )

    assertThat(code.find("commit").map { it["id"] }).containsAll(commits.map { it.id })
  }


}