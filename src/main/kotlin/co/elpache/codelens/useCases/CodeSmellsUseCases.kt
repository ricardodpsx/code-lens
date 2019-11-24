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

    private val smellsPresets = mapOf(
      "longParameterList" to SmellsPreset(
        "Long Parameter List",
        "functions should have max 4 parameters",
        "fun[params>4]",
        "params"
      ),
      "longFunction" to SmellsPreset("Long Function", "", "fun[lines>6]", "lines"),
      "longMethodList" to SmellsPreset("Long Method List", "", "class[methods>4]", "methods"),
      "nestedCode" to SmellsPreset("Nested Code", "", "fun[depth>3]", "depth")
    )

    fun findSmellByName(name: String): SmellsPreset {
      return smellsPresets[name]!!
    }

    fun getSmellPresets(): Map<String, SmellsPreset> {
      return smellsPresets
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

  fun list(): Map<String, SmellsPreset> = smellsPresets

}