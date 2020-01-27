package co.elpache.codelens.extensions.kotlin

import co.elpache.codelens.codeLoader.FileLoader
import co.elpache.codelens.codeLoader.LanguageIntegration
import co.elpache.codelens.firstLine
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf
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


val kotlinLanguageIntegration = LanguageIntegration(
  fileLoaderBuilder = ::KotlinFileLoader,
  applyMetrics = ::applyKotlinMetrics,
  filePattern = ".*\\.kt"
)

class KotlinFileLoader(file: File, basePath: File) : FileLoader<PsiElement>(file, "kotlin", basePath) {

  override fun getChildren(node: PsiElement) =
    (node as PsiElement).children.withIndex().associateBy({ it.index.toString() }, { it.value })

  override fun getValues(node: PsiElement) = toVertice(node as PsiElement)

  override fun parseFile() = parseFile(file.readText())
}

internal fun toVertice(c: PsiElement): VData {
  val astType = c.node.elementType.toString().underscoreToCamel()

  return vDataOf(
    "type" to simplifyType(astType),
    "start" to c.startOffsetSkippingComments,
    "end" to c.endOffset,
    "astType" to astType,
    "name" to getName(c),
    "firstLine" to c.text.firstLine()
  )
}

private fun getName(c: PsiElement): String? {
  val name = when (c) {
    is KtClass -> c.name ?: "Anonymous"
    is KtDeclaration -> c.name ?: "Anonymous"
    is KtPackageDirective -> c.fqName.toString()
    is KtImportDirective -> c.importPath.toString()
    is KtAnnotationEntry, is KtTypeReference, is KtReferenceExpression,
    is KtConstantExpression, is KtStringTemplateExpression -> c.text
    else -> null
  }
  return name
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

fun simplifyType(type: String) = when (type) {
  "valueParameter" -> "param"
  "valueParameterList" -> "params"
  "valueArgumentList" -> "args"
  "valueArgument" -> "arg"
  "classInitializer", "lambdaExpression", "constructor", "secondaryConstructor" -> "fun"
  "callExpression" -> "call" ////todo: Multitype's object contruction, how to differenciate?
  "importDirective" -> "import"
  "stringTemplate" -> "string"
  "integerConstant", "floatConstant", "doubleConstant" -> "number"
  "property" -> "binding"
  "BinaryExpression" -> "expression"
  "eolComment" -> "comment"
  "while", "doWhile", "for" -> "loop"
  else -> type
}
