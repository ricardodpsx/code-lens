package co.elpache.codelens.tree

import org.jetbrains.kotlin.backend.common.pop
import java.util.LinkedList
import java.util.TreeMap

open class CodeTree {

  val vertices: TreeMap<Vid, Vertice>

  constructor(vertices: Map<Vid, Vertice>) {
    this.vertices = TreeMap()
    this.vertices.putAll(vertices.map { it.key to it.value.clone() })
  }

  constructor() {
    this.vertices = TreeMap()
  }

  var rootVid: Vid? = null

  fun rootVid() = rootVid ?: error("RootNode not Set, Did you forgot to add a root node?")


  fun root(): VData = v(rootVid())

  fun addIfAbsent(id: String, data: VData): CodeTree {
    if (contains(id)) return this
    vertices[id] = Vertice(id, data)
    return this;
  }

  fun children(vid: Vid) = vertices[vid]!!.children

  fun addChild(from: Vid, to: Vid): CodeTree {
    vertices[from]!!.children.add(to)
    vertices[to]!!.parent = from
    return this

  }

  fun addChild(from: Vid, to: Vid, node: VData): CodeTree {
    assert(from != to)
    addIfAbsent(to, node)
    vertices[to]!!.parent = from
    vertices[from]!!.children.add(to)
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
    val root = v(rootVid()).plus("code" to "<Excluded>")
    out.append("${rootVid}: ${root}\n")
    dfs(rootVid(), "-", out)
    return out.toString()
  }

  private fun dfs(vid: Vid, tab: String, out: StringBuilder) {
    for (cVid in children(vid)) {
      val child = v(cVid).minus("code")

      out.append(" $tab ${cVid}: ${child}\n")
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
          resTree = resTree.addIfAbsent(it[0], v(it[0]))
          if (it.size == 2) resTree = resTree.addChild(it[0], it[1], v(it[1]))
        }
    }
    resTree.rootVid = rootVid
    return resTree
  }

  fun toMap(): Map<String, Any> {
    return vertices.map {
      it.key to mapOf(
        "data" to it.value.data.minus("code"),
        "parent" to it.value.parent,
        "children" to it.value.children.toList()
      )
    }.toMap().plus("rootVid" to rootVid!!).toMap()
  }

  fun v(vid: Vid): VData = vertice(vid).data

  fun parentNode(b: Vid) = if (vertice(b).parent != null) v(vertice(b).parent!!) else null

  fun parent(b: Vid) = vertice(b).parent

  private fun vertice(vid: Vid) = vertices[vid] ?: error("Vertice $vid not found")

  fun contains(vid: Vid) = vertices.contains(vid)
}


