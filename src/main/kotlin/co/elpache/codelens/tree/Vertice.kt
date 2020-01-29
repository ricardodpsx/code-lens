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

fun vDataOf(vararg pair: Pair<String, Any?>): Vertice {
  val vData = Vertice()
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

  fun putAll(other: Map<String, Any>) = data.putAll(other)
  fun putAll(vertice: Vertice) = data.putAll(vertice.data)

  operator fun set(key: String, value: Any) {
    if (value.toString().isBlank()) return

    //Todo: Adding should be explicit
    //Avoiding overriding of fields
    if (data.containsKey(key))
      data.put(key, listOf(data.get(key).toString().trim(), value.toString().trim()).joinToString(" "))
    else
      data.put(key, value)
  }

  operator fun get(key: String) = data[key]
  fun getString(key: String): String = (data[key] as? String) ?: ""
  fun getInt(key: String): Int = data[key].toString().toIntOrNull() ?: 0

  fun getDouble(key: String): Double = data[key].toString().toDoubleOrNull() ?: 0.0

  fun addAll(vararg pairs: Pair<String, Any?>): Vertice {
    pairs.filter { it.second != null }.forEach {
      this[it.first] = it.second!!
    }
    return this
  }

  fun containsKey(key: String) = data.containsKey(key)
  fun toMap() = data.toMap()
}

data class Edge(
  val name: String,
  val to: Vid
) : Comparable<Edge> {
  override fun compareTo(other: Edge): Int =
    "$name-$to".compareTo(other = "${other.name}-${other.to}")
}

