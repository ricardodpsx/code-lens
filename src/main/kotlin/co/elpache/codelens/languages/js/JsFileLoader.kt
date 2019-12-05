package co.elpache.codelens.languages.js

import co.elpache.codelens.codeLoader.FileLoader
import co.elpache.codelens.codeLoader.LanguageIntegration
import co.elpache.codelens.codeLoader.languageSupportRegistry
import co.elpache.codelens.tree.VData
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import kotlin.streams.toList


fun jsInit() {
  languageSupportRegistry["js"] = jsLanguageIntegration
}


val jsLanguageIntegration = LanguageIntegration(
  fileLoaderBuilder = ::JsFileLoader,
  applyMetrics = ::applyJsMetrics,
  onBaseCodeLoad = ::preloadParsedFiles
)
val parsedCache = HashMap<String, JsonNode>()

class JsFileLoader(file: File) : FileLoader(file, "js") {

  override fun traverse(visitor: (ce: VData, parent: VData?) -> Unit, parent: VData?) {
    visitor(data, parent)

    parsedCache[file.path]!!
      .at("/program/body")
      .asIterable()
      .map {
        traverseChilds(it, data, visitor)
      }
  }

  private fun traverseChilds(node: JsonNode, parent: VData?, visitor: (ce: VData, parent: VData?) -> Unit) {
    val ce = toJsNode(node, this, parent)
    visitor(ce, parent)
    node.astChildren().forEach {
      traverseChilds(it, ce, visitor)
    }
  }
}

/**
 * As Javascript parsing is calling a nodeJs process, parsing files in bundles is more efficient than parsing them one by one.
 * Because initializing nodejs everytime is expensive. If there is a limit on the amount of arguments tha can be passed
 * to the Javascrpt parser program we may need to split the files in batches.
 * Another more memory-efficient approach would be to make the JsParsing process interactive and use pipelines.
 */
fun preloadParsedFiles(path: File) {
  parsedCache.clear()

  val files = path.walkTopDown().filter { it.extension == "js" }.toList()

  if (files.isNotEmpty()) {
    val parsed = parseFiles(files)

    files.zip(parsed).forEach {
      parsedCache[it.first!!.path] = toJson(it.second)
    }
  }
}

fun toJson(js: String): JsonNode {
  return ObjectMapper().readTree(js)
}

fun parseFiles(files: List<File>): List<String> {
  val fileArgs = files.map { it.absolutePath }.joinToString(" ")
  val command = "node JavaScriptSupport.js ${fileArgs}"
  val e = Runtime.getRuntime().exec(command, null, File("frontend"))

  val programOutput = e.inputStream.bufferedReader().lines().toList()
  val programError = e.errorStream.bufferedReader().readText()

  if (programError.isNotEmpty())
    System.err.println(programError)

  return programOutput
}