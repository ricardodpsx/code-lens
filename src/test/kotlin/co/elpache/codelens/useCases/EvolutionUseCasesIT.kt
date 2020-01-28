package co.elpache.codelens.useCases;

import co.elpache.codelens.Factory
import co.elpache.codelens.app.CodeLensApp
import co.elpache.codelens.createCommits
import org.assertj.core.api.Assertions.assertThat
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
class EvolutionUseCasesIT {

  @Autowired
  lateinit var applicationContext: ApplicationContext



  @Test
  fun `Can see change in a function`() {
    var factory = Factory(path = "tmp", currentCodePath = "../code-examples/", context = applicationContext)

    factory.preloadCommits(createCommits("d37fb4b", "a1e3958"))

    val uc = EvolutionUseCases(factory)

    val statistics = uc.collectHistory(
      "#kotlin #ExampleClass #functionWithParams", "params", createCommits("d37fb4b", "a1e3958")
    )

    assertThat(statistics[0].statistics.max).isEqualTo(2.0)
    assertThat(statistics[1].statistics.max).isEqualTo(3.0)
  }

  @Test
  fun `Can see change of methods in a class`() {
    var factory = Factory(path = "tmp", currentCodePath = "../code-examples/", context = applicationContext)

    factory.preloadCommits(createCommits("e3b714c", "e323c18"))

    val uc = EvolutionUseCases(factory)

    val statistics = uc.collectHistory(
      "#ExampleClass", "methods",
      createCommits("e3b714c", "e323c18")
    )

    assertThat(statistics[0].statistics.max).isEqualTo(1.0)
    assertThat(statistics[1].statistics.max).isEqualTo(2.0)
  }

}
