package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.tree.Vid

data class SmellsResults(
  val checkSmell: Boolean,
  //val smellScore: Double,
  val searchResults: SearchResultsWithParams,
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
            "fun[params>3]",
            "params"
        )
    }

    fun getSmellPresets(): Map<String, SmellsPreset> {
      return smellsPresets
    }

    private fun findSmellByName(name: String): SmellsPreset {
      return smellsPresets[name]!!
    }
  }

  fun executeCodeSmell(smellName: String): SmellsResults {
    val preset = findSmellByName(smellName)
    val smellResults = codeExplorerUseCases.getSearchResultsWithParams(preset.query)
    val smellAnalytics = codeExplorerUseCases.getFrequencyByParam(preset.query, preset.param)
    return SmellsResults(
        smellAnalytics.rows.isNotEmpty(),
        smellResults,
        smellAnalytics
    );
  }
}