package co.elpache.codelens.tree


typealias Vid = String

val RESERVED_PARAMS = listOf("name", "type", "vid", "start", "end", "index")

fun Map<String, Any>.toVData(): Vertice {
  val vData = Vertice()
  this.entries.forEach {
    vData[it.key] = it.value
  }
  return vData
}

fun Collection<Vertice>.vids() = map { it.vid }

fun Collection<Vertice>.paramsValues(key: String, param: String): List<Pair<Vid, Double>> {
  return filter { it.contains(param) }
    .map { Pair(it[key].toString(), it.getDouble(param)) }
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

fun Map<String, Any>.getString(key: String) = (this[key] as? String) ?: ""
fun Map<String, Any>.getInt(key: String) = (this[key]?.toString()?.toIntOrNull()) ?: 0
fun Map<String, Any>.getDouble(key: String): Double = (this[key]?.toString()?.toDoubleOrNull()) ?: 0.0

class Vertice(val data: HashMap<String, Any> = HashMap()) {

  fun clone(): Vertice {
    //Todo: Avoid returning reference
    return Vertice(data)
  }

  val rawType: String get() = type.split(" ").first()

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

  fun params() = data
    .filter { it.value is Int || it.value is Double || it.value is Float }
    .keys
    .filterNot { RESERVED_PARAMS.contains(it) }
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
  val to: Vid,
  val toName: String?,
  val toType: String,
  val data: HashMap<String, Any> = HashMap()
) : Comparable<Edge> {
  override fun compareTo(other: Edge): Int =
    "$name-$to".compareTo(other = "${other.name}-${other.to}")

  fun clone(): Edge {
    return Edge(name, to, toName, toType, HashMap(data))
  }
}

