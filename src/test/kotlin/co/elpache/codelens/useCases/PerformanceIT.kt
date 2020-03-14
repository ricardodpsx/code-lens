package co.elpache.codelens.useCases;

import co.elpache.codelens.Factory
import co.elpache.codelens.app.CodeLensApp
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import kotlin.system.measureTimeMillis


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [CodeLensApp::class])
@ActiveProfiles(profiles = ["test"])
class PerformanceIT {

  @Autowired
  lateinit var applicationContext: ApplicationContext

  @Test
  fun `Common query times`() {
    var factory = Factory(path = "tmp", currentCodePath = "../code-examples/", context = applicationContext)

    var uc = CodeExplorerUseCases(factory)
    checkTime(7) { uc.codeTree }

    checkTime(1) { uc.find("fun[{try}]") }
  }

  fun checkTime(maxSeconds: Int, cb: () -> Unit) {
    val time = measureTimeMillis { cb() }
    assertThat(time / 1000).isLessThan(7)
    System.out.println("Time $time")
  }


}
