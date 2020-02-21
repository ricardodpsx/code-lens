package co.elpache.codelens.useCases;

import co.elpache.codelens.codeTree
import co.elpache.codelens.createCodeSmellsUseCases
import co.elpache.codelens.tree.verticeOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CodeSmellsTest {

  private lateinit var uc: CodeSmellsUseCases

  @Before
  fun setUp() {
    uc = createCodeSmellsUseCases(
      codeTree(
        verticeOf("1", "type" to "file"),
        codeTree(
          verticeOf("2", "type" to "files"),
          codeTree(verticeOf("3", "type" to "fun", "params" to 2)),
          codeTree(verticeOf("4", "type" to "fun", "params" to 2)),
          codeTree(verticeOf("5", "type" to "fun", "params" to 5))
        ),
        codeTree(verticeOf("6", "type" to "fun", "params" to 5)),
        codeTree(verticeOf("7", "type" to "fun", "params" to 2)),
        codeTree(verticeOf("8", "type" to "fun", "params" to 3))
      )
    )
  }

  @Test
  fun testRegexp() {
    assertThat("yy/node_modules/xxx".matches(".*node_modules.*".toRegex())).isTrue()
  }


  //Needs
  @Test
  fun `can get code smell list`() {
    val smellPresets = uc.getSmellPresets()

    assertThat(smellPresets).isNotNull
    assertThat(smellPresets["longParameterList"]?.isStinky).isTrue()
  }

  @Test
  fun `can get long parameter list functions`() {
    val results = uc.executeCodeSmell("longParameterList")

    assertThat(results.analyticsResults.rows.find {
      it.paramValue == 5.0 && it.frequency == 2
    }?.nodes)
      .extracting("vid")
      .containsExactly("5", "6")

    assertThat(results.isStinky).isTrue()

    assertThat(results.smellResults.results).extracting("vid").containsExactly("5", "6")
  }


}

