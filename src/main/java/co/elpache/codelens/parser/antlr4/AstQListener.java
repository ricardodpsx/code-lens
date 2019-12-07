// Generated from co/elpache/codelens/parser/antlr4/AstQ.g4 by ANTLR 4.5
package co.elpache.codelens.parser.antlr4;


import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AstQParser}.
 */
public interface AstQListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AstQParser#setQuery}.
	 * @param ctx the parse tree
	 */
	void enterSetQuery(AstQParser.SetQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#setQuery}.
	 * @param ctx the parse tree
	 */
	void exitSetQuery(AstQParser.SetQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#paramSet}.
	 * @param ctx the parse tree
	 */
	void enterParamSet(AstQParser.ParamSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#paramSet}.
	 * @param ctx the parse tree
	 */
	void exitParamSet(AstQParser.ParamSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(AstQParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(AstQParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#aggregator}.
	 * @param ctx the parse tree
	 */
	void enterAggregator(AstQParser.AggregatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#aggregator}.
	 * @param ctx the parse tree
	 */
	void exitAggregator(AstQParser.AggregatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(AstQParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(AstQParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(AstQParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(AstQParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(AstQParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(AstQParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(AstQParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(AstQParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunc(AstQParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunc(AstQParser.FuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#subQuery}.
	 * @param ctx the parse tree
	 */
	void enterSubQuery(AstQParser.SubQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#subQuery}.
	 * @param ctx the parse tree
	 */
	void exitSubQuery(AstQParser.SubQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(AstQParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(AstQParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#op0}.
	 * @param ctx the parse tree
	 */
	void enterOp0(AstQParser.Op0Context ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#op0}.
	 * @param ctx the parse tree
	 */
	void exitOp0(AstQParser.Op0Context ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#op1}.
	 * @param ctx the parse tree
	 */
	void enterOp1(AstQParser.Op1Context ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#op1}.
	 * @param ctx the parse tree
	 */
	void exitOp1(AstQParser.Op1Context ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#op2}.
	 * @param ctx the parse tree
	 */
	void enterOp2(AstQParser.Op2Context ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#op2}.
	 * @param ctx the parse tree
	 */
	void exitOp2(AstQParser.Op2Context ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#op3}.
	 * @param ctx the parse tree
	 */
	void enterOp3(AstQParser.Op3Context ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#op3}.
	 * @param ctx the parse tree
	 */
	void exitOp3(AstQParser.Op3Context ctx);
	/**
	 * Enter a parse tree produced by {@link AstQParser#reference}.
	 * @param ctx the parse tree
	 */
	void enterReference(AstQParser.ReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link AstQParser#reference}.
	 * @param ctx the parse tree
	 */
	void exitReference(AstQParser.ReferenceContext ctx);
}