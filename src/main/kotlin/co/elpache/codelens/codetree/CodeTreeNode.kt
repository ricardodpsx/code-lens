package co.elpache.codelens.codetree


typealias NodeData = HashMap<String, Any>

fun NodeData.and(vararg p: Pair<String, Any?>) = this.plus(p) as NodeData

fun HashMap<String, Any>.addAll(vararg pairs: Pair<String, Any?>) {
  pairs.forEach {
    if (it.second != null)
      this[it.first] = it.second!!
  }
}

fun nodeDataOf(vararg pairs: Pair<String, Any?>) =
  if (pairs.isNotEmpty()) NodeData(pairs.filter { it.second != null }.toMap()) else NodeData()


abstract class CodeTreeNode {
  abstract fun expand(): List<CodeTreeNode>
  val data = HashMap<String, Any>()
  fun <T> value(key: String): T = data[key] as T
  fun contains(key: String) = data.contains(key)
}