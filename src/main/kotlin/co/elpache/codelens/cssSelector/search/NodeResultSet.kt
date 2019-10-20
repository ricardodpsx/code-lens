package co.elpachecode.codelens.cssSelector.search


class NodeResultSet(val list: Collection<NodeResult>): Collection<NodeResult> {

  fun data() = map { it.data }
  fun vids() = map { it.vid }

  override fun contains(element: NodeResult) = list.contains(element)

  override fun containsAll(elements: Collection<NodeResult>) = list.containsAll(elements)

  override fun isEmpty() = list.isEmpty()

  override fun iterator() = list.iterator()

  override val size: Int = list.size

  fun first() = firstOrNull() ?: EmptyResult()
}

fun Collection<NodeResult>.toResultSet() = NodeResultSet(this)