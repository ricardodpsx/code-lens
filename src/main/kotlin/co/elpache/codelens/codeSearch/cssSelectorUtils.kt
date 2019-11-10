package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.tree.VData

fun matches(ce: VData, typeSelector: TypeSelector): Boolean {

  if (typeSelector.name != "*" && ce[typeSelector.attributeToMatch].toString().toLowerCase() != typeSelector.name.toLowerCase())
    return false

  return typeSelector.attributes.all { matchesAttribute(it, ce) }
}

fun matchesAttribute(sel: AttributeSelector, obj: VData): Boolean {
  if (!propertyExists(obj, sel.name)) return false
  if (sel.value == null) return true

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


fun getPropertyValue(ce: VData, name: String) = ce[name]!!.toString()

fun propertyExists(ce: VData, name: String) = ce.containsKey(name)
