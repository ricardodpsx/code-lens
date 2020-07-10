@file:Suppress("Reformat")
package co.elpache.codelens.codeSearch.parser

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.codeSearch.search.PathFinder
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.RESERVED_PARAMS
import co.elpache.codelens.tree.Vertice
import co.elpache.codelens.tree.vids
import co.elpache.codelens.useCases.SearchResults
import mu.KotlinLogging

data class ParamSet(val paramName: String, val query: Query)
data class SetQuery(val nodesToSet: Query, val paramSetters: List<ParamSet>)
enum class RelationType {
  DIRECT_RELATION,
  FOLLOW_RELATION
}

private val logger = KotlinLogging.logger {}

data class SelectorFunction(
  val name: String,
  val params: List<Expression>
) : Expression {

  data class FunctionEntry(
    val name: String,
    val function: (args: List<String>, node: ContextNode) -> Any,
    val type: String
  )

  companion object {
    private val functionRegistry: MutableList<FunctionEntry> = mutableListOf()
    fun addFunction(name: String, type: String, function: (args: List<String>, node: ContextNode) -> Any) {
      functionRegistry.add(
        FunctionEntry(
          name = name,
          function = function,
          type = type
        )
      )
    }
  }

  override fun evaluate(context: ContextNode): Any? =
    functionRegistry.firstOrNull {
      name == it.name && context.vertice.isA(it.type)
    }?.let {
      try {
        it.function(params.map { param -> param.evaluate(context).toString() }, context)
      } catch (e: Exception) {
        logger.warn("Problem executing $it", e)
      }
    }

}



typealias AggregationFunction = (CodeTree, List<Vertice>, List<Any?>) -> Any?

//Query ast
data class Query(
  val selectors: List<TypeSelector>,
  val aggregator: SelectorFunction? = null
) : Expression {
  companion object {
    val aggregatorRegistry: HashMap<String, AggregationFunction> = HashMap()

    init {
      aggregatorRegistry["count"] = { _, results, params -> results.size }
      aggregatorRegistry["collapsing"] = { tree, results, params ->
        val collapsed = tree.treeFromChildren(results.vids())
        results.filter { it.isA("dir") }.forEach { p ->
          val vid = p.vid
          collapsed.addTransitiveRelationships(vid)
        }

        results.filter { it.isA("dir") }.forEach {
          collapsed.collapse(it.vid)
        }

        SearchResults(collapsed, results)
      }
    }
  }

  private fun callAggregator(aggregator: SelectorFunction, tree: CodeTree, res: List<Vertice>): Any? {
    if (!aggregatorRegistry.containsKey(aggregator.name)) error("Aggregator function '${aggregator.name}' not found")
    return aggregatorRegistry[aggregator.name]!!(tree, res, aggregator.params.map {
      it.evaluate(ContextNode(tree.rootDirVid(), tree))
    })
  }

  override fun
      evaluate(context: ContextNode): Any? {
    val res = PathFinder(context.tree, context.vid).find(this)
    return if (aggregator != null)
      callAggregator(aggregator, context.tree, res)
    else
      res
  }
}

interface Expression {
  fun evaluate(context: ContextNode): Any?
}

interface UnnaryExpression : Expression
data class NullExpression(val value: String = "") : Expression {
  override fun evaluate(context: ContextNode): Any? = null
}

//Support for falsy and thruthy
fun isThruty(value: Any?) =
  !(value == null
      || value.toString().isBlank()
      || ((value as? Int) == 0)
      || ((value as? Boolean) == false)
      || ((value as? List<*>)?.size == 0))

data class BinnaryExpression(val left: Expression, val op: String, val right: Expression) :
  Expression {
  private val logger = KotlinLogging.logger {}

  override fun evaluate(context: ContextNode): Any? {
    val leftRaw = left.evaluate(context)
    val rightRaw = right.evaluate(context)
    val leftVal = leftRaw.toString()
    val rightVal = rightRaw.toString()
    try {
      return when (op) {
        "=" -> equals(leftRaw, rightRaw)
        "!=" -> !equals(leftRaw, rightRaw)
        "*=" -> leftVal.contains(rightVal)
        "^=" -> leftVal.startsWith(rightVal)
        "$=" -> leftVal.endsWith(rightVal)
        ">" -> leftVal.toDouble() > rightVal.toDouble()
        "<" -> leftVal.toDouble() < rightVal.toDouble()
        ">=" -> leftVal.toDouble() >= rightVal.toDouble()
        "<=" -> leftVal.toDouble() <= rightVal.toDouble()
        "||" -> isThruty(leftRaw) || isThruty(rightRaw)
        "&&" -> isThruty(leftRaw) && isThruty(rightRaw)
        "+" -> leftVal.toDouble() + rightVal.toDouble()
        "-" -> leftVal.toDouble() - rightVal.toDouble()
        "*" -> leftVal.toDouble() * rightVal.toDouble()
        "/" -> leftVal.toDouble() / rightVal.toDouble()
        else -> error("Operation '$op' not implemented")
      }
    } catch (e: Exception) {
      //Todo: Pass down the original expression string so it's easier to troubleshoot
      logger.warn("Problem evaluating $this") { e }
      return false
    }
  }

  private fun equals(leftRaw: Any?, rightRaw: Any?): Boolean {
    return if (leftRaw is String || rightRaw is String)
      leftRaw == rightRaw
    else
      leftRaw.toString().toDouble() == rightRaw.toString().toDouble()
  }
}

data class LiteralExpression(val value: Any) : UnnaryExpression {
  override fun evaluate(context: ContextNode): Any? {
    return value
  }
}

data class NameExpression(val value: String) : UnnaryExpression {
  override fun evaluate(context: ContextNode): Any? {
    return context.vertice[value]
  }
}

data class Relation(
  val name: String,
  val type: RelationType
)

data class TypeSelector(
  val name: String,
  val attributeToMatch: String = "type",
  val expr: Expression,
  val relation: Relation
) : Expression {


  fun isPseudoElement() = name.startsWith(":")

  override fun evaluate(context: ContextNode): Boolean {
    val values = context.vertice[attributeToMatch].toString().split(" ").map { it.trim().toLowerCase() }

    if (name != "*" && values.none { it == name.toLowerCase() })
      return false

    if (expr is NullExpression) return true

    return isThruty(expr.evaluate(context))
  }

}

data class AttributeSelector(
  val name: String,
  val op: String? = null,
  val value: String? = null
)

data class AliasExpression(val name: String, val expr: Expression) : Expression {
  override fun evaluate(context: ContextNode): Any? {
    if (context.vertice.containsKey(name))
      logger.warn { "Trying to set the alias alias $name for an existing element " }

    return expr.evaluate(context)?.let {
      if (RESERVED_PARAMS.contains(name)) logger.warn { "Can not override $name" }
      else context.vertice[name] = it
      it
    }
  }
}