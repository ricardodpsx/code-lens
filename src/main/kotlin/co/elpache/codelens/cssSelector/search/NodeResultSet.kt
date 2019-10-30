package co.elpachecode.codelens.cssSelector.search


class NodeResultSet(val list: List<NodeResult>) : List<NodeResult> by list {

  fun data() = map { it.data }
  fun vids() = map { it.vid }


  fun first() = firstOrNull() ?: EmptyResult()
}

fun Collection<NodeResult>.toResultSet() = NodeResultSet(this.toList())