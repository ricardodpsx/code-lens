package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.Node
import co.elpache.codelens.ParserBuilder
import co.elpache.codelens.QUOTED_STRING
import co.elpache.codelens.defaultParser
import co.elpache.codelens.rootParser
import co.elpache.codelens.unwrap

fun parseCssSelector(selector: String) = selectorParser().define { code ->
  mapOf("value" to atLeastOne(typeSelector(), code))
}.take(selector) as CssSelectors

fun parseTypeSelector(selector: String) = typeSelector().take(selector) as TypeSelector

fun typeSelector(): ParserBuilder {
  return typeSelectorParser().define { code ->
    mapOf(
      "attributeSelector" to many(
        attributeSelectorParser().define {
          one(openBraket(), code)
          val attrName = one(attributeNameParser(), code)
          val attrOperation = zeroOrOne(attributeOperationParser(), code)
          val attrValue = zeroOrOne(
            attributeParser().define {
              mapOf("value" to oneOf(attributeStringParser(), attributeIntegerParser(), code = code))
            }, code
          )
          one(closeBraket(), code)

          mapOf("attrName" to attrName, "attrOperation" to attrOperation, "attrValue" to attrValue)
        }, code
      ),
      "relation" to zeroOrOne(relationParser(), code)
    )
  }

}


class CssSelectors(val selectors: List<TypeSelector>) : CssSelector()
open class CssSelector : Node()

class TypeSelector(
  val name: String,
  val attributes: List<AttributeSelector>,
  val relationType: RelationType,
  val attributeToMatch: String = "type"
) : CssSelector()

class AttributeSelector(
  val name: String,
  val op: String? = null,
  val value: String? = null
) : Node()

val selectorParser = rootParser {
  CssSelectors(selectors = it.children.map { it as TypeSelector })
}

val typeSelectorParser = defaultParser("^(#?[A-Za-z0-9_\\-]+|\\$|\\*)") {

  val name = if (it.text.first() == '#') it.text.drop(1) else it.text

  val attributeToMatch = if (it.text.first() == '#') "name" else "type"

  TypeSelector(
    name,
    it.children.dropLast(1).map { it as AttributeSelector },
    it.children.last() as RelationType,
    attributeToMatch
  )
}

enum class RelationTypes {
  DIRECT_DESCENDANT, CHILDREN
}

class RelationType(val type: RelationTypes) : Node()

val relationParser = defaultParser("^>?") {
  if (it.text == ">") RelationType(RelationTypes.DIRECT_DESCENDANT)
  else RelationType(RelationTypes.CHILDREN)
}

val openBraket = defaultParser("^\\[")
val closeBraket = defaultParser("^\\]")
val attributeSelectorParser = defaultParser("^", "^\\[") {
  AttributeSelector(it[0]!!.text, it[1]?.text, it[2]?.text)
}


val attributeParser = defaultParser("^", "^['\"0-9]", converter = { Node(it[0]!!.text) })

val attributeNameParser = defaultParser("^[A-Za-z0-9_\\-]+")
val attributeOperationParser = defaultParser("^[\\^\\|\\~\\$\\*]?=")

val attributeStringParser = defaultParser(QUOTED_STRING, "^['\"]") {
  Node(it.text.unwrap())
}
val attributeIntegerParser = defaultParser("^[+-]?[0-9]+") {
  Node(it.text)
}