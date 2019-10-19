package co.elpache.codelens.languages.kotlin

import co.elpache.codelens.codetree.CodeEntity
import co.elpache.codelens.codetree.CodeFile
import co.elpache.codelens.codetree.LanguageCodeEntity
import co.elpache.codelens.codetree.addAll
import co.elpache.codelens.codetree.buildAstFile
import co.elpache.codelens.codetree.relevantCodeLines
import co.elpache.codelens.firstLine
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
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import java.io.File


val buildKotlinFile: buildAstFile = { file: File -> KotlinFile(file) }

class KotlinFile(val file: File) : CodeFile(file) {
  override val lang: String = "kotlin"

  override fun expand() =
    parseFile(file.readText()).children.map {
      toCodeEntity(it)
    }
}

class KotlinCodeEntity(
  name: String?,
  type: String, astType: String,
  val node: PsiElement,
  val line: Int,
  startOffset: Int,
  endOffset: Int,
  code: String
) : LanguageCodeEntity(
  name = name,
  type = type, astType = astType, startOffset = startOffset, endOffset = endOffset, code = code
) {

  override fun expand() =
    node.children.map {
      toCodeEntity(it)
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

//block, fun, class, if
fun simplifyType(type: String) = when (type) {
  "valueParameter" -> "param"
  "valueParameterList" -> "params"
  "valueArgumentList" -> "args"
  "valueArgument" -> "arg"
  "classInitializer" -> "fun"
  "callExpression" -> "call" ////todo: Multitype's object contruction, how to differenciate?
  "importDirective" -> "import"
  "property" -> "binding"
  "lambdaExpression" -> "fun"
  "BinaryExpression" -> "expression"
  "while", "doWhile", "for" -> "loop"
  else -> type
}

private fun toCodeEntity(c: PsiElement): CodeEntity {

  val name = when (c) {
    is KtClass -> c.name ?: "Anonymous"
    is KtDeclaration -> c.name ?: "Anonymous"
    is KtPackageDirective -> c.fqName.toString()
    is KtImportDirective -> c.importPath.toString()
    is KtAnnotationEntry, is KtTypeReference, is KtReferenceExpression,
    is KtConstantExpression, is KtStringTemplateExpression -> c.text
    else -> null
  }

  return KotlinCodeEntity(
    name = name,
    astType = c.node.elementType.toString().underscoreToCamel(),
    type = simplifyType(c.node.elementType.toString().underscoreToCamel()),
    line = c.node.startOffset,
    node = c,
    code = c.text,
    startOffset = c.startOffset,
    endOffset = c.endOffset
  )

}

