package co.elpache.codelens.codeSearch.search

import co.elpache.codelens.tree.Vid
import co.elpachecode.codelens.cssSelector.Query


val aggregatorsRegistry = HashMap<String, (params: List<String>, res: NodeResultSet) -> Any>()


class NodeResultSet(val list: List<NodeResult>, val query: Query?) : List<NodeResult> by list {

  companion object {
    init {
      aggregatorsRegistry["count"] = { _, res -> res.list.size }
    }
  }

  fun data() = map { it.data }
  fun vids() = map { it.vid }

  fun value() = with(query!!.func!!) {
    aggregatorsRegistry[op]!!(params, this@NodeResultSet)
  }


  fun first() = firstOrNull() ?: EmptyResult()

  fun paramsValues(param: String): List<Pair<Vid, Int>> {
    return list.filter { it.data.contains(param) }
      .map { Pair(it.vid, it.data[param] as Int) }
  }
}

fun Collection<NodeResult>.toResultSet(query: Query? = null) = NodeResultSet(this.toList(), query)