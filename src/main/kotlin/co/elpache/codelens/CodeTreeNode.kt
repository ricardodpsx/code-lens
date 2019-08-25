package co.elpache.codelens


typealias NodeData = HashMap<String, Any>

fun NodeData.and(vararg p: Pair<String, Any?>) = this.plus(p) as NodeData


fun nodeDataOf(vararg pairs: Pair<String, Any?>) =
  if (pairs.isNotEmpty()) NodeData(pairs.filter { it.second != null }.toMap()) else NodeData()


interface CodeTreeNode {
  fun expand(): List<CodeTreeNode>
  fun data(): NodeData
}