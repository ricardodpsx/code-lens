package co.elpache.codelens.app

import co.elpache.codelens.UseCases
import co.elpache.codelens.toMap
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
    val resulTree: Map<String, Any>,
    val results: List<String> = listOf()
  )

  @GetMapping("/")
  @ResponseBody
  fun cssQuery(@RequestParam cssQuery: String) =
    with(useCases.selectCodeWithParents(cssQuery)) {
      SearchResult(
        toMap(treeWithDescendants),
        results
      )
    }


  @GetMapping("/node/{vid}")
  @ResponseBody
  fun openFile(@PathVariable vid: String) = useCases.loadNodeContents(vid)

}