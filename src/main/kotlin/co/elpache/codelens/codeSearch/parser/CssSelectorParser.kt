@file:Suppress("Reformat")

package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.parser.antlr4.AstQBaseListener
import co.elpache.codelens.parser.antlr4.AstQLexer
import co.elpache.codelens.parser.antlr4.AstQParser
import co.elpache.codelens.unwrap
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker


fun parseSetQuery(query: String): SetQuery {

  val parser = antlr4GetParser(query)
  val walker = ParseTreeWalker()
  var res: SetQuery? = null
  walker.walk(object : AstQBaseListener() {
    override fun exitSetQuery(ctx: AstQParser.SetQueryContext?) {
      res = SetQuery(
        nodesToSet = ctx!!.subQuery().query().toQuery(),
        paramSetters = ctx.paramSet().map {
          ParamSet(paramName = it.reference().text, query = it.subQuery().query().toQuery())
        }
      )
    }
  }, parser.setQuery())


  return res!!
}

fun parseQuery(query: String): Query {
  val parser = antlr4GetParser(query)
  val walker = ParseTreeWalker()

  var res: Query? = null

  walker.walk(object : AstQBaseListener() {
    override fun exitQuery(ctx: AstQParser.QueryContext?) {
      res = ctx.toQuery()
    }
  }, parser.query())

  return res!!;
}

private fun antlr4GetParser(query: String): AstQParser {
  val input = ANTLRInputStream(query)
// create a lexer that feeds off of input CharStream
  val lexer = AstQLexer(input)
// create a buffer of tokens pulled from the lexer
  val tokens = CommonTokenStream(lexer)
// create a parser that feeds off the tokens buffer

  val parser = AstQParser(tokens)
  return parser
}

fun AstQParser.FuncContext?.toFunction(): Function? {
  if (this == null) return null

  return Function(name = this.reference().text,
    params = this.expr().map { it.toExpression() }
  )
}

fun AstQParser.QueryContext?.toQuery(): Query {
  return Query(
    selectors = this!!.type().map {
      TypeSelector(
        it.reference().text,
        relationType = if (it.relation() == null)
          RelationType.CHILDREN
        else
          RelationType.DIRECT_DESCENDANT,
        attributeToMatch = if (it.PREFIX()?.toString() == "#") "name" else "type",
        expr = it.attribute()?.expr().toExpression()
      )
    },
    aggregator = this.aggregator()?.func().toFunction()
  )
}

fun AstQParser.ExprContext?.toExpression(): Expression {
  val op = listOf(this?.op0(), this?.op1(), this?.op2(), this?.op3()).find { it != null }

  return if (this == null)
    NullExpression()
  else if (this.op0() != null || this.op1() != null || this.op2() != null || this.op3() != null) {
    BinnaryExpression(
      left = this.expr(0).toExpression(),
      right = this.expr(1).toExpression(),
      op = op!!.text
    )
  } else if (this.literal() != null) this.literal().toLiteralExpression()
  else if (this.reference() != null) NameExpression(this.text)
  else if (this.subQuery() != null) this.subQuery().query().toQuery()
  else NullExpression()
}

fun AstQParser.LiteralContext.toLiteralExpression(): LiteralExpression {
  return if (this.INT() != null) LiteralExpression(this.INT().text.toInt())
  else LiteralExpression(this.STRING().text.unwrap())
}