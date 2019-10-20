package co.elpache.codelens.codetree

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
  val codeFile: CodeFile? = null
) : CodeEntity(name, type) {


  open val code: String
    get() =
      if (codeFile!!.isNotEmpty()
        && startOffset <= endOffset
        && startOffset < codeFile.code.length
        && endOffset <= codeFile.code.length)
        codeFile.code.substring(startOffset, endOffset)
      else {
        System.err.println("Problem with node $data")
        ""
      }

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

  init {
    data.addAll(
      "fileName" to dir.name
    )
  }

  override fun expand() =
    try {
      dir.listFiles()
        .map { if (it.isDirectory) CodeFolder(it, basePath) else loadFile(it) }.filterNotNull()
    } catch (e: Exception) {
      throw RuntimeException("problem opening directory ${dir.absolutePath}", e)
    }
  private fun loadFile(file: File): CodeEntity? {
    val buildAstFile = LanguageSupportRegistry[file.extension]
    return if (buildAstFile != null) buildAstFile(file) else null
  }
}

typealias buildAstFile = (file: File) -> CodeFile

open class CodeFile(
  private val file: File,
  type: String = "file",
  astType: String = type,
  val lang: String
) : LanguageCodeEntity(
  type = type,
  astType = astType,
  name = file.nameWithoutExtension,
  startOffset = 0,
  endOffset = file.length().toInt()
) {

  override val code: String by lazy { contents() }

  val fileName: String = file.name

  var error: String = ""

  override fun expand() = emptyList<CodeEntity>()

  fun contents() = error + file.readText(Charsets.UTF_8)

  fun isNotEmpty() = contents().isNotEmpty()

  init {
    data.addAll(
      "language" to lang,
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