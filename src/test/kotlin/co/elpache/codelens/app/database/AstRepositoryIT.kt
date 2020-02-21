package co.elpache.codelens.app.database

import co.elpache.codelens.app.CodeLensApp
import co.elpache.codelens.codeTree
import co.elpache.codelens.compareTreeOutputs
import co.elpache.codelens.tree.verticeOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [CodeLensApp::class])
@ActiveProfiles(profiles = ["test"])
class AstRepositoryIT {

  @Autowired
  lateinit var astRepository: AstRepository

  @Autowired
  lateinit var astStore: AstStore

  @Test
  fun astCanBeSaved() {
    astRepository.deleteAll()

    val t = codeTree(
      verticeOf("1"),
      codeTree(verticeOf("2", "name" to "a")),
      codeTree(verticeOf("3", "name" to "b"))
    )

    astStore.save("someCommit", t)
    assertThat(astRepository.findAll()).extracting("commit").contains("someCommit")

    compareTreeOutputs(
      astStore.load("someCommit")!!,
      """{code=<Excluded>}
 - {name=a}
 - {name=b}
"""
    )
  }


}