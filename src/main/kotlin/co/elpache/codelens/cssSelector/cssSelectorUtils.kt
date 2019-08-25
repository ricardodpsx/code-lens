package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.NodeData

fun matches(ce: NodeData, typeSelector: TypeSelector): Boolean {
  if (ce["type"].toString().toLowerCase() != typeSelector.name.toLowerCase())
    return false

  return typeSelector.attributes.all { matchesAttribute(it, ce) }
}

fun matchesAttribute(sel: AttributeSelector, obj: NodeData): Boolean {
  if (!propertyExists(obj, sel.name)) return false

  val value = getPropertyValue(obj, sel.name)
  val search = sel.value ?: ""

  return when (sel.op ?: "*=") {
    "=" -> value == search
    "*=" -> value.contains(search)
    "^=" -> value.startsWith(search)
    "$=" -> value.endsWith(search)
    else -> false
  }
}


fun getPropertyValue(ce: NodeData, name: String) = ce[name]!!.toString()

fun propertyExists(ce: NodeData, name: String) = ce.containsKey(name)
