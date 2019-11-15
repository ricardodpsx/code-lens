package co.elpache.codelens.useCases

import co.elpache.codelens.Factory
import co.elpache.codelens.tree.Vid

data class SmellsResults(
    val checkSmell: Boolean,
    //val smellScore: Double,
    val analyticsResults: AnalyticsResults
)

data class SmellsPresets(
    val title: String,
    val query: Vid,
    val param: String
)

public class CodeSmellsUseCases(factory: Factory = Factory()) {

    private val codeExplorerUseCases = CodeExplorerUseCases(factory)
    private val smellsPresets = HashMap<String, SmellsPresets>()

    init {
        smellsPresets["longParameterList"] = SmellsPresets("Long Parameter List", "fun[params=4]", "params")
    }

    fun checkLongParameterList(): SmellsResults {
        val preset = smellsPresets["longParameterList"]
        val smellingResults = codeExplorerUseCases.getFrequencyByParam(preset!!.query, preset.param)
        return SmellsResults(smellingResults.rows.isNotEmpty(), smellingResults);
    }
}