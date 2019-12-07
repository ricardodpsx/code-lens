// Generated from co/elpache/codelens/parser/antlr4/AstQ.g4 by ANTLR 4.5
package co.elpache.codelens.parser.antlr4;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AstQParser extends Parser {
	static {
		RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
			new PredictionContextCache();
	public static final int
			T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
			T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14, T__14 = 15, T__15 = 16, T__16 = 17,
			T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, INT = 24, PREFIX = 25,
			NAME = 26, STRING = 27, SQSTRING = 28, DQSTRING = 29, BRK_OP = 30, BRK_CL = 31, WHITESPACE = 32;
	public static final int
			RULE_setQuery = 0, RULE_paramSet = 1, RULE_query = 2, RULE_aggregator = 3,
			RULE_type = 4, RULE_attribute = 5, RULE_relation = 6, RULE_expr = 7, RULE_func = 8,
			RULE_subQuery = 9, RULE_literal = 10, RULE_op0 = 11, RULE_op1 = 12, RULE_op2 = 13,
			RULE_op3 = 14, RULE_reference = 15;
	public static final String[] ruleNames = {
			"setQuery", "paramSet", "query", "aggregator", "type", "attribute", "relation",
			"expr", "func", "subQuery", "literal", "op0", "op1", "op2", "op3", "reference"
	};

	private static final String[] _LITERAL_NAMES = {
			null, "'SET'", "','", "'='", "'|'", "'>'", "'('", "')'", "'{'", "'}'",
			"'*'", "'/'", "'+'", "'-'", "'^='", "'*='", "'$='", "'<'", "'>='", "'<='",
			"'!='", "'&&'", "'||'", "'$'", null, null, null, null, null, null, "'['",
			"']'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
			null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null,
			"INT", "PREFIX", "NAME", "STRING", "SQSTRING", "DQSTRING", "BRK_OP", "BRK_CL",
			"WHITESPACE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() {
		return "AstQ.g4";
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String getSerializedATN() {
		return _serializedATN;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public AstQParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}
	public static class SetQueryContext extends ParserRuleContext {
		public SubQueryContext subQuery() {
			return getRuleContext(SubQueryContext.class, 0);
		}
		public List<ParamSetContext> paramSet() {
			return getRuleContexts(ParamSetContext.class);
		}
		public ParamSetContext paramSet(int i) {
			return getRuleContext(ParamSetContext.class, i);
		}
		public SetQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_setQuery;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterSetQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitSetQuery(this);
		}
	}

	public final SetQueryContext setQuery() throws RecognitionException {
		SetQueryContext _localctx = new SetQueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_setQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(32);
				match(T__0);
				setState(33);
				subQuery();
				setState(34);
				paramSet();
				setState(39);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == T__1) {
					{
						{
							setState(35);
							match(T__1);
							setState(36);
							paramSet();
						}
					}
					setState(41);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamSetContext extends ParserRuleContext {
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class, 0);
		}
		public SubQueryContext subQuery() {
			return getRuleContext(SubQueryContext.class, 0);
		}
		public ParamSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_paramSet;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterParamSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitParamSet(this);
		}
	}

	public final ParamSetContext paramSet() throws RecognitionException {
		ParamSetContext _localctx = new ParamSetContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_paramSet);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(42);
				reference();
				setState(43);
				match(T__2);
				setState(44);
				subQuery();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryContext extends ParserRuleContext {
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class, i);
		}
		public AggregatorContext aggregator() {
			return getRuleContext(AggregatorContext.class, 0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_query;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitQuery(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_query);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
						{
							setState(46);
							type();
						}
					}
					setState(49);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__22) | (1L << PREFIX) | (1L << NAME))) != 0));
				setState(53);
				_la = _input.LA(1);
				if (_la == T__3) {
					{
						setState(51);
						match(T__3);
						setState(52);
						aggregator();
					}
				}

			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AggregatorContext extends ParserRuleContext {
		public FuncContext func() {
			return getRuleContext(FuncContext.class, 0);
		}
		public AggregatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_aggregator;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterAggregator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitAggregator(this);
		}
	}

	public final AggregatorContext aggregator() throws RecognitionException {
		AggregatorContext _localctx = new AggregatorContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_aggregator);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(55);
				func();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class, 0);
		}

		public TerminalNode PREFIX() {
			return getToken(AstQParser.PREFIX, 0);
		}
		public AttributeContext attribute() {
			return getRuleContext(AttributeContext.class, 0);
		}
		public RelationContext relation() {
			return getRuleContext(RelationContext.class, 0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_type;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitType(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(58);
				_la = _input.LA(1);
				if (_la == PREFIX) {
					{
						setState(57);
						match(PREFIX);
					}
				}

				setState(60);
				reference();
				setState(62);
				_la = _input.LA(1);
				if (_la == BRK_OP) {
					{
						setState(61);
						attribute();
					}
				}

				setState(65);
				_la = _input.LA(1);
				if (_la == T__4) {
					{
						setState(64);
						relation();
					}
				}

			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeContext extends ParserRuleContext {
		public TerminalNode BRK_OP() {
			return getToken(AstQParser.BRK_OP, 0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class, 0);
		}

		public TerminalNode BRK_CL() {
			return getToken(AstQParser.BRK_CL, 0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_attribute;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitAttribute(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(67);
				match(BRK_OP);
				setState(68);
				expr(0);
				setState(69);
				match(BRK_CL);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelationContext extends ParserRuleContext {
		public RelationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_relation;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterRelation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitRelation(this);
		}
	}

	public final RelationContext relation() throws RecognitionException {
		RelationContext _localctx = new RelationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_relation);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(71);
				match(T__4);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class, 0);
		}
		public SubQueryContext subQuery() {
			return getRuleContext(SubQueryContext.class, 0);
		}
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class, 0);
		}
		public FuncContext func() {
			return getRuleContext(FuncContext.class, 0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class, i);
		}
		public Op0Context op0() {
			return getRuleContext(Op0Context.class, 0);
		}
		public Op1Context op1() {
			return getRuleContext(Op1Context.class, 0);
		}
		public Op2Context op2() {
			return getRuleContext(Op2Context.class, 0);
		}
		public Op3Context op3() {
			return getRuleContext(Op3Context.class, 0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_expr;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 14;
		enterRecursionRule(_localctx, 14, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(82);
				switch (getInterpreter().adaptivePredict(_input, 6, _ctx)) {
					case 1: {
						setState(74);
						literal();
					}
					break;
					case 2: {
						setState(75);
						subQuery();
					}
					break;
					case 3: {
						setState(76);
						reference();
					}
					break;
					case 4: {
						setState(77);
						func();
					}
					break;
					case 5: {
						setState(78);
						match(T__5);
						setState(79);
						expr(0);
						setState(80);
						match(T__6);
					}
					break;
				}
				_ctx.stop = _input.LT(-1);
				setState(102);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 8, _ctx);
				while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (_parseListeners != null) triggerExitRuleEvent();
						_prevctx = _localctx;
						{
							setState(100);
							switch (getInterpreter().adaptivePredict(_input, 7, _ctx)) {
								case 1: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(84);
									if (!(precpred(_ctx, 4)))
										throw new FailedPredicateException(this, "precpred(_ctx, 4)");
									setState(85);
									op0();
									setState(86);
									expr(5);
								}
								break;
								case 2: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(88);
									if (!(precpred(_ctx, 3)))
										throw new FailedPredicateException(this, "precpred(_ctx, 3)");
									setState(89);
									op1();
									setState(90);
									expr(4);
								}
								break;
								case 3: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(92);
									if (!(precpred(_ctx, 2)))
										throw new FailedPredicateException(this, "precpred(_ctx, 2)");
									setState(93);
									op2();
									setState(94);
									expr(3);
								}
								break;
								case 4: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(96);
									if (!(precpred(_ctx, 1)))
										throw new FailedPredicateException(this, "precpred(_ctx, 1)");
									setState(97);
									op3();
									setState(98);
									expr(2);
								}
								break;
							}
						}
					}
					setState(104);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 8, _ctx);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FuncContext extends ParserRuleContext {
		public ReferenceContext reference() {
			return getRuleContext(ReferenceContext.class, 0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class, i);
		}
		public FuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_func;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitFunc(this);
		}
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_func);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(105);
				reference();
				setState(121);
				switch (getInterpreter().adaptivePredict(_input, 11, _ctx)) {
					case 1: {
						setState(106);
						match(T__5);
						setState(108);
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
								{
									setState(107);
									expr(0);
								}
							}
							setState(110);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__7) | (1L << T__9) | (1L << T__22) | (1L << INT) | (1L << NAME) | (1L << STRING))) != 0));
						setState(116);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la == T__1) {
							{
								{
									setState(112);
									match(T__1);
									setState(113);
									expr(0);
								}
							}
							setState(118);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(119);
						match(T__6);
					}
					break;
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubQueryContext extends ParserRuleContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class, 0);
		}
		public SubQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_subQuery;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterSubQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitSubQuery(this);
		}
	}

	public final SubQueryContext subQuery() throws RecognitionException {
		SubQueryContext _localctx = new SubQueryContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_subQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(123);
				match(T__7);
				setState(124);
				query();
				setState(125);
				match(T__8);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode INT() {
			return getToken(AstQParser.INT, 0);
		}

		public TerminalNode STRING() {
			return getToken(AstQParser.STRING, 0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_literal;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitLiteral(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(127);
				_la = _input.LA(1);
				if (!(_la == INT || _la == STRING)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op0Context extends ParserRuleContext {
		public Op0Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_op0;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterOp0(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitOp0(this);
		}
	}

	public final Op0Context op0() throws RecognitionException {
		Op0Context _localctx = new Op0Context(_ctx, getState());
		enterRule(_localctx, 22, RULE_op0);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(129);
				_la = _input.LA(1);
				if (!(_la == T__9 || _la == T__10)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op1Context extends ParserRuleContext {
		public Op1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_op1;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterOp1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitOp1(this);
		}
	}

	public final Op1Context op1() throws RecognitionException {
		Op1Context _localctx = new Op1Context(_ctx, getState());
		enterRule(_localctx, 24, RULE_op1);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(131);
				_la = _input.LA(1);
				if (!(_la == T__11 || _la == T__12)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op2Context extends ParserRuleContext {
		public Op2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_op2;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterOp2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitOp2(this);
		}
	}

	public final Op2Context op2() throws RecognitionException {
		Op2Context _localctx = new Op2Context(_ctx, getState());
		enterRule(_localctx, 26, RULE_op2);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(133);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__4) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op3Context extends ParserRuleContext {
		public Op3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_op3;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterOp3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitOp3(this);
		}
	}

	public final Op3Context op3() throws RecognitionException {
		Op3Context _localctx = new Op3Context(_ctx, getState());
		enterRule(_localctx, 28, RULE_op3);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(135);
				_la = _input.LA(1);
				if (!(_la == T__20 || _la == T__21)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReferenceContext extends ParserRuleContext {
		public TerminalNode NAME() {
			return getToken(AstQParser.NAME, 0);
		}
		public ReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_reference;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).enterReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof AstQListener) ((AstQListener) listener).exitReference(this);
		}
	}

	public final ReferenceContext reference() throws RecognitionException {
		ReferenceContext _localctx = new ReferenceContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(137);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__22) | (1L << NAME))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
			case 7:
				return expr_sempred((ExprContext) _localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
			case 0:
				return precpred(_ctx, 4);
			case 1:
				return precpred(_ctx, 3);
			case 2:
				return precpred(_ctx, 2);
			case 3:
				return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
			"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\"\u008e\4\2\t\2\4" +
					"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
					"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\3" +
					"\2\3\2\3\2\7\2(\n\2\f\2\16\2+\13\2\3\3\3\3\3\3\3\3\3\4\6\4\62\n\4\r\4" +
					"\16\4\63\3\4\3\4\5\48\n\4\3\5\3\5\3\6\5\6=\n\6\3\6\3\6\5\6A\n\6\3\6\5" +
					"\6D\n\6\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5" +
					"\tU\n\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3" +
					"\t\7\tg\n\t\f\t\16\tj\13\t\3\n\3\n\3\n\6\no\n\n\r\n\16\np\3\n\3\n\7\n" +
					"u\n\n\f\n\16\nx\13\n\3\n\3\n\5\n|\n\n\3\13\3\13\3\13\3\13\3\f\3\f\3\r" +
					"\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\21\2\3\20\22\2\4\6\b\n" +
					"\f\16\20\22\24\26\30\32\34\36 \2\b\4\2\32\32\35\35\3\2\f\r\3\2\16\17\5" +
					"\2\5\5\7\7\f\26\3\2\27\30\5\2\f\f\31\31\34\34\u008e\2\"\3\2\2\2\4,\3\2" +
					"\2\2\6\61\3\2\2\2\b9\3\2\2\2\n<\3\2\2\2\fE\3\2\2\2\16I\3\2\2\2\20T\3\2" +
					"\2\2\22k\3\2\2\2\24}\3\2\2\2\26\u0081\3\2\2\2\30\u0083\3\2\2\2\32\u0085" +
					"\3\2\2\2\34\u0087\3\2\2\2\36\u0089\3\2\2\2 \u008b\3\2\2\2\"#\7\3\2\2#" +
					"$\5\24\13\2$)\5\4\3\2%&\7\4\2\2&(\5\4\3\2\'%\3\2\2\2(+\3\2\2\2)\'\3\2" +
					"\2\2)*\3\2\2\2*\3\3\2\2\2+)\3\2\2\2,-\5 \21\2-.\7\5\2\2./\5\24\13\2/\5" +
					"\3\2\2\2\60\62\5\n\6\2\61\60\3\2\2\2\62\63\3\2\2\2\63\61\3\2\2\2\63\64" +
					"\3\2\2\2\64\67\3\2\2\2\65\66\7\6\2\2\668\5\b\5\2\67\65\3\2\2\2\678\3\2" +
					"\2\28\7\3\2\2\29:\5\22\n\2:\t\3\2\2\2;=\7\33\2\2<;\3\2\2\2<=\3\2\2\2=" +
					">\3\2\2\2>@\5 \21\2?A\5\f\7\2@?\3\2\2\2@A\3\2\2\2AC\3\2\2\2BD\5\16\b\2" +
					"CB\3\2\2\2CD\3\2\2\2D\13\3\2\2\2EF\7 \2\2FG\5\20\t\2GH\7!\2\2H\r\3\2\2" +
					"\2IJ\7\7\2\2J\17\3\2\2\2KL\b\t\1\2LU\5\26\f\2MU\5\24\13\2NU\5 \21\2OU" +
					"\5\22\n\2PQ\7\b\2\2QR\5\20\t\2RS\7\t\2\2SU\3\2\2\2TK\3\2\2\2TM\3\2\2\2" +
					"TN\3\2\2\2TO\3\2\2\2TP\3\2\2\2Uh\3\2\2\2VW\f\6\2\2WX\5\30\r\2XY\5\20\t" +
					"\7Yg\3\2\2\2Z[\f\5\2\2[\\\5\32\16\2\\]\5\20\t\6]g\3\2\2\2^_\f\4\2\2_`" +
					"\5\34\17\2`a\5\20\t\5ag\3\2\2\2bc\f\3\2\2cd\5\36\20\2de\5\20\t\4eg\3\2" +
					"\2\2fV\3\2\2\2fZ\3\2\2\2f^\3\2\2\2fb\3\2\2\2gj\3\2\2\2hf\3\2\2\2hi\3\2" +
					"\2\2i\21\3\2\2\2jh\3\2\2\2k{\5 \21\2ln\7\b\2\2mo\5\20\t\2nm\3\2\2\2op" +
					"\3\2\2\2pn\3\2\2\2pq\3\2\2\2qv\3\2\2\2rs\7\4\2\2su\5\20\t\2tr\3\2\2\2" +
					"ux\3\2\2\2vt\3\2\2\2vw\3\2\2\2wy\3\2\2\2xv\3\2\2\2yz\7\t\2\2z|\3\2\2\2" +
					"{l\3\2\2\2{|\3\2\2\2|\23\3\2\2\2}~\7\n\2\2~\177\5\6\4\2\177\u0080\7\13" +
					"\2\2\u0080\25\3\2\2\2\u0081\u0082\t\2\2\2\u0082\27\3\2\2\2\u0083\u0084" +
					"\t\3\2\2\u0084\31\3\2\2\2\u0085\u0086\t\4\2\2\u0086\33\3\2\2\2\u0087\u0088" +
					"\t\5\2\2\u0088\35\3\2\2\2\u0089\u008a\t\6\2\2\u008a\37\3\2\2\2\u008b\u008c" +
					"\t\7\2\2\u008c!\3\2\2\2\16)\63\67<@CTfhpv{";
	public static final ATN _ATN =
			new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}