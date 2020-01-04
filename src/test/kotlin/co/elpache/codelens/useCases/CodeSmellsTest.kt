package co.elpache.codelens.useCases;

import co.elpache.codelens.codeTree
import co.elpache.codelens.createCodeSmellsUseCases
import co.elpache.codelens.tree.vDataOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CodeSmellsTest {

  private lateinit var uc: CodeSmellsUseCases

  @Before
  fun setUp() {
    uc = createCodeSmellsUseCases(
      codeTree(
        "1",
        vDataOf("type" to "file"),
        codeTree(
          "2",
          vDataOf("type" to "files"),
          codeTree("3", vDataOf("type" to "fun", "params" to 2)),
          codeTree("4", vDataOf("type" to "fun", "params" to 2)),
          codeTree("5", vDataOf("type" to "fun", "params" to 5))
        ),
        codeTree("6", vDataOf("type" to "fun", "params" to 5)),
        codeTree("7", vDataOf("type" to "fun", "params" to 2)),
        codeTree("8", vDataOf("type" to "fun", "params" to 3))
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

    assertThat(results.analyticsResults.rows).containsExactly(
      ParamFrequencyRow(5.0, 2, listOf("5", "6"))
    )
    assertThat(results.isStinky).isTrue()

    assertThat(results.smellResults.results).containsExactly("5", "6")
    //Assertions.assertThat(results.smellScore).isEqualTo(0.66)
  }


}

