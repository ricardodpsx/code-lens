package co.elpache.codelens.codeLoader

import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf
import java.io.File


abstract class FileLoader(val file: File, val lang: String) : NodeLoader {
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
    "code" to contents()
  )

  companion object {
    fun loadFile(path: String, parent: VData?, visitor: (node: VData, parent: VData?) -> Unit) {
      val file = File(path)
      languageSupportRegistry[file.extension]?.let {
        it.fileLoaderBuilder(file).traverse(visitor, parent)
      }
    }
  }
}