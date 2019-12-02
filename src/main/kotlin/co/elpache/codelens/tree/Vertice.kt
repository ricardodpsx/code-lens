package co.elpache.codelens.tree

import java.util.TreeSet

typealias Vid = String


fun HashMap<String, Any>.toVData(): VData {
  val vData = VData()
  this.entries.forEach {
    vData[it.key] = it.value
  }
  return vData
}

fun vDataOf(vararg pair: Pair<String, Any?>): VData {
  val vData = VData()
  pair.forEach {
    if (it.second != null)
      vData.put(it.first, it.second!!)
  }
  return vData
}

class VData : HashMap<String, Any>() {
  val fileNode: String get() = this["fileNode"] as Vid
  val type: String get() = this["type"] as String

  val startOffset: Int get() = this["startOffset"] as Int
  val endOffset: Int get() = this["endOffset"] as Int

  fun getString(key: String): String = (this[key] as? String) ?: ""
  fun getInt(key: String): Int = (this[key] as? Int) ?: 0
  fun addAll(vararg pairs: Pair<String, Any?>): VData {
    pairs.filter { it.second != null }.forEach {
      this[it.first] = it.second!!
    }
    return this
  }
}

data class Vertice(
  val vid: Vid,
  val data: VData,
  var parent: Vid? = null,
  val children: TreeSet<Vid> = TreeSet()
) {
  fun clone() = this.copy(parent = parent, children = TreeSet(children))
}