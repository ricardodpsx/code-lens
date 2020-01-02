package co.elpache.codelens.codeLoader

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf
import java.io.File


fun codeNodeBase(
  type: String,
  start: Int,
  end: Int,
  astType: String,
  name: String?,
  firstLine: String
): VData {
  return vDataOf(
    "type" to type,
    "start" to start,
    "end" to end,
    "astType" to astType,
    "name" to name,
    "firstLine" to firstLine
  )
}

class DefaultFileLoader(val file: File, val basePath: File) : NodeLoader {
  val codeTree = CodeTree()
  override fun load(): CodeTree {
    codeTree.addNode(file.path, fileData())
    codeTree.rootVid = file.path
    return codeTree;
  }

  fun fileData(): VData {
    return vDataOf(
      "fileName" to file.name,
      "name" to file.nameWithoutExtension,
      "extension" to file.extension,
      "type" to "file",
      "lang" to "raw",
      "path" to file.relativeTo(basePath).toString()
    )
  }
}

abstract class FileLoader(val file: File, val lang: String, val basePath: File) : NodeLoader {
  val type = "file"
  val fileName = file.name
  val name: String = file.nameWithoutExtension
  val startOffset: Int = 0
  val endOffset: Int = file.length().toInt()
  fun contents() = file.readText(Charsets.UTF_8)

  fun fileData(): VData {
    val code = file.readText(Charsets.UTF_8)
    return vDataOf(
      "fileName" to file.name,
      "name" to file.nameWithoutExtension,
      "extension" to file.extension,
      "type" to "file",
      "lang" to lang,
      "path" to file.relativeTo(basePath).toString(),
      "start" to 0,
      "end" to code.length,
      "code" to code
    )
  }

//  companion object {
//    fun loadFile(path: String, parent: VData?, visitor: (node: VData, parent: VData?) -> Unit, basePath: File) {
//      val file = File(path)
//      try {
//        //Todo: There is a duplicated use of languageSupportRegistry
//        languageSupportRegistry.entries.forEach {
//          if (path.evaluate(it.key.toRegex())) {
//            it.value.fileLoaderBuilder!!(file, basePath).traverse(visitor, parent)
//          }
//        }
//      } catch (e: Exception) {
//        throw RuntimeException("Problem loading file ${file.path}", e)
//      }
//    }
//  }
}