package co.elpache.codelens.codetree

import java.io.File

open class CodeFile(
  val file: File,
  type: String = "file",
  astType: String = type,
  val lang: String
) : LangEntity(
  type = type,
  astType = astType,
  name = file.nameWithoutExtension,
  startOffset = 0,
  endOffset = file.length().toInt()
) {

  override val code: String by lazy {
    contents()
  }

  val fileName: String = file.name

  var error: String = ""

  override fun expand() = emptyList<CodeEntity>()

  fun contents() = error + file.readText(Charsets.UTF_8)

  fun isNotEmpty() = contents().isNotEmpty()

  companion object {
    fun loadFile(path: String): CodeEntity? {
      val file = File(path)
      val buildAstFile = LanguageSupportRegistry[file.extension]
      return if (buildAstFile != null) buildAstFile(file) else null
    }
  }

  override fun getCodes(): String {
    return contents()
  }

  init {
    data.addAll(
      "language" to lang,
      "startOffset" to startOffset,
      "fileName" to fileName,
      "endOffset" to endOffset,
      "type" to type,
      "lang" to lang,
      "code" to contents()
    )
  }

  override fun toString(): String {
    return file.path.toString()
  }
}