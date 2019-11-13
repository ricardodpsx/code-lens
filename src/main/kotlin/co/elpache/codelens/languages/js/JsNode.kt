package co.elpache.codelens.languages.js

typealias JsNode = Map<String, Any?>

fun JsNode.child(key: String) = (this as Map<String, Any>)[key] as? JsNode
fun JsNode.getMap(key: String) = (this as Map<String, Any>)[key] as Map<String, Any>
fun JsNode.getList(key: String) = (this as Map<String, Any>)[key] as List<Any>
fun JsNode.getString(key: String): String? {
  val map = (this as? Map<String, Any>)

  if (map != null)
    return map.get(key) as? String

  return null
}

fun JsNode.getInt(key: String): Int? {
  val map = (this as? Map<String, Any>)

  if (map != null)
    return map.get(key) as? Int

  return null
}

fun JsNode.children() =
  this.entries
    .map { it.value?.asNode(it.key) }.filterNotNull()

fun JsNode.childrenMap(): Map<String, Any?> {
  val res = HashMap<String, Any?>()
  this.keys
    .forEach {
      res.put(it, this.entries.asNode(it))
    }
  return res
}

fun JsNode.jsonValues() =
  this.entries
    .filter { it.value?.asNode(it.key) == null }.map {
      it.key to it.value
    }
    .filterNot {
      listOf("type", "name").contains(it.first)
    }
    .toMap()

fun Any.asNode(key: String? = null): JsNode? {
  val map = this as? Map<String, Any>
  val li = this as? List<Map<String, Any>>

  return if (map != null && map.containsKey("type"))
    map
  else if (li != null && li.isNotEmpty() && li.first().containsKey("type"))
    mapOf(
      "type" to key!!,
      "start" to li.first()["start"],
      "end" to li.last()["end"]
    ).plus(li.mapIndexed { index, it -> index.toString() to it }).toMap()
  else null
}