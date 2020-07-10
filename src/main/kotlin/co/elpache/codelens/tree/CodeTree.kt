package co.elpache.codelens.tree

import org.jetbrains.kotlin.backend.common.pop
import java.util.LinkedList
import java.util.TreeMap
import java.util.TreeSet

open class CodeTree {

  val vertices: TreeMap<Vid, Vertice> = TreeMap()

  val edges: HashMap<Vid, MutableSet<Edge>> = HashMap()

  var rootDirVid: Vid? = null

  fun rootDirVid() = rootDirVid ?: error("RootNode not Set, Did you forgot to add a root node?")

  var byTypeCache = HashMap<Vid, MutableList<Vid>>()

  fun addVertice(v: Vertice): Vertice {
    if (rootDirVid == null) rootDirVid = v.vid

    if (contains(v.vid)) return v
    vertices[v.vid] = v.clone()
    return v
  }

  fun children(vid: Vid) = adj(vid, "children")

  fun addChild(from: Vid, to: Vid) {
    if (parent(to) == null) {
      addRelation("children", from, to)
      addRelation("parent", to, from)
    }
  }

  fun addChild(from: Vid, node: Vertice): CodeTree {
    assert(from != node.vid)
    addVertice(node)
    addRelation("children", from, node.vid)
    addRelation("parent", node.vid, from)
    return this
  }

  fun descendants(vid: Vid, descendantList: MutableSet<Vid> = mutableSetOf()): Set<Vid> {
    for (cVid in children(vid)) {
      descendantList.add(cVid)
      descendants(cVid, descendantList)
    }
    return descendantList
  }

  fun subTree(vid: Vid): CodeTree {
    val ct = CodeTree()
    ct.rootDirVid = vid
    val items = ArrayList<Vid>()
    items.add(vid)
    while (items.isNotEmpty()) {
      val p = items.pop()
      ct.vertices[p] = vertices[p]!!
      ct.edges[p] = edges[p]!!
      children(p).forEach {
        items.add(it)
      }
    }
    return ct
  }

  fun print(from: Vid = rootDirVid()) {
    println(subTree(from).asString())
  }

  fun asString(): String {
    val out = StringBuilder()
    val root = v(rootDirVid()).plus("code" to "<Excluded>").minus("vid")
    out.append("${root}\n")
    dfs(rootDirVid(), "-", out)
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

  fun treeFromChildren(vids: List<Vid>): CodeTree {
    if (vids.isEmpty()) return CodeTree()

    var resTree = CodeTree()
    //add tree
    vids.forEach { v ->
      ancestors(v).reversed()
        .windowed(2, partialWindows = true).forEach {
          resTree.addVertice(v(it[0]))
          if (it.size == 2) resTree = resTree.addChild(it[0], v(it[1]))
        }
    }
    //Add rest of the relationships
    resTree.vertices.keys.forEach { vid ->
      edgesOf(vid).filter { it.name != "parent" && it.name != "children" }.forEach { e ->
        if (resTree.vertices.containsKey(e.to)) {
          resTree.addRelation(e.name, vid, e.to)
        }
      }
    }
    resTree.rootDirVid = rootDirVid
    return resTree
  }

  fun v(vid: Vid): Vertice = vertice(vid)

  fun parentNode(b: Vid) = parent(b)?.let { v(parent(b)!!) }

  fun parent(b: Vid) = adj(b, "parent").getOrNull(0)

  private fun vertice(vid: Vid) = vertices[vid] ?: error("Vertice $vid not found")

  fun contains(vid: Vid) = vertices.contains(vid)

  fun addSubTree(subTree: CodeTree, to: Vid): CodeTree {
    val expectedSize = subTree.vertices.size + vertices.size
    vertices.putAll(subTree.vertices.toList())
    edges.putAll(subTree.edges.toList())
    addChild(to, subTree.rootDirVid())

    if (vertices.size != expectedSize)
      throw Error("The trees should be disjoint")

    return this
  }

  fun addRelation(name: String, from: Vid, to: Vid) {
    if (adj(from, name).contains(to)) return
    if (name == "parent" && parent(from) != null) error("Only one parent accepted")
    edges.computeIfAbsent(from) { TreeSet() }
    edges[from]?.add(Edge(name, to, v(to).getString("name"), v(to).getString("type")))
  }

  fun adj(vid: String): List<Vid> = edges[vid]?.map { it.to } ?: emptyList()

  fun adj(vid: String, relName: String): List<Vid> =
    edges.getOrDefault(vid, emptyList<Edge>()).filter { it.name == relName }.map { it.to }

  fun code(vid: Vid): String {
    return if (v(vid).isA("file"))
      v(vid).getString("code")
    else {
      val fileNode = ancestors(vid).find { v(it).isA("file") } as Vid
      val contents = v(fileNode).getString("code")
      contents.substring(v(vid).start, v(vid).end)
    }
  }

  fun inorder(): List<Vid> {
    val out = mutableListOf<Vertice>()
    out.add(v(rootDirVid!!))
    dfs(rootDirVid!!, out)
    return out.map { it["value"] as String }
  }

  private fun dfs(vid: Vid, out: MutableList<Vertice>) {
    for (cVid in children(vid)) {
      out.add(v(cVid))
      dfs(cVid, out)
    }
  }

  fun join(child: CodeTree) {
    assert(vertices.keys.intersect(child.vertices.keys.minus("rootVid")).isEmpty()) { "Trees should be disjoint" }
    vertices.putAll(child.vertices)
    edges.putAll(child.edges)
    addChild(rootDirVid!!, child.rootDirVid!!)
  }

  fun rootDir(): Vertice = v(rootDirVid())

  private fun edgesOf(v: Vid) = edges[v] ?: emptySet<Edge>()

  fun edgeOf(from: Vid, to: String, relName: String) =
    edges[from]?.firstOrNull { it.to == to && it.name == relName }

  private data class Backref(val from: String, val to: String, val name: String)

  private fun backReferences() = edges.flatMap { e ->
    e.value.filter { it.name == "imports" }
      .map { Backref(e.key, it.to, it.name) }
  }.groupBy { it.to }

  fun addTransitiveRelationships(vid: Vid) {
    val backRefs = backReferences()

    descendants(vid).forEach { d ->
      edgesOf(d)
        .filter { it.name == "imports" }
        .forEach { de ->
          addRelation(de.name, vid, de.to)
          edgeOf(vid, de.to, de.name)?.let {
            it.data["${it.name}Count"] = it.data.getInt("${it.name}Count") + 1
          }
        }

      backRefs[d]?.forEach {
        addRelation(it.name, it.from, vid)
      }
    }
  }

  fun collapse(vid: Vid) {
    val descendants = descendants(vid).filterNot {  v(it).isA("dir") }
    val prevEdges = edges.toList()
    descendants.forEach { d ->
      edges.remove(d)
      vertices.remove(d)
    }
    prevEdges.forEach { e ->
      edges[e.first]?.removeIf { descendants.contains(it.to) }
    }
  }

  fun relationData(from: Vid, to: Vid, relName: String): Map<String, Any> {
    return edges[from]
      ?.firstOrNull { it.to == to && it.name == relName }
      ?.data ?: mapOf()
  }

  fun clone(): CodeTree {
    val newTree = CodeTree()
    edges.forEach {
      newTree.edges[it.key] = it.value.map { it.clone() }.toMutableSet()
    }
    vertices.forEach {
      newTree.vertices[it.key] = it.value.clone()
    }
    newTree.rootDirVid = rootDirVid
    return newTree
  }

}


