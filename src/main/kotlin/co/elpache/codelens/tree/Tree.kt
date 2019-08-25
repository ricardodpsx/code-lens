package co.elpache.codelens.tree

import java.util.TreeMap
import java.util.TreeSet


typealias Vid = String


class Tree<T> {

  val vertices: TreeMap<Vid, Vertice<T>>

  constructor(vertices: Map<Vid, Vertice<T>>) {
    this.vertices = TreeMap()
    this.vertices.putAll(vertices.map { it.key to it.value.clone() })
  }

  constructor() {
    this.vertices = TreeMap()
  }

  var rootVid: Vid? = null

  fun rootVid() = rootVid ?: error("RootNode not Set, Did you forgot to add a root node?")


  fun root(): T = v(rootVid())

  fun addIfAbsent(id: String, node: T): Tree<T> {
    if (contains(id)) return this
    vertices[id] = Vertice(id, node)
    return this;
  }

  fun children(vid: Vid) = vertices[vid]!!.children

  fun addChild(from: Vid, to: Vid): Tree<T> {
    vertices[from]!!.children.add(to)
    vertices[to]!!.parentVid = from
    return this

  }

  fun addChild(from: Vid, to: Vid, node: T): Tree<T> {

    addIfAbsent(to, node)
    vertices[to]!!.parentVid = from
    vertices[from]!!.children.add(to)
    return this
  }

  fun v(vid: Vid): T = vertice(vid).node

  fun parentNode(b: Vid) = if (vertice(b).parentVid != null) v(vertice(b).parentVid!!) else null

  fun parent(b: Vid) = vertice(b).parentVid

  private fun vertice(vid: Vid) = vertices[vid] ?: error("Vertice $vid not found")

  fun contains(vid: Vid) = vertices.contains(vid)
}

data class Vertice<T>(
  val vid: Vid,
  val node: T,
  var parentVid: Vid? = null,
  val children: TreeSet<String> = TreeSet()
) {
  fun clone() = this.copy(parentVid = parentVid, children = TreeSet(children))
}
