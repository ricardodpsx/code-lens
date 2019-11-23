package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.tree.Vid

data class SmellsResults(
  val checkSmell: Boolean,
  //val smellScore: Double,
  val analyticsResults: AnalyticsResults
)

data class SmellsPreset(
    val title: String,
    val description: String,
    val query: Vid,
    val param: String
)

class CodeSmellsUseCases(factory: Factory = Factory()) {

  private val codeExplorerUseCases = CodeExplorerUseCases(factory)

  companion object {
    private val smellsPresets = HashMap<String, SmellsPreset>()

    init {
        smellsPresets["longParameterList"] = SmellsPreset(
            "Long parameter list",
            "functions should have max 3 parameters",
            "fun[params=4]",
            "params"
        )
    }

    fun findSmellByName(name: String): SmellsPreset? {
      return smellsPresets[name]
    }

    fun getSmellPresets(): Map<String, SmellsPreset> {
      return smellsPresets
    }
  }

  fun checkLongParameterList(): SmellsResults {
    val preset = findSmellByName("longParameterList")
    val smellingResults = codeExplorerUseCases.getFrequencyByParam(preset!!.query, preset.param)
    return SmellsResults(smellingResults.rows.isNotEmpty(), smellingResults);
  }
}