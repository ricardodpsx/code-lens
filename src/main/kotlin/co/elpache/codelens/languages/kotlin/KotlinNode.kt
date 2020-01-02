package co.elpache.codelens.languages.kotlin

import co.elpache.codelens.codeLoader.codeNodeBase
import co.elpache.codelens.firstLine
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
  "eolComment" -> "comment"
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
    type = simplifyType(astType),
    start = c.startOffsetSkippingComments,
    end = c.endOffset,
    astType = astType,
    name = name,
    firstLine = c.text.firstLine()
  )
}