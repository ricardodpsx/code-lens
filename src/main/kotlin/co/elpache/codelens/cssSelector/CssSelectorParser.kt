package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.Node
import co.elpache.codelens.QUOTED_STRING
import co.elpache.codelens.defaultParser
import co.elpache.codelens.rootParser
import co.elpache.codelens.unwrap
import java.util.LinkedList

fun parseCssSelector(selector: String) = selectorParser()
  .atLeastOne(typeSelectorParser()) {
    many(attributeSelectorParser()) {
      one(openBraket())
      one(attributeNameParser())
      zeroOrOne(attributeOperationParser())
      zeroOrOne(attributeValueParser())
      one(closeBraket())
    }
  }.take(selector) as CssSelectors

class CssSelectors(val selectors: List<CssSelector>) : CssSelector()
open class CssSelector : Node()

class TypeSelector(
  val name: String,
  val attributes: List<AttributeSelector>
) : CssSelector()

class DescendantSelector(
  val descendants: ArrayList<TypeSelector> = arrayListOf()
) : CssSelector()

class AttributeSelector(
  val name: String,
  val op: String? = null,
  val value: String? = null
) : Node()

val selectorParser = rootParser { node ->
  CssSelectors(selectors = groupDescendantSelectors(node.children).map {
    if (it.size == 1) it[0]
    else DescendantSelector(it)
  })
}

private fun groupDescendantSelectors(children: List<Node>): ArrayList<ArrayList<TypeSelector>> {

  val flatSelectorList = LinkedList(children.map { it as TypeSelector })
  val grouped = ArrayList<ArrayList<TypeSelector>>()

  while (flatSelectorList.isNotEmpty()) {
    val current = flatSelectorList.pollFirst()
    if (current.name == ">")
      grouped.last().add(flatSelectorList.pollFirst())
    else
      grouped.add(arrayListOf(current))
  }

  return grouped
}

val typeSelectorParser = defaultParser("^([A-Za-z0-9_]+|>)") {
  when (it.text) {
    ">" -> TypeSelector(it.text, it.children.map { it as AttributeSelector })
    else -> TypeSelector(it.text, it.children.map { it as AttributeSelector })
  }
}
val openBraket = defaultParser("^\\[")
val closeBraket = defaultParser("^\\]")
val attributeSelectorParser = defaultParser("^", "^\\[") {
  AttributeSelector(it[1]!!.text, it[2]?.text, it[3]?.text)
}

val attributeNameParser = defaultParser("^[A-Za-z0-9_\\-]+")
val attributeOperationParser = defaultParser("^[\\^\\|\\~\\$\\*]?=")
val attributeValueParser = defaultParser(QUOTED_STRING, "^['\"]") {
  Node(it.text.unwrap())
}