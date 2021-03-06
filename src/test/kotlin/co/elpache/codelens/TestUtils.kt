package co.elpache.codelens

import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.Vertice
import org.assertj.core.api.Assertions.assertThat


class X

fun readTestFile(file: String) = X::class.java.classLoader.getResource(file).readText()


fun compareTreeOutputs(a: CodeTree, b: String) =
  assertThat(a.asString().normalize()).isEqualTo(b.normalize())

fun String.normalize () = this.replace(Regex("\t+"), " ").replace(Regex(" +")," ").trim();



