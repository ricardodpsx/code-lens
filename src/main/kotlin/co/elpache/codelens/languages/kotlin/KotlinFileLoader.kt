package co.elpache.codelens.languages.kotlin

import co.elpache.codelens.codeLoader.FileLoader
import co.elpache.codelens.codeLoader.LanguageIntegration
import co.elpache.codelens.codeLoader.codeNodeBase
import co.elpache.codelens.codeLoader.languageSupportRegistry
import co.elpache.codelens.firstLine
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.VData
import co.elpache.codelens.underscoreToCamel
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffsetSkippingComments
import java.io.File
import java.util.LinkedList


fun kotlinInit() {
  languageSupportRegistry[".*\\.kt"] = kotlinLanguageIntegration
}

val kotlinLanguageIntegration = LanguageIntegration(
  fileLoaderBuilder = ::KotlinFileLoader,
  applyMetrics = ::applyKotlinMetrics
)

class KotlinFileLoader(file: File, basePath: File) : FileLoader(file, "kotlin", basePath) {
  override fun load(): CodeTree {
    val codeTree = CodeTree()
    val parsed = parseFile(file.readText())
    val fileNode = codeTree.addNode(file.path, fileData())

    data class Item(val node: PsiElement, val parent: VData)

    val list = LinkedList<Item>()
    list.addLast(Item(node = parsed, parent = fileNode))

    var i = 0
    while (list.isNotEmpty()) {
      val (node, parent) = list.removeFirst()

      val newNode = codeTree.addNode("${file.path}-$i", toVertice(node))
      codeTree.addChild(parent.vid, newNode.vid)

      node.children.forEach {
        list.add(Item(it, newNode))
      }
      i++
    }
    codeTree.rootVid = fileNode.vid
    return codeTree
  }

}

internal fun toVertice(c: PsiElement): VData {

  val name = when (c) {
    is KtClass -> c.name ?: "Anonymous"
    is KtDeclaration -> c.name ?: "Anonymous"
    is KtPackageDirective -> c.fqName.toString()
    is KtImportDirective -> c.importPath.toString()
    is KtAnnotationEntry, is KtTypeReference, is KtReferenceExpression,
    is KtConstantExpression, is KtStringTemplateExpression -> c.text
    else -> null
  }
  val astType = c.node.elementType.toString().underscoreToCamel()

  return codeNodeBase(
    name = name,
    astType = astType,
    type = simplifyType(astType),
    start = c.startOffsetSkippingComments,
    end = c.endOffset,
    firstLine = c.text.firstLine()
  )
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

