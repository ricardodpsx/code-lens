package co.elpache.codelens.useCases;

import co.elpache.codelens.Factory
import co.elpache.codelens.app.CodeLensApp
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [CodeLensApp::class])
class TemporalMetricsTest {

  @Autowired
  lateinit var applicationContext: ApplicationContext

  var factory: Factory = Factory()

  @Before
  fun setup() {
    factory = Factory(path = "tmp", currentCodePath = "../code-examples/change", context = applicationContext)
    Factory.initializeLanguageRegistry()
  }

//  @Test
//  fun `can see changes in a file`() {
//    val code = factory.createBaseCode()
//
//    assertThat(code.finder().find("#fileAddedLater>commits>commit")).extracting("id").contains(
//      "fd3b52fa6cf46fc98ca398629d12a93698b8dcfd",
//      "4e4cf91d604d618ecdc2658d9d700ac7f7b7027f",
//      "5f17efeaf4c23928a464505f4699809ec679da67"
//    )
//  }

}
