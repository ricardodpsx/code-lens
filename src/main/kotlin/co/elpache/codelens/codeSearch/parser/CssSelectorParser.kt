@file:Suppress("Reformat")

package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.QUOTED_STRING
import co.elpache.codelens.codeSearch.parser.DefaultParser
import co.elpache.codelens.codeSearch.parser.defaultParser
import co.elpache.codelens.codeSearch.parser.rootParser
import co.elpache.codelens.codeSearch.parser.textParser
import co.elpache.codelens.unwrap

fun parseCssSelector(selector: String) = selectorParser.parse(selector)

fun parseTypeSelector(selector: String) = typeSelectorParser.parse(selector)

fun parseSetQuery(selector: String) = setQueryParser.parse(selector)


/*

root = query
expr = (expr) | (expr op expr) | funCall | exprQuery
exprQuery = query '|' aggregator
query = sel \ sel > query | sel query
sel = type[expr]

fun[expr]
 */

//Query ast
data class Query(
  val selectors: List<TypeSelector>,
  val func: Func?
)

data class TypeSelector(
  val name: String,
  val attributes: List<AttributeSelector>,
  val relationType: RelationType,
  val attributeToMatch: String = "type",
  val pseudoAttribute: PseudoAttribute?
)

data class AttributeSelector(
  val name: String,
  val op: String? = null,
  val value: String? = null
)

val PARENTHENYS_OP = textParser("^\\(")
val PARENTHENYS_CL = textParser("^\\)")

val paramSetParser = defaultParser<ParamSet>(exp = "^", lookAhead = ".") {
  val attributeName = one(attributeNameParser)
  one(textParser("="))
  ParamSet(attributeName, either(subQuery, funcParser))
}

data class ParamSet(val paramName: String, val setFunction: Any)
data class SetQuery(val nodesToSet: Query, val paramSetters: List<ParamSet>)

val setQueryParser: DefaultParser<SetQuery> =
  defaultParser<SetQuery>("^SET") {

    one(PARENTHENYS_OP)
    val nodesToUpdate = one(selectorParser)
    one(PARENTHENYS_CL)
    val paramSets = many(paramSetParser)

    SetQuery(nodesToUpdate, paramSets)
  }

val subQuery = defaultParser<Query>(exp = "^", lookAhead = "^\\(") {
  one(PARENTHENYS_OP)
  val setQuery = one(selectorParser)
  one(PARENTHENYS_CL)
  setQuery
}


//Query Parser
val selectorParser: DefaultParser<Query> = rootParser<Query> {
  Query(selectors = atLeastOne(typeSelectorParser), func = zeroOrOne(funcParser))
}

class Func(val op: String, val params: List<String>)

val funcNameParser = textParser("^[A-Za-z0-9_\\-]+")

val funcParser: DefaultParser<Func?> = defaultParser("^\\|") {
  Func(one(funcNameParser), zeroOrOne(funcParamListParser) ?: listOf())
}

val funcParamListParser = defaultParser<List<String>>("^", "\\(") {
  one(PARENTHENYS_OP)
  val params = many(funcParamParser)
  one(PARENTHENYS_CL)
  params
}

val funcParamParser = defaultParser<String>("^", "^['\"0-9A-Za-z\\-_]") {
  val params = oneOf(attributeValueParser, attributeNameParser)
  oneOf(textParser("^\\,"), textParser("^", "^\\)"))
  params
}


data class PseudoAttribute(val op: String, val query: Query)

val stutff = HashMap<String, Any>()

val pseudoAttributeSelector: DefaultParser<PseudoAttribute> = defaultParser<PseudoAttribute>("^:") {
  val op = one(textParser("^(has)"))
  one(PARENTHENYS_OP)
  val query = one(selectorParser)
  one(PARENTHENYS_CL)

  PseudoAttribute(op, query)
}


val typeSelectorParser: DefaultParser<TypeSelector> =
  defaultParser<TypeSelector>("^(#?[A-Za-z0-9_\\-]+|\\$|\\*)") {

    val attrSelector = many(attributeSelectorParser)

    val pseudoAttribute = zeroOrOne(pseudoAttributeSelector)
    val relation = zeroOrOne(relationParser) ?: RelationType(RelationTypes.CHILDREN)

    val name = if (it.first() == '#') it.drop(1) else it

    val attributeToMatch = if (it.first() == '#') "name" else "type"

    TypeSelector(name, attrSelector, relation, attributeToMatch, pseudoAttribute)
  }


enum class RelationTypes { DIRECT_DESCENDANT, CHILDREN }

class RelationType(val type: RelationTypes)

val relationParser: DefaultParser<RelationType> = defaultParser<RelationType>("^>") {
  RelationType(RelationTypes.DIRECT_DESCENDANT)
}

val openBraket: DefaultParser<String> = textParser("^\\[")
val closeBraket: DefaultParser<String> = textParser("^\\]")

val attributeSelectorParser: DefaultParser<AttributeSelector> = defaultParser<AttributeSelector>("^", "^\\[")
{
  one(openBraket)
  val attrName = one(attributeNameParser)
  val attrOperation = zeroOrOne(attributeOperationParser)
  val attrValue = zeroOrOne(attributeValueParser)
  one(closeBraket)

  AttributeSelector(attrName, attrOperation, attrValue)
}

val attributeValueParser = textParser("^", "^['\"0-9]") {
  oneOf(attributeStringParser, attributeIntegerParser)
}

val attributeNameParser = textParser("^[A-Za-z0-9_\\-]+")

val attributeOperationParser = textParser("^([\\^\\|\\~\\$\\*!<>]?=|<|>)")

val attributeStringParser = textParser(QUOTED_STRING, "^['\"]") { it.unwrap() }

val attributeIntegerParser = textParser("^[+-]?[0-9]+")
