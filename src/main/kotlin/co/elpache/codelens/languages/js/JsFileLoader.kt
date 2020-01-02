package co.elpache.codelens.languages.js

import co.elpache.codelens.codeLoader.FileLoader
import co.elpache.codelens.codeLoader.LanguageIntegration
import co.elpache.codelens.codeLoader.ignorePatterns
import co.elpache.codelens.codeLoader.languageSupportRegistry
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import kotlin.streams.toList

fun jsInit() {
  languageSupportRegistry[".*\\.js"] = jsLanguageIntegration
}

val jsLanguageIntegration = LanguageIntegration(
  fileLoaderBuilder = ::JsFileLoader,
  applyMetrics = ::applyJsMetrics,
  onBaseCodeLoad = ::preloadParsedFiles
)

val parsedCache = ConcurrentHashMap<String, JsonNode>()

class JsFileLoader(file: File, basePath: File) : FileLoader(file, "js", basePath) {

  override fun load(): CodeTree {
    val parsed = parseFile(file).at("/program/body")
    val codeTree = CodeTree()

    val fileNode = codeTree.addNode(file.path, fileData())

    data class Item(val jsonNode: JsonNode, val parent: VData, val key: String? = null, val index: Int? = null)

    val list = LinkedList<Item>()
    list.addLast(Item(jsonNode = parsed, parent = fileNode, key = "body"))

    var i = 0
    while (list.isNotEmpty()) {
      val (jsonNode, parent, key, index) = list.removeFirst()

      val node = codeTree.addNode("${file.path}-$i")

      codeTree.addChild(parent.vid, node.vid)

      if (jsonNode.isArray)
        jsonNode.asSequence().withIndex().forEach {
          if (it.value.isValueNode)
            parent[it.index.toString()] = it.value.asText()
          else
            list.addLast(Item(it.value, node, index = it.index))
        }

      jsonNode.fields().forEach {
        if (it.value.isValueNode)
          node[it.key] = it.value.asText()
        else list.addLast(Item(it.value, node, key = it.key))
      }

      if (index != null) node["index"] = index
      if (key != null) node["type"] = key

      i++
    }
    //Todo: Make the rootVid the first added node by default
    codeTree.rootVid = fileNode.vid
    return codeTree
  }
}


fun parseFile(file: File): JsonNode {
  return parsedCache.computeIfAbsent(file.path) {
    toJson(parseFiles(listOf(file)).first())
  }
}

/**
 * As Javascript parsing is calling a nodeJs process, parsing files in bundles is more efficient than parsing them one by one.
 * Because initializing nodejs everytime is expensive. If there is a limit on the amount of arguments tha can be passed
 * to the Javascrpt parser program we may need to split the files in batches.
 * Another more memory-efficient approach would be to make the JsParsing process interactive and use pipelines.
 */
fun preloadParsedFiles(path: File) {
  //parsedCache.clear()

  //Todo: Using a recursive file traverser that avoids visiting unnecesary folders may be more efficient
  val files = path.walkTopDown()
    .filterNot { f ->
      ignorePatterns.any { f.path.matches(it.toRegex()) }
    }
    .filter { it.extension == "js" }.toList()

  if (files.isNotEmpty()) {

    files.chunked(50) {
      val parsed = parseFiles(it)
      it.zip(parsed).forEach {
        parsedCache[it.first.path] = toJson(it.second)
      }
    }
  }
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

fun toJson(js: String): JsonNode {
  return ObjectMapper().readTree(js)
}

