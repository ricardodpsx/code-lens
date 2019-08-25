package co.elpache.codelens.languages.js

import co.elpache.codelens.CodeEntity
import co.elpache.codelens.CodeFile
import co.elpache.codelens.CodeTree
import co.elpache.codelens.LanguageCodeEntity
import co.elpache.codelens.and
import co.elpache.codelens.buildAstFile
import co.elpache.codelens.depth
import co.elpache.codelens.relevantCodeLines
import co.elpache.codelens.search
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File


val buildJsFile: buildAstFile = { file: File -> JsFile(file) }

typealias JsNode = Map<String, Any>

fun JsNode.child(key: String): JsNode {
  return this[key] as JsNode
}

fun JsNode.list(key: String): ArrayList<Any> {
  return this[key] as ArrayList<Any>
}

fun <T> JsNode.value(key: String): T = this[key] as T


class JsFile(val file: File) : CodeFile(file) {
  override val lang = "js"
  override fun expand(): List<CodeEntity> {
    val code = contents()
    return parseFile(file).child("program").list("body")
      .map { toJsCodeEntity(it as JsNode, code) }
  }
}


data class JsCodeEntity(
  override val name: String?,
  override val type: String,
  val fileCode: String,
  val node: JsNode
) : LanguageCodeEntity {
  override val startOffset: Int = node.value("start")
  override val endOffset: Int = node.value("end")

  override val code by lazy {
    if(!( startOffset >= 0 && startOffset <= fileCode.length && endOffset >= 0 && endOffset <= fileCode.length)) {
      println("Code offsets mistmatch i")
    }
    fileCode.substring(startOffset, endIndex = Math.min(endOffset, fileCode.length))
  }

  override fun expand() =
    node.values
      .filter { it is Map<*, *> }
      .filter { (it as Map<String, Any>).containsKey("type") }
      .map {
        toJsCodeEntity(it as JsNode, fileCode = fileCode)
      }.plus(
        node.values.filter {
          it is List<*>
        }.map {
          (it as List<Any>)
            .filter { it is Map<*, *> }
            .filter { (it as Map<String, Any>).containsKey("type") }
            .map { toJsCodeEntity(it as JsNode, fileCode = fileCode) }
        }.flatten()
      )

  val data = super.data().and(
    "startOffset" to startOffset,
    "endOffset" to endOffset
  )

  override fun data() = data
}


private fun parseFile(file: File): Map<String, Any> {
  val programOutput = Runtime.getRuntime().exec(
    "node JavaScriptSupport.js ${file.absolutePath}", null,
    File("/Users/ricardodps/PROJECTS/CodeLens/frontend")
  ).inputStream
  //Todo: Process error output to ease troubleshooting
  return ObjectMapper().readValue(programOutput, Map::class.java) as Map<String, Any>
}


private fun toJsCodeEntity(c: JsNode, fileCode: String): CodeEntity {
  return JsCodeEntity(
    name = c["name"] as? String,
    type = c["type"] as String,
    node = c,
    fileCode = fileCode
  );
}



//Todo: Analytics must happen on Demand?
fun jsApplyAnalytics(tree: CodeTree): CodeTree {

  search(tree, "fun").forEach {
    //Lines
    val function = tree.v(it) as LanguageCodeEntity

    //For one line functions
    function.data()["lines"] = relevantCodeLines(function.code)
    //Depth
    function.data()["depth"] = depth(tree, it)

    search(tree, "block", it).getOrNull(0)?.let {
      val block = tree.v(it) as LanguageCodeEntity
      function.data()["lines"] = relevantCodeLines(block.code)
    }

    //Arguments
    search(tree, "valueParameterList", it).getOrNull(0)?.let {
      function.data()["argumentCount"] = (tree.v(it) as LanguageCodeEntity).data()["childrenCount"]!!
    }

  }
  return tree
}