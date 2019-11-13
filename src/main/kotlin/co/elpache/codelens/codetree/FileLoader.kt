package co.elpache.codelens.codetree

import co.elpache.codelens.languages.js.buildJsFile
import co.elpache.codelens.languages.kotlin.buildKotlinFile
import co.elpache.codelens.tree.VData
import java.io.File

val LanguageSupportRegistry = hashMapOf(
  "kt" to buildKotlinFile,
  "js" to buildJsFile
)

typealias buildAstFile = (file: File, parent: VData?) -> FileLoader


open class FileLoader(
  val file: File,
  val type: String = "file",
  val astType: String = type,
  val lang: String,
  val name: String = file.nameWithoutExtension,
  val startOffset: Int = 0,
  val endOffset: Int = file.length().toInt()
) : CodeTreeLoader {

  override fun traverse(visitor: (node: VData, parent: VData?) -> Unit, parent: VData?) {}

  val code: String by lazy {
    contents()
  }

  val fileName: String = file.name

  var error: String = ""

  fun contents() = error + file.readText(Charsets.UTF_8)

  fun isNotEmpty() = contents().isNotEmpty()

  companion object {
    fun loadFile(path: String, parent: VData?, visitor: (node: VData, parent: VData?) -> Unit) {
      val file = File(path)
      val buildAstFile = LanguageSupportRegistry[file.extension]
      if (buildAstFile != null) buildAstFile(file, parent).traverse(visitor, parent)
    }
  }

  override fun toString(): String {
    return file.path.toString()
  }
}