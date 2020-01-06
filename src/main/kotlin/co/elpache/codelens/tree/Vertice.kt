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
  val type: String get() = this.getOrDefault("type", "").toString()
  val vid: String get() = this["vid"] as String

  fun isA(str: String): Boolean {
    return type.split(" ").map { it.trim().toLowerCase() }.any { str.toLowerCase() == it }
  }

  val start: Int get() = this.getInt("start")
  val end: Int get() = this.getInt("end")

  operator fun set(key: String, value: Any) {
    if (value.toString().isBlank()) return

    //Avoiding overriding of fields
    if (containsKey(key))
      super.put(key, listOf(super.get(key).toString().trim(), value.toString().trim()).joinToString(" "))
    else
      super.put(key, value)
  }

  fun getString(key: String): String = (this[key] as? String) ?: ""
  fun getInt(key: String): Int = this[key].toString().toIntOrNull() ?: 0

  fun getDouble(key: String): Double = this[key].toString().toDoubleOrNull() ?: 0.0

  fun addAll(vararg pairs: Pair<String, Any?>): VData {
    pairs.filter { it.second != null }.forEach {
      this[it.first] = it.second!!
    }
    return this
  }
}

data class Edge(
  val name: String,
  val to: Vid
) : Comparable<Edge> {
  override fun compareTo(other: Edge): Int =
    "$name-$to".compareTo(other = "${other.name}-${other.to}")
}

data class Vertice(
  val vid: Vid,
  val data: VData,
  val relations: TreeSet<Edge> = TreeSet()
) {
  fun clone() = this.copy(relations = TreeSet(relations))
}