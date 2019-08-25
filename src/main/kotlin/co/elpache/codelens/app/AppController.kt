package co.elpache.codelens.app

import co.elpache.codelens.CodeBase
import co.elpache.codelens.CodeFile
import co.elpache.codelens.buildCodeTree
import co.elpache.codelens.tree.subTree
import co.elpache.codelens.toMap
import co.elpache.codelens.selectCodeWithParents
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class AppController {
//  val codeBase =
//    buildCodeTree(CodeBase.load("src/test/kotlin/co/elpache/codelens/subpackage/"))

  private val codeBase =
    buildCodeTree(CodeBase.load("src/../frontend/src/"))


  data class SearchResult(
    val resulTree: Map<String, Any>,
    val results: List<String> = listOf()
  )

  @GetMapping("/")
  @ResponseBody
  fun cssQuery(@RequestParam cssQuery: String) =
    with(selectCodeWithParents(codeBase, cssQuery)) {
      SearchResult(
        toMap(treeWithDescendants),
        results
      )
    }


  data class NodeResult(
    val text: String,
    val ast:Map<String, Any>
  )

  @GetMapping("/node/{vid}")
  @ResponseBody
  fun openFile(@PathVariable vid: String) = NodeResult(
    (codeBase.v(vid) as CodeFile).contents(),
    toMap(subTree(codeBase, vid))
  )

}