@file:Suppress("Reformat")

package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.QUOTED_STRING
import co.elpache.codelens.defaultParser
import co.elpache.codelens.rootParser
import co.elpache.codelens.textParser
import co.elpache.codelens.unwrap

fun parseCssSelector(selector: String) = selectorParser.parse(selector)

fun parseTypeSelector(selector: String) = typeSelectorParser.parse(selector)

fun typeSelector() = typeSelectorParser

data class CssSelectors(val selectors: List<TypeSelector>)

class TypeSelector(
  val name: String,
  val attributes: List<AttributeSelector>,
  val relationType: RelationType,
  val attributeToMatch: String = "type"
)

class AttributeSelector(
  val name: String,
  val op: String? = null,
  val value: String? = null
)

val selectorParser =
  rootParser<CssSelectors>() {
    CssSelectors(selectors = atLeastOne(typeSelectorParser))
  }


val typeSelectorParser =
  defaultParser<TypeSelector>("^(#?[A-Za-z0-9_\\-]+|\\$|\\*)") {

    val attrSelector = many(attributeSelectorParser)
    val relation = zeroOrOne(relationParser) ?: RelationType(RelationTypes.CHILDREN)

    val name = if (it.first() == '#') it.drop(1) else it

    val attributeToMatch = if (it.first() == '#') "name" else "type"

    TypeSelector(name, attrSelector, relation, attributeToMatch)
  }


enum class RelationTypes { DIRECT_DESCENDANT, CHILDREN }

class RelationType(val type: RelationTypes)

val relationParser = defaultParser<RelationType>("^>") {
  RelationType(RelationTypes.DIRECT_DESCENDANT)
}

val openBraket = textParser("^\\[")
val closeBraket = textParser("^\\]")

val attributeSelectorParser = defaultParser<AttributeSelector>("^", "^\\[")
{
  one(openBraket)
  val attrName = one(attributeNameParser)
  val attrOperation = zeroOrOne(attributeOperationParser)
  val attrValue = zeroOrOne(attributeParser)
  one(closeBraket)

  AttributeSelector(attrName, attrOperation, attrValue)
}


val attributeParser = textParser("^", "^['\"0-9]")
{ oneOf(attributeStringParser, attributeIntegerParser) }


val attributeNameParser = textParser("^[A-Za-z0-9_\\-]+")

val attributeOperationParser = textParser("^[\\^\\|\\~\\$\\*]?=")

val attributeStringParser = textParser(QUOTED_STRING, "^['\"]") { it.unwrap() }


val attributeIntegerParser = textParser("^[+-]?[0-9]+")