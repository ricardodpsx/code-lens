@file:Suppress("Reformat")

package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.codeSearch.search.PathFinder
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
      name == it.name && context.data.isA(it.type)
    }?.let {
      try {
        it.function(params.map { param -> param.evaluate(context).toString() }, context)
      } catch (e: Exception) {
        logger.warn("Problem executing $it", e)
      }
    }

}
//Query ast
data class Query(
  val selectors: List<TypeSelector>,
  val aggregator: SelectorFunction? = null
) : Expression {
  override fun evaluate(context: ContextNode): Any? {
    val res = PathFinder(context).find(this)
    return if (aggregator != null) res.size else res
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
    return context.data[value]
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
    val values = context.data[attributeToMatch].toString().split(" ").map { it.trim().toLowerCase() }

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
    if (context.data.containsKey(name))
      logger.warn { "Trying to set the alias alias $name for an existing element " }

    return expr.evaluate(context)?.let {
      context.data.put(name, it)
      it
    }
  }
}