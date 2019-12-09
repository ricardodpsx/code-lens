@file:Suppress("Reformat")

package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.codeSearch.search.PathFinder
import mu.KotlinLogging

data class ParamSet(val paramName: String, val query: Query)
data class SetQuery(val nodesToSet: Query, val paramSetters: List<ParamSet>)
enum class RelationType {
  CHILDREN, DIRECT_DESCENDANT
}


data class Function(
  val name: String,
  val params: List<Expression>
)

//Query ast
data class Query(
  val selectors: List<TypeSelector>,
  val aggregator: Function? = null
) : Expression {
  override fun evaluate(ctx: ContextNode): Any? {
    val res = PathFinder(ContextNode(ctx.vid, ctx.tree)).find(this)
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
      || ((value as? List<*>)?.size == 0)
      )

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
        "=" -> leftVal == rightVal
        "*=" -> leftVal.contains(rightVal)
        "^=" -> leftVal.startsWith(rightVal)
        "$=" -> leftVal.endsWith(rightVal)
        ">" -> leftVal.toDouble() > rightVal.toDouble()
        "<" -> leftVal.toDouble() < rightVal.toDouble()
        ">=" -> leftVal.toDouble() >= rightVal.toDouble()
        "<=" -> leftVal.toDouble() <= rightVal.toDouble()
        "!=" -> leftVal != rightVal
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

data class TypeSelector(
  val name: String,
  val relationType: RelationType,
  val attributeToMatch: String = "type",
  val expr: Expression
) {

  fun matches(ctx: ContextNode): Boolean {
    val ce = ctx.data

    if (name != "*" && ce[attributeToMatch].toString().toLowerCase() != name.toLowerCase())
      return false

    if (expr is NullExpression) return true

    return isThruty(expr.evaluate(ctx))
  }

}

data class AttributeSelector(
  val name: String,
  val op: String? = null,
  val value: String? = null
)