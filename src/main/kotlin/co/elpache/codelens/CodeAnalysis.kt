package co.elpache.codelens

import co.elpache.codelens.languages.js.buildJsFile
import co.elpache.codelens.languages.kotlin.buildKotlinFile
import java.io.File


val LanguageSupportRegistry = hashMapOf(
  "kt" to buildKotlinFile,
  "js" to buildJsFile
)

fun List<CodeEntity>.data(): List<NodeData> = this.map { it.data() }

interface LanguageSupport {
  fun parseFile() {

  }
}

interface CodeEntity : CodeTreeNode {
  val name: String?
  val type: String
  override fun expand(): List<CodeEntity>
  override fun data() =
    nodeDataOf(
      "type" to type,
      "name" to name
    )
}

interface LanguageCodeEntity : CodeEntity {
  val startOffset: Int
  val endOffset: Int
  val code: String
}

class CodeBase(dir: File, basePath: File) : CodeFolder(dir, basePath) {
  override val name = dir.relativeTo(basePath).path.toString()

  companion object {
    fun load(src: String = "src/main"): CodeBase {
      val path = System.getProperty("user.dir")
      return CodeBase(File("$path/$src"), File(path))
    }
  }

  override val type = "root"

  fun files(namePattern: String = "**"): List<CodeFile> {
    return dir.walk()
      .filter { it.name.endsWith(".kt") }
      .filter { matches(it.name, namePattern) }
      .map { CodeFile(it) }
      .asIterable()
      .toList()
  }
}

open class CodeFolder(val dir: File, val basePath: File) : CodeEntity {
  override val name = dir.name
  override val type = "dir"
  val file = dir


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
  override val type: String = "file"
) : LanguageCodeEntity {

  open val lang = "rawText"
  override val name: String = file.name
  override val startOffset: Int = 0
  override val endOffset: Int by lazy { file.length().toInt() }
  override val code: String by lazy { file.readText() }

  override fun expand() = emptyList<CodeEntity>()

  fun contents() = file.readText(Charsets.UTF_8)

  override fun data() = super.data().and(
    "startOffset" to startOffset,
    "endOffset" to endOffset,
    "type" to type,
    "lang" to lang
  )

  override fun toString(): String {
    return name
  }
}