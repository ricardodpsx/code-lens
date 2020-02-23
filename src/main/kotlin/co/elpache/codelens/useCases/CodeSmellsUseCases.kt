package co.elpache.codelens.useCases

import co.elpache.codelens.codeSearch.search.find
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

//Todo: Should use cases use other use cases?
class CodeSmellsUseCases(private val codeExplorerUseCases: CodeExplorerUseCases) {

  private val smellsPresets = mapOf(
    "longParameterList" to SmellsPreset(
      "Long Parameter List",
      "functions should have max 4 parameters",
      "fun[params>4]",
      "params",
      false
    ),
    "longFunction" to SmellsPreset("Long Function", "", "fun[lines>6]", "lines", false),
    "longMethodList" to SmellsPreset("Long Method List", "", "class[methods>4]", "methods", false),
    "nestedCode" to SmellsPreset("Nested Code", "", "fun[depth>3]", "depth", false),
    "divergentChange" to SmellsPreset(
      "Divergent Change", "",
      "file[(percentOfChanges(30) as percentOfChanges)>0.1]", "percentOfChanges", false
    ),
    "shoutgunSurgery" to SmellsPreset(
      "Shoutgun Surgery", "",
      "commit[filesAffected>20]", "filesAffected", false
    )
  )

  private fun findSmellByName(name: String): SmellsPreset {
    return smellsPresets[name]!!
  }

  //TODO: This is having side effects
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
    codeExplorerUseCases.codeTree.find(query).isNotEmpty()

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
