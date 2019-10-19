package co.elpache.codelens.codetree

import co.elpache.codelens.tree.Tree
import co.elpache.codelens.tree.Vid
import co.elpache.codelens.tree.buildTreeFromChildren
import co.elpache.codelens.tree.subTree
import co.elpachecode.codelens.cssSelector.EmptyResult
import co.elpachecode.codelens.cssSelector.NodeResult
import java.lang.StringBuilder
import kotlin.math.max

class CodeTree(val tree: Tree<CodeTreeNode> = Tree()) {
  fun children(vid: String) = tree.children(vid).map {
    tree.v(it) as CodeEntity
  }

  fun file(vid: Vid) = tree.v(vid) as CodeFile

  fun data(vid: Vid) = tree.v(vid).data

  fun node(vid: Vid) = tree.v(vid)


  fun treeFromChildren(children: List<Vid>) =
    CodeTree(buildTreeFromChildren(tree, children))


  fun toMap(): Map<String, Any> {
    return tree.vertices.map {
      it.key to mapOf(
        "data" to it.value.node.data,
        "parent" to it.value.parentVid,
        "children" to it.value.children.toList()
      )
    }.toMap().plus("rootVid" to tree.rootVid!!).toMap()
  }

  fun subTreeFrom(vid: Vid) = CodeTree(subTree(tree, vid))

  fun byType(type: String) =
    tree.vertices
      .filter { it.value.node is LanguageCodeEntity }
      .map { it.key to it.value.node as LanguageCodeEntity }
      .filter { it.second.type == type }
      .map { NodeResult(it.first, this) }

  fun find(css: String) = NodeResult(tree.rootVid(), this).find(css)


  //Todo: Test tree expansion
  fun expandFullCodeTree(node: CodeTreeNode): CodeTree =
    _expandTreeNode(node)


  fun depth(vid: Vid): Int {
    var maxDepth = 0
    for (cVid in tree.children(vid))
      maxDepth = max(depth(cVid), maxDepth)

    return (if (increasesNesting((tree.v(vid) as CodeEntity).type)) 1 else 0) + maxDepth
  }

  fun applyAnalytics(): CodeTree {

    byType("call").forEach {
      it.data["args"] = it.first("args").children("arg").size
    }

    byType("fun").forEach {
      it.data["textLines"] = it.code.split("\n").size
      it.data["lines"] = it.code.relevantCodeLines() - 1
      it.data["depth"] = depth(it.vid) - 1
      it.data["params"] = it.first("params").children("param").size
    }

    byType("file").forEach {
      it.data["lines"] = it.code.relevantCodeLines()
      it.data["textLines"] = it.code.split("\n").size
      it.data["functions"] = it.children("fun").size
      it.data["classes"] = it.children("class").size
      it.data["bindings"] = it.children("binding").size
    }

    byType("class").forEach {
      it.data["lines"] = it.code.relevantCodeLines()

      var body = it.first("ClassBody").first("body")
      it.data["constructors"] = body.children("fun[kind='constructor']").size
      it.data["methods"] = body.children("fun").size
      it.data["properties"] = body.children("binding").size
      it.data["members"] = it.code.relevantCodeLines()
    }

    return this
  }

  private fun _expandTreeNode(
    node: CodeTreeNode,
    ids: HashSet<Vid> = HashSet(),
    parent: Vid? = null
  ): CodeTree {

    val vid = ids.size.toString()

    if (ids.contains(vid)) throw error("Duplicated Id $vid")

    ids.add(vid)

    //val vid = UUID.randomUUID().toString()
    tree.addIfAbsent(vid, node)

    if (parent != null)
      tree.addChild(parent, vid)

    node.expand().forEach {
      _expandTreeNode(it, ids, vid)
    }

    tree.rootVid = vid
    return this
  }

  fun printTree(): String {
    val out = StringBuilder()
    val root = tree.v(tree.rootVid())
    out.append("${tree.rootVid}: ${root.data}\n")
    dfs(tree.rootVid(), "-", out)
    return out.toString()
  }

  private fun dfs(vid: Vid, tab: String, out: StringBuilder) {
    for (cVid in tree.children(vid)) {
      val child = tree.v(cVid)
      out.append(" $tab ${cVid}: ${child.data}\n")
      dfs(cVid, "$tab-", out)
    }
  }

}


fun increasesNesting(type: String) = listOf("fun", "if", "loop").contains(type)


fun String.relevantCodeLines() =
  split("\n")
    .map { it.trim() }
    .filter { !it.isBlank() }
    .filter { it.length > 1 }
    .size