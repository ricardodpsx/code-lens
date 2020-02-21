package co.elpache.codelens.tree

import java.util.TreeSet

typealias Vid = String


fun Map<String, Any>.toVData(): Vertice {
  val vData = Vertice()
  this.entries.forEach {
    vData[it.key] = it.value
  }
  return vData
}

fun verticeOf(vid: String, vararg pair: Pair<String, Any?>): Vertice {
  val vData = Vertice()
  vData["vid"] = vid
  pair.forEach {
    if (it.second != null)
      vData.data.put(it.first, it.second!!)
  }
  return vData
}

class Vertice(val data: HashMap<String, Any> = HashMap(), val relations: TreeSet<Edge> = TreeSet()) {

  fun clone(): Vertice {
    return Vertice(data)
  }

  val type: String get() = data.getOrDefault("type", "").toString()

  val vid: String get() = data["vid"] as String

  fun contains(value: String) = data.contains(value)
  fun isA(str: String): Boolean {
    return type.split(" ").map { it.trim().toLowerCase() }.any { str.toLowerCase() == it }
  }

  val start: Int get() = this.getInt("start")
  val end: Int get() = this.getInt("end")

  fun minus(key: String) = data.minus(key)
  fun plus(vararg pairs: Pair<String, Any>) = data.plus(pairs)

  fun params() = data.keys
    .filterNot { listOf("name", "type", "vid").contains(it) }
    .filterNot { it.startsWith(":") }


  operator fun set(key: String, value: Any) {
    data[key] = value
  }

  fun addType(value: String) {
    val key = "type"
    if (value.isBlank()) return

    if (data.containsKey(key))
      data[key] = listOf(data[key].toString().trim(), value.trim()).joinToString(" ")
    else
      data[key] = value
  }

  operator fun get(key: String) = data[key]
  fun getString(key: String): String = (data[key] as? String) ?: ""
  fun getInt(key: String): Int = data[key].toString().toIntOrNull() ?: 0

  fun getDouble(key: String): Double = data[key].toString().toDoubleOrNull() ?: 0.0

  fun addAll(m: Map<String, Any?>): Vertice {
    m.filterValues { it != null }.forEach {
      data[it.key] = it.value!!
    }
    return this
  }


  fun addAll(vararg pairs: Pair<String, Any?>): Vertice {
    pairs.filter { it.second != null }.forEach {
      data[it.first] = it.second!!
    }
    return this
  }

  fun containsKey(key: String) = data.containsKey(key)
  fun toMap() = data
}

data class Edge(
  val name: String,
  val to: Vid
) : Comparable<Edge> {
  override fun compareTo(other: Edge): Int =
    "$name-$to".compareTo(other = "${other.name}-${other.to}")
}

