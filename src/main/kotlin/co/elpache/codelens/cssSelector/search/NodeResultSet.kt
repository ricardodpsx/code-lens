package co.elpachecode.codelens.cssSelector.search

import co.elpache.codelens.tree.Vid


class NodeResultSet(val list: List<NodeResult>) : List<NodeResult> by list {

  fun data() = map { it.data }
  fun vids() = map { it.vid }


  fun first() = firstOrNull() ?: EmptyResult()

  fun paramsValues(param: String): List<Pair<Vid, Int>> {
    return list.filter { it.data.contains(param) }
      .map { Pair(it.vid, it.data[param] as Int) }
  }
}

fun Collection<NodeResult>.toResultSet() = NodeResultSet(this.toList())