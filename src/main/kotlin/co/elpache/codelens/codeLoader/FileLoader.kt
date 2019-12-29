package co.elpache.codelens.codeLoader

import co.elpache.codelens.firstLine
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf
import java.io.File


fun codeNodeBase(type: String, start: Int, end: Int, astType: String, file: FileLoader, name: String?): VData {
  val code = file.contents().substring(start, end)
  return vDataOf(
    "type" to type,
    "startOffset" to start,
    "endOffset" to end,
    "astType" to astType,
    "firstLine" to code.firstLine(),
    "name" to name
  )
}

abstract class FileLoader(val file: File, lang: String, basePath: File) : NodeLoader {
  val type = "file"
  val fileName = file.name
  val name: String = file.nameWithoutExtension
  val startOffset: Int = 0
  val endOffset: Int = file.length().toInt()
  fun contents() = file.readText(Charsets.UTF_8)

  val data = vDataOf(
    "startOffset" to startOffset,
    "fileName" to fileName,
    "name" to file.nameWithoutExtension,
    "extension" to file.extension,
    "endOffset" to endOffset,
    "type" to type,
    "lang" to lang,
    "code" to contents(),
    "path" to file.relativeTo(basePath).toString()
  )

  companion object {
    fun loadFile(path: String, parent: VData?, visitor: (node: VData, parent: VData?) -> Unit, basePath: File) {
      val file = File(path)
      try {


        //Todo: There is a duplicated use of languageSupportRegistry
        languageSupportRegistry.entries.forEach {
          if (path.matches(it.key.toRegex())) {
            it.value.fileLoaderBuilder(file, basePath).traverse(visitor, parent)
          }
        }
      } catch (e: Exception) {
        throw RuntimeException("Problem loading file ${file.path}", e)
      }
    }
  }
}