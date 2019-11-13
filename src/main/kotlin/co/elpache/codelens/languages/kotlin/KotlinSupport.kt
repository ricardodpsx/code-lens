package co.elpache.codelens.languages.kotlin

import co.elpache.codelens.codetree.FileLoader
import co.elpache.codelens.codetree.buildAstFile
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


val buildKotlinFile: buildAstFile = { file: File, parent: VData? ->
  KotlinFileLoader(file, parent)
}


fun traverse(node: PsiElement, fileLoader: FileLoader, parent: VData?, visitor: (node: VData, parent: VData?) -> Unit) {
  val data = toCodeEntity(node, fileLoader)
  visitor(data, parent)
  node.children.forEach {
    traverse(it, fileLoader, data, visitor)
  }
}

class KotlinFileLoader(file: File, parent: VData?) : FileLoader(file, lang = "kotlin") {
  override fun traverse(visitor: (node: VData, parent: VData?) -> Unit, parent: VData?) {
    val data = vDataOf(
      "language" to lang,
      "startOffset" to startOffset,
      "fileName" to fileName,
      "name" to file.nameWithoutExtension,
      "endOffset" to endOffset,
      "type" to type,
      "lang" to lang,
      "code" to contents()
    )
    visitor(data, parent)

    parseFile(file.readText()).children.forEach { traverse(it, this, data, visitor) }
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

private fun toCodeEntity(c: PsiElement, fileLoader: FileLoader): VData {

  val name = when (c) {
    is KtClass -> c.name ?: "Anonymous"
    is KtDeclaration -> c.name ?: "Anonymous"
    is KtPackageDirective -> c.fqName.toString()
    is KtImportDirective -> c.importPath.toString()
    is KtAnnotationEntry, is KtTypeReference, is KtReferenceExpression,
    is KtConstantExpression, is KtStringTemplateExpression -> c.text
    else -> null
  }

  val data = vDataOf(
    "name" to name,
    "astType" to simplifyType(c.node.elementType.toString().underscoreToCamel()),
    "type" to simplifyType(c.node.elementType.toString().underscoreToCamel()),
    "line" to c.node.startOffset,
    "startOffset" to c.startOffsetSkippingComments,
    "endOffset" to c.endOffset
  )

  val code = fileLoader.contents().substring(data.getInt("startOffset"), data.getInt("endOffset"))
  data["firstLine"] = code.firstLine()
  data["code"] = code

  return data
  //Comments don't parse as nodes with this parser
//  if (c.startsWithComment()) {
//
//    val comm = vDataOf(
//      "astType" to "comment",
//      "type" to "comment",
//      "startOffset" to c.startOffset,
//      "endOffset" to c.startOffsetSkippingComments,
//      "codeFile" to codeFile,
//      "line" to c.startOffset,
//      "node" to c
//    )
//    val commCode = codeFile.contents().substring(comm.getInt("startOffset"), comm.getInt("endOffset"))
//    comm["firstLine"] = commCode.firstLine()
//    comm["code"] = commCode
//    return listOf(comm).plus(data)
//  }
//
//  return listOf(data)
}

