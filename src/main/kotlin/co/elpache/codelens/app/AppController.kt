package co.elpache.codelens.app

import co.elpache.codelens.useCases.CodeExplorerUseCases
import co.elpache.codelens.useCases.CodeSmellsUseCases
import co.elpache.codelens.useCases.EvolutionUseCases
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class AppController {

  @Autowired
  lateinit var codeExplorerUseCases: CodeExplorerUseCases

  @Autowired
  lateinit var evolutionUseCases: EvolutionUseCases

  @Autowired
  lateinit var codeSmellsUseCases: CodeSmellsUseCases


  @GetMapping("/")
  @ResponseBody
  fun cssQuery(@RequestParam query: String) = codeExplorerUseCases.getSearchResultsWithParams(query)


  @GetMapping("/node/{vid}")
  @ResponseBody
  fun openFile(@PathVariable vid: String) = codeExplorerUseCases.loadNodeContents(vid)


  @GetMapping("/analytics/{param}")
  @ResponseBody
  fun analytics(@PathVariable param: String, @RequestParam query: String) =
    codeExplorerUseCases.getFrequencyByParam(query, param).rows


  @GetMapping("/history/{param}")
  @ResponseBody
  fun history(@PathVariable param: String, @RequestParam query: String, @RequestParam maxCommits: Int) =
    evolutionUseCases.collectHistory(query, param, maxCommits)


  @GetMapping("/smell")
  @ResponseBody
  fun smells() =
    codeSmellsUseCases.list()

  @GetMapping("/smell/{smellName}")
  @ResponseBody
  fun executeSmell(@PathVariable smellName: String) =
    codeSmellsUseCases.executeCodeSmell(smellName)

}