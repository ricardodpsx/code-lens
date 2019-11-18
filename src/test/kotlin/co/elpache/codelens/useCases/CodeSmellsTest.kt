package co.elpache.codelens.useCases;

import co.elpache.codelens.codeTree
import co.elpache.codelens.createCodeSmellsUseCases
import co.elpache.codelens.tree.vDataOf
import org.assertj.core.api.Assertions
import org.junit.Test;

class CodeSmellsTest {

  @Test
  fun `can get code smell list`() {
    val smellPresets = CodeSmellsUseCases.getSmellPresets()

    Assertions.assertThat(smellPresets).isNotNull
  }

  @Test
  fun `can get code smell by name`() {
    val smellPreset = CodeSmellsUseCases.findSmellByName("longParameterList")

    Assertions.assertThat(smellPreset).isNotNull
    Assertions.assertThat(smellPreset?.param).isEqualTo("params")
  }

  @Test
  fun `can get long parameter list functions`() {
    val uc = createCodeSmellsUseCases(
      codeTree(
        "1",
        vDataOf("type" to "file"),
        codeTree(
          "2",
          vDataOf("type" to "files"),
          codeTree("3", vDataOf("type" to "fun", "params" to 2)),
          codeTree("4", vDataOf("type" to "fun", "params" to 2)),
          codeTree("5", vDataOf("type" to "fun", "params" to 4))
        ),
        codeTree("6", vDataOf("type" to "fun", "params" to 4)),
        codeTree("7", vDataOf("type" to "fun", "params" to 2)),
        codeTree("8", vDataOf("type" to "fun", "params" to 3))
      )
    )

    val results = uc.checkLongParameterList()

    Assertions.assertThat(results.checkSmell).isTrue()
    Assertions.assertThat(results.analyticsResults.rows).containsExactly(
      listOf(4, 2)
    )
    //Assertions.assertThat(results.smellScore).isEqualTo(0.66)
  }
}
