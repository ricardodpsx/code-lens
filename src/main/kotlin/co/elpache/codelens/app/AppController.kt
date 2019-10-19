package co.elpache.codelens.app

import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.UseCases
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class AppController {

  val useCases = UseCases()
//  private val codeBase =
//    expandFullCodeTree(CodeBase.load("src/../frontend/src/"))
//

  data class SearchResult(
    val codeTree: Map<String, Any>,
    val results: List<String> = listOf(),
    val analyticsParams: List<String>
  )

  @GetMapping("/")
  @ResponseBody
  fun cssQuery(@RequestParam query: String) =
    with(useCases.selectCodeWithParents(query)) {
      SearchResult(
        treeWithDescendants.toMap(),
        results,
        useCases.getPossibleIntParams(query)
      )
    }


  @GetMapping("/node/{vid}")
  @ResponseBody
  fun openFile(@PathVariable vid: String) = useCases.loadNodeContents(vid)


  @GetMapping("/analytics/{param}")
  @ResponseBody
  fun analytics(@PathVariable param: String, @RequestParam query: String) =
    useCases.getFrequencyByParam(query, param).rows


  @GetMapping("/history/{param}")
  @ResponseBody
  fun history(@PathVariable param: String, @RequestParam query: String) =
    useCases.collectHistory(query, param, listOf("d37fb4b", "a1e3958"))//Todo: remove hardcode

}