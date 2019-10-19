package co.elpache.codelens.codetree

import co.elpache.codelens.firstLine
import co.elpache.codelens.languages.js.buildJsFile
import co.elpache.codelens.languages.kotlin.buildKotlinFile
import java.io.File


val LanguageSupportRegistry = hashMapOf(
  "kt" to buildKotlinFile,
  "js" to buildJsFile
)

abstract class CodeEntity(val name: String? = null, val type: String) : CodeTreeNode() {

  init {
    data.addAll(
      "type" to type,
      "name" to name
    )
  }

}

abstract class LanguageCodeEntity(
  val astType: String,
  type: String,
  name: String? = null,
  val startOffset: Int,
  val endOffset: Int,
  val code: String
) : CodeEntity(name, type) {

  init {
    data.addAll(
      "astType" to astType,
      "name" to name,
      "type" to type,
      "startOffset" to startOffset,
      "endOffset" to endOffset
    )
  }

}


open class CodeFolder(val dir: File, val basePath: File = dir) : CodeEntity(dir.name, "dir") {
  val file = dir

  companion object {
    fun load(src: String): CodeFolder {
      val dir = File(src)
      if (dir.isAbsolute)
        return CodeFolder(dir, File(src))

      val path = System.getProperty("user.dir")
      return CodeFolder(File("$path/$src"), File(path))
    }
  }

  override fun expand() =
    dir.listFiles()
      .map {
        if (it.isDirectory)
          CodeFolder(it, basePath)
        else
          loadFile(it)
      }

  fun loadFile(file: File): CodeEntity {
    val buildAstFile = LanguageSupportRegistry[file.extension]
    return if (buildAstFile != null) buildAstFile(file)
    else CodeFile(file)
  }
}

typealias buildAstFile = (file: File) -> CodeFile

open class CodeFile(
  private val file: File,
  type: String = "file",
  astType: String = type
) : LanguageCodeEntity(
  type = type,
  astType = astType,
  name = file.nameWithoutExtension,
  startOffset = 0,
  endOffset = file.length().toInt(),
  code = file.readText()
) {

  open val lang = "rawText"
  val fileName: String = file.name

  var error: String = ""

  override fun expand() = emptyList<CodeEntity>()

  fun contents() = error + file.readText(Charsets.UTF_8)

  init {
    data.addAll(
      "startOffset" to startOffset,
      "fileName" to fileName,
      "endOffset" to endOffset,
      "type" to type,
      "lang" to lang
    )
  }

  override fun toString(): String {
    return file.path.toString()
  }
}