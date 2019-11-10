package co.elpache.codelens.tree

import java.util.TreeMap


open class Tree {

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

  fun addIfAbsent(id: String, data: VData): Tree {
    if (contains(id)) return this
    vertices[id] = Vertice(id, data)
    return this;
  }

  fun children(vid: Vid) = vertices[vid]!!.children

  fun addChild(from: Vid, to: Vid): Tree {
    vertices[from]!!.children.add(to)
    vertices[to]!!.parent = from
    return this

  }

  fun addChild(from: Vid, to: Vid, node: VData): Tree {
    addIfAbsent(to, node)
    vertices[to]!!.parent = from
    vertices[from]!!.children.add(to)
    return this
  }

  fun v(vid: Vid): VData = vertice(vid).data

  fun parentNode(b: Vid) = if (vertice(b).parent != null) v(vertice(b).parent!!) else null

  fun parent(b: Vid) = vertice(b).parent

  private fun vertice(vid: Vid) = vertices[vid] ?: error("Vertice $vid not found")

  fun contains(vid: Vid) = vertices.contains(vid)
}


