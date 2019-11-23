package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.tree.Vid

data class SmellsResults(
    val smellDetails: SmellsPreset,
    val smellResults: SearchResultsWithParams,
    val analyticsResults: AnalyticsResults,
    val checkSmell: Boolean
  //val smellScore: Double,
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
    val smellPreset = findSmellByName(smellName)
    val smellResults = codeExplorerUseCases.getSearchResultsWithParams(smellPreset.query)
    val smellAnalytics = codeExplorerUseCases.getFrequencyByParam(smellPreset.query, smellPreset.param)
    return SmellsResults(
        smellPreset,
        smellResults,
        smellAnalytics,
        smellAnalytics.rows.isNotEmpty()
    );
  }
}