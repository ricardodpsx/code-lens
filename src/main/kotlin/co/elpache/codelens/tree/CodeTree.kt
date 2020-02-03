package co.elpache.codelens.tree

import org.jetbrains.kotlin.backend.common.pop
import java.util.LinkedList
import java.util.TreeMap

open class CodeTree {

  val vertices: TreeMap<Vid, Vertice> = TreeMap()

  private var rootVid: Vid? = null

  fun rootVid() = rootVid ?: error("RootNode not Set, Did you forgot to add a root node?")


  fun addIfAbsent(data: Vertice): Vertice {
    if(rootVid == null) rootVid = data.vid
    if (contains(data.vid)) return data
    vertices[data.vid] = data.clone()
    return data
  }

  fun children(vid: Vid) = adj(vid, "children")

  fun addChild(from: Vid, to: Vid): CodeTree {
    addRelation("children", from, to)
    addRelation("parent", to, from)
    return this

  }

  fun addChild(from: Vid, node: Vertice): CodeTree {
    assert(from != node.vid)
    addIfAbsent(node)
    addRelation("children", from, node.vid)
    addRelation("parent", node.vid, from)
    return this
  }

  fun descendants(vid: Vid, descendantList: ArrayList<Vid> = arrayListOf()): List<Vid> {
    for (cVid in children(vid)) {
      descendantList.add(cVid)
      descendants(cVid, descendantList)
    }
    return descendantList
  }

  fun subTree(vid: Vid): CodeTree {
    val ct = CodeTree()
    ct.rootVid = vid
    val items = ArrayList<Vid>()
    items.add(vid)
    while (items.isNotEmpty()) {
      val p = items.pop()
      ct.vertices[p] = vertices[p]!!
      children(p).forEach {
        items.add(it)
      }
    }
    return ct
  }


  fun asString(): String {
    val out = StringBuilder()
    val root = v(rootVid()).plus("code" to "<Excluded>").minus("vid")
    out.append("${root}\n")
    dfs(rootVid(), "-", out)
    return out.toString()
  }

  private fun dfs(vid: Vid, tab: String, out: StringBuilder) {
    for (cVid in children(vid)) {
      val child = v(cVid).minus("code").minus("vid")

      out.append(" $tab ${child}\n")
      dfs(cVid, "$tab-", out)
    }
  }

  fun ancestors(vid: String): List<Vid> {
    val list = LinkedList<Vid>()
    var p: Vid? = vid
    while (p != null) {
      list.add(p)
      p = parent(p)
    }
    return list.toList()
  }


  fun treeFromChildren(children: List<Vid>): CodeTree {
    var resTree = CodeTree()

    children.forEach { vid ->
      ancestors(vid).reversed()
        .windowed(2, partialWindows = true).forEach {
          resTree.addIfAbsent(v(it[0]))
          if (it.size == 2) resTree = resTree.addChild(it[0], v(it[1]))
        }
    }
    resTree.rootVid = rootVid
    return resTree
  }

  fun toMap(): Map<String, Any> {
    return vertices.map {
      it.key to mapOf(
        "vertice" to v(it.key).minus("code"),
        "parent" to adj(it.key, "parent").getOrNull(0),
        "children" to adj(it.key, "children").toList()
      )
    }.toMap().plus("rootVid" to rootVid!!).toMap()
  }

  fun v(vid: Vid): Vertice = vertice(vid)

  fun parentNode(b: Vid) = parent(b)?.let { v(parent(b)!!) }

  fun parent(b: Vid) = adj(b, "parent").getOrNull(0)

  private fun vertice(vid: Vid) = vertices[vid] ?: error("Vertice $vid not found")

  fun contains(vid: Vid) = vertices.contains(vid)

  fun addSubTree(subTree: CodeTree, to: Vid): CodeTree {
    val expectedSize = subTree.vertices.size + vertices.size
    vertices.putAll(subTree.vertices)
    addChild(to, subTree.rootVid())

    if (vertices.size != expectedSize)
      throw Error("The trees should be disjoint")

    return this
  }

  fun addRelation(name: String, from: Vid, to: Vid) {
    vertices[from]!!.relations.add(Edge(name, to))
  }

  fun adj(vid: String): List<Vid> =
    vertices[vid]!!.relations.map { it.to }.plus(children(vid))

  fun adj(vid: String, relName: String): List<Vid> =
    vertices[vid]!!.relations.filter { it.name == relName }.map { it.to }


  fun inorder(): List<Vid> {
    val out = mutableListOf<Vertice>()
    out.add(v(rootVid!!))
    dfs(rootVid!!, out)
    return out.map { it["value"] as String }
  }

  private fun dfs(vid: Vid, out: MutableList<Vertice>) {
    for (cVid in children(vid)) {
      out.add(v(cVid))
      dfs(cVid, out)
    }
  }

  fun join(child: CodeTree) {
    assert(vertices.keys.intersect(child.vertices.keys).isEmpty()) { "Trees should be disjoint" }
    vertices.putAll(child.vertices)
    addChild(rootVid!!, child.rootVid!!)
  }
}


