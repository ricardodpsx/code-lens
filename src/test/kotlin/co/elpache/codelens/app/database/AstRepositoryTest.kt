package co.elpache.codelens.app.database

import co.elpache.codelens.app.CodeLensApp
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [CodeLensApp::class])
class AstRepositoryTest {

  @Autowired
  lateinit var astRepository: AstRepository

  @Test
  fun astCanBeSaved() {
    astRepository.deleteAll()

    astRepository.save(AstRecord("someCommit", "{}"))

//    assertThat(astRepository.findAll().count()).isEqualTo(1)
    assertThat(astRepository.findAll()).extracting("commit").contains("someCommit")
  }


}