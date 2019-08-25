package co.elpache.codelens.languages.kotlin

import co.elpache.codelens.CodeEntity
import co.elpache.codelens.CodeFile
import co.elpache.codelens.CodeTree
import co.elpache.codelens.LanguageCodeEntity
import co.elpache.codelens.and
import co.elpache.codelens.buildAstFile
import co.elpache.codelens.depth
import co.elpache.codelens.relevantCodeLines
import co.elpache.codelens.search
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
import java.io.File


val buildKotlinFile: buildAstFile = { file: File -> KotlinFile(file) }

class KotlinFile(val file: File) : CodeFile(file) {
  override val lang: String = "kotlin"

  override fun expand() =
    parseFile(file.readText()).children.map {
      toCodeEntity(it)
    }
}

data class KotlinCodeEntity(
  override val name: String,
  override val type: String,
  val node: PsiElement,
  val line: Int
) : LanguageCodeEntity {
  override val startOffset: Int = node.startOffset
  override val endOffset: Int = node.endOffset

  override val code by lazy { node.text }

  override fun expand() =
    node.children.map {
      toCodeEntity(it)
    }

  val data = super.data().and(
    "startOffset" to node.startOffset,
    "endOffset" to node.endOffset,
    "textLines" to node.text.split("\n").size,
    "childrenCount" to node.children.size
  )

  override fun data() = data
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

private fun toCodeEntity(c: PsiElement): CodeEntity {
  val base = KotlinCodeEntity(
    name = "",
    type = c.node.elementType.toString().underscoreToCamel(),
    line = c.node.startOffset,
    node = c
  )
  return when (c) {
    is KtClass -> base.copy(name = c.name ?: "Anonymous")
    is KtDeclaration -> base.copy(name = c.name ?: "Anonymous")
    is KtPackageDirective -> base.copy(name = c.fqName.toString())
    is KtImportDirective -> base.copy(name = c.importPath.toString())
    is KtAnnotationEntry, is KtTypeReference, is KtReferenceExpression,
    is KtConstantExpression, is KtStringTemplateExpression -> base.copy(name = c.text)
    else -> base
  }
}

//Todo: Analytics must happen on Demand
fun applyAnalytics(tree: CodeTree): CodeTree {

  search(tree, "fun").forEach {
    //Lines
    val function = tree.v(it) as LanguageCodeEntity

    //For one line functions
    function.data()["lines"] = relevantCodeLines(function.code)
    //Depth
    function.data()["depth"] = depth(tree, it)

    search(tree, "block", it).getOrNull(0)?.let {
      val block = tree.v(it) as LanguageCodeEntity
      function.data()["lines"] = relevantCodeLines(block.code)
    }

    //Arguments
    search(tree, "valueParameterList", it).getOrNull(0)?.let {
      function.data()["argumentCount"] = (tree.v(it) as LanguageCodeEntity).data()["childrenCount"]!!
    }

  }
  return tree
}