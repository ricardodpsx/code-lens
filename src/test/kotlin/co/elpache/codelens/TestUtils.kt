package co.elpache.codelens

import co.elpache.codelens.tree.Tree
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.join
import org.assertj.core.api.Assertions.assertThat
import java.lang.StringBuilder


class X

fun readTestFile(file: String) = X::class.java.classLoader.getResource(file).readText()


fun codeTreeNode(vararg data: Pair<String, Any>) =
  object : CodeTreeNode {
    override fun data(): NodeData = HashMap(data.toMap())
    override fun expand(): List<CodeTreeNode> = emptyList()
    override fun toString() = data().toString()

  }

fun compareTreeOutputs(a: CodeTree, b: String) = assertThat(printTree(a).normalize()).isEqualTo(b.normalize())

fun String.normalize () = this.replace(Regex("\t+"), " ").replace(Regex(" +")," ").trim();


fun codeTree(vid: String, node: CodeTreeNode, vararg expands: CodeTree): CodeTree {
  var tree = CodeTree()
  tree = tree.addIfAbsent(vid, node)
  tree.rootVid = vid
  expands.forEach {
    tree = join(tree, it)
  }
  return tree
}

fun tree(vid: String, vararg expands: Tree<String>): Tree<String> {
  var tree = Tree<String>()
  tree = tree.addIfAbsent(vid, vid)
  tree.rootVid = vid
  expands.forEach {
    tree = join(tree, it)
  }
  return tree
}


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

fun selectCode(
  tree: CodeTree,
  cssSelector: String
): List<Map<String, Any>> {
  return UseCases(tree).selectCodeWithParents(cssSelector).results.map {
    tree.v(it).data()
  }
}