package co.elpache.codelens.tree


typealias Vid = String


class Tree<T> {

  val vertices: Map<Vid, Vertice<T>>

  constructor(vertices: Map<Vid, Vertice<T>>) {
    this.vertices = vertices
  }

  constructor() {
    this.vertices = HashMap()
  }

  var rootVid: Vid? = null

  fun rootVid() = rootVid ?: error("RootNode not Set, Did you forgot to add a root node?")


  fun root(): T = v(rootVid())

  fun addIfAbsent(id: String, node: T): Tree<T> {
    if (contains(id)) return this
    return Tree(vertices.plus(id to Vertice(id, node)))
  }

  fun children(vid: Vid) = vertices[vid]!!.children

  fun addChild(from: Vid, to: Vid) = Tree(
    vertices
      .plus(from to vertice(from).copy(children = vertice(from).children.plus(to)))
      .plus(to to vertice(to).copy(parentVid = from))
  )

  fun addChild(from: Vid, to: Vid, node: T) = Tree(
    vertices
      .plus(from to vertice(from).copy(children = vertice(from).children.plus(to)))
      .plus(to to Vertice(to, node, from))
  )

  fun v(vid: Vid): T = vertice(vid).node

  fun parentNode(b: Vid) = if(vertice(b).parentVid != null) v(vertice(b).parentVid!!) else null

  fun parent(b: Vid) = vertice(b).parentVid

  private fun vertice(vid: Vid) = vertices[vid] ?: error("Vertice $vid not found")

  fun contains(vid: Vid) = vertices.contains(vid)


}

data class Vertice<T>(
  val vid: Vid,
  val node: T,
  val parentVid: Vid? = null,
  val children: Set<String> = setOf()
)
