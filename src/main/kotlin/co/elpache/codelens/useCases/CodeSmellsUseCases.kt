package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.tree.Vid

data class SmellsResults(
  val smellDetails: SmellsPreset,
  val smellResults: SearchResultsWithParams,
  val analyticsResults: AnalyticsResults,
  val isStinky: Boolean
  //val smellScore: Double,
)

data class SmellsPreset(
  val title: String,
  val description: String,
  val query: Vid,
  val param: String,
  val isStinky: Boolean
)

class CodeSmellsUseCases(factory: Factory = Factory()) {

  private val codeExplorerUseCases = CodeExplorerUseCases(factory)

  private val smellsPresets = mapOf(
    "longParameterList" to SmellsPreset(
      "Long Parameter List",
      "functions should have max 4 parameters",
      "fun[params>10]",
      "params",
      false
    ),
    "longFunction" to SmellsPreset("Long Function", "", "fun[lines>6]", "lines", false),
    "longMethodList" to SmellsPreset("Long Method List", "", "class[methods>4]", "methods", false),
    "nestedCode" to SmellsPreset("Nested Code", "", "fun[depth>3]", "depth", false)
  )

  private fun findSmellByName(name: String): SmellsPreset {
    return smellsPresets[name]!!
  }

  fun getSmellPresets(): Map<String, SmellsPreset> {
    return smellsPresets.mapValues { presetValue ->
      SmellsPreset(
        presetValue.value.title,
        presetValue.value.description,
        presetValue.value.query,
        presetValue.value.param,
        isSmellStinky(presetValue.value.query, presetValue.value.param)
      )
    }
  }

  private fun isSmellStinky(query: String, param: String) =
    codeExplorerUseCases.getFrequencyByParam(query, param).rows.isNotEmpty()


  fun executeCodeSmell(smellName: String): SmellsResults {
    val smellPreset = findSmellByName(smellName)
    val smellResults = codeExplorerUseCases.getSearchResultsWithParams(smellPreset.query)
    val smellAnalytics = codeExplorerUseCases.getParamDistribution(smellPreset.query, smellPreset.param)
    return SmellsResults(
      smellPreset,
      smellResults,
      smellAnalytics,
      smellAnalytics.rows.isNotEmpty()
    );
  }

  fun list(): Map<String, SmellsPreset> = getSmellPresets()

}
