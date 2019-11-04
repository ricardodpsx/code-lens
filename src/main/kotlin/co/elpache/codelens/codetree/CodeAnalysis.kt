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


typealias buildAstFile = (file: File) -> CodeFile

