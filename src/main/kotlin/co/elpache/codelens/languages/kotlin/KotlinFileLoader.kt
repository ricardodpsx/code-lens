package co.elpache.codelens.languages.kotlin

import co.elpache.codelens.codeLoader.FileLoader
import co.elpache.codelens.codeLoader.LanguageIntegration
import co.elpache.codelens.tree.VData
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import java.io.File


val kotlinLanguageIntegration = LanguageIntegration(
  fileLoaderBuilder = ::KotlinFileLoader,
  applyMetrics = ::applyKotlinMetrics
)

class KotlinFileLoader(file: File) : FileLoader(file, "kotlin") {
  override fun traverse(visitor: (node: VData, parent: VData?) -> Unit, parent: VData?) {
    visitor(data, parent)
    parseFile(file.readText()).children.forEach { traverse(it, data, visitor) }
  }

  private fun traverse(node: PsiElement, parent: VData?, visitor: (node: VData, parent: VData?) -> Unit) {
    val data = toCodeEntity(node, this)
    visitor(data, parent)
    node.children.forEach {
      traverse(it, data, visitor)
    }
  }
}


private fun parseFile(code: String): KtFile {
  val proj by lazy {
    KotlinCoreEnvironment.createForProduction(
      Disposer.newDisposable(),
      CompilerConfiguration(),
      EnvironmentConfigFiles.JVM_CONFIG_FILES
    ).project
  }
  return PsiManager.getInstance(proj).findFile(
    LightVirtualFile(
      "temp.kt",
      KotlinFileType.INSTANCE,
      code
    )
  ) as KtFile

}

