package co.elpache.codelens.languages.kotlin

import co.elpache.codelens.codeLoader.codeNodeBase
import co.elpache.codelens.tree.VData
import co.elpache.codelens.underscoreToCamel
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffsetSkippingComments

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

internal fun toCodeEntity(c: PsiElement, fileLoader: KotlinFileLoader): VData {

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
    file = fileLoader
  )

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