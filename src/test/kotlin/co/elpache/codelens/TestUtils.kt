package co.elpache.codelens

import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.tree.Tree
import org.assertj.core.api.Assertions.assertThat


class X

fun readTestFile(file: String) = X::class.java.classLoader.getResource(file).readText()


fun compareTreeOutputs(a: CodeTree, b: String) =
  assertThat(a.asString().normalize()).isEqualTo(b.normalize())

fun String.normalize () = this.replace(Regex("\t+"), " ").replace(Regex(" +")," ").trim();


fun <T> inorder(codeTree: Tree<T>): List<T> {
  val out = mutableListOf<T>()
  out.add(codeTree.root())
  dfs(codeTree.rootVid(), codeTree, out)
  return out
}


fun <T> dfs(vid: String, tree: Tree<T>, out: MutableList<T>) {
  for (cVid in tree.children(vid)) {
    out.add(tree.v(cVid))
    dfs(cVid, tree, out)
  }
}

