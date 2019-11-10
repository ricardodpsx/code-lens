package co.elpache.codelens.languages.kotlin

import co.elpache.codelens.codetree.CodeEntity
import co.elpache.codelens.codetree.CodeFile
import co.elpache.codelens.codetree.LangEntity
import co.elpache.codelens.codetree.buildAstFile
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
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffsetSkippingComments
import org.jetbrains.kotlin.psi.psiUtil.startsWithComment
import java.io.File


val buildKotlinFile: buildAstFile = { file: File -> KotlinFile(file) }

class KotlinFile(file: File) : CodeFile(file, lang = "kotlin") {

  override fun expand() = run {
    val text = file.readText()
    parseFile(text).children.map {
      toCodeEntity(it, this)
    }.flatten()
  }
}

class KotlinCodeEntity(
  name: String? = null,
  type: String, astType: String,
  val node: PsiElement,
  val line: Int,
  startOffset: Int,
  endOffset: Int,
  codeFile: CodeFile
) : LangEntity(
  name = name,
  type = type, astType = astType, startOffset = startOffset, endOffset = endOffset, codeFile = codeFile
) {

  override fun expand(): List<CodeEntity> {
    return node.children.map {
      toCodeEntity(it, codeFile!!)
    }.flatten()
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
  "classInitializer", "lambdaExpression", "constructor", "secondaryConstructor" -> "fun"
  "callExpression" -> "call" ////todo: Multitype's object contruction, how to differenciate?
  "importDirective" -> "import"
  "stringTemplate" -> "string"
  "integerConstant", "floatConstant", "doubleConstant" -> "number"
  "property" -> "binding"
  "BinaryExpression" -> "expression"
  "while", "doWhile", "for" -> "loop"
  else -> type
}

private fun toCodeEntity(c: PsiElement, codeFile: CodeFile): List<CodeEntity> {

  val name = when (c) {
    is KtClass -> c.name ?: "Anonymous"
    is KtDeclaration -> c.name ?: "Anonymous"
    is KtPackageDirective -> c.fqName.toString()
    is KtImportDirective -> c.importPath.toString()
    is KtAnnotationEntry, is KtTypeReference, is KtReferenceExpression,
    is KtConstantExpression, is KtStringTemplateExpression -> c.text
    else -> null
  }


  val k = KotlinCodeEntity(
    name = name,
    astType = c.node.elementType.toString().underscoreToCamel(),
    type = simplifyType(c.node.elementType.toString().underscoreToCamel()),
    line = c.node.startOffset,
    node = c,
    codeFile = codeFile,
    startOffset = c.startOffsetSkippingComments,
    endOffset = c.endOffset
  )

  k.data["firstLine"] = k.code.firstLine()
  k.data["code"] = k.code

  //Comments don't parse as nodes with this parser
  if (c.startsWithComment()) {
    val comm = KotlinCodeEntity(
      astType = "comment", type = "comment",
      startOffset = c.startOffset,
      endOffset = c.startOffsetSkippingComments,
      codeFile = codeFile,
      line = c.startOffset,
      node = c
    )

    comm.data["firstLine"] = comm.code.firstLine()
    comm.data["code"] = comm.code
    return listOf(comm).plus(k)
  }

  return listOf(k)
}

