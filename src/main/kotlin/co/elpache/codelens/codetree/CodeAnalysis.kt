package co.elpache.codelens.codetree

import co.elpache.codelens.languages.js.buildJsFile
import co.elpache.codelens.languages.kotlin.buildKotlinFile
import co.elpache.codelens.tree.VData
import java.io.File


val LanguageSupportRegistry = hashMapOf(
  "kt" to buildKotlinFile,
  "js" to buildJsFile
)

abstract class CodeEntity(val name: String? = null, val type: String) {

  val data = VData()

  init {
    data.addAll(
      "type" to type,
      "name" to name
    )
  }

  abstract fun expand(): List<CodeEntity>
}


typealias buildAstFile = (file: File) -> CodeFile

