package co.elpache.codelens.parser.antlr4;// Generated from co/elpache/codelens/parser/antlr4/AstQ.g4 by ANTLR 4.5

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
            T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, T__23 = 24,
            T__24 = 25, INT = 26, DECIMAL = 27, PREFIX = 28, NAME = 29, STRING = 30, SQSTRING = 31,
            DQSTRING = 32, BRK_OP = 33, BRK_CL = 34, WHITESPACE = 35;
    public static final int
            RULE_setQuery = 0, RULE_paramSet = 1, RULE_query = 2, RULE_aggregator = 3,
            RULE_type = 4, RULE_attribute = 5, RULE_relation = 6, RULE_directRelation = 7,
            RULE_followRelation = 8, RULE_expr = 9, RULE_group = 10, RULE_func = 11,
            RULE_subQuery = 12, RULE_literal = 13, RULE_op0 = 14, RULE_op1 = 15, RULE_op2 = 16,
            RULE_op3 = 17, RULE_alias = 18, RULE_reference = 19;
    public static final String[] ruleNames = {
            "setQuery", "paramSet", "query", "aggregator", "type", "attribute", "relation",
            "directRelation", "followRelation", "expr", "group", "func", "subQuery",
            "literal", "op0", "op1", "op2", "op3", "alias", "reference"
    };

    private static final String[] _LITERAL_NAMES = {
            null, "'SET'", "','", "'='", "'|'", "'-'", "'>'", "'>>'", "'('", "')'",
            "'{'", "'}'", "'*'", "'/'", "'+'", "'^='", "'*='", "'$='", "'<'", "'>='",
            "'<='", "'!='", "'&&'", "'||'", "'as'", "'$'", null, null, null, null,
            null, null, null, "'['", "']'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
            null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, "INT", "DECIMAL", "PREFIX", "NAME", "STRING", "SQSTRING",
            "DQSTRING", "BRK_OP", "BRK_CL", "WHITESPACE"
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
                setState(40);
                match(T__0);
                setState(41);
                subQuery();
                setState(42);
                paramSet();
                setState(47);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__1) {
                    {
                        {
                            setState(43);
                            match(T__1);
                            setState(44);
                            paramSet();
                        }
                    }
                    setState(49);
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
                setState(50);
                reference();
                setState(51);
                match(T__2);
                setState(52);
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
                setState(55);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(54);
                            type();
                        }
                    }
                    setState(57);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__11) | (1L << T__24) | (1L << PREFIX) | (1L << NAME))) != 0));
                setState(61);
                _la = _input.LA(1);
                if (_la == T__3) {
                    {
                        setState(59);
                        match(T__3);
                        setState(60);
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
                setState(63);
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
                setState(66);
                _la = _input.LA(1);
                if (_la == PREFIX) {
                    {
                        setState(65);
                        match(PREFIX);
                    }
                }

                setState(68);
                reference();
                setState(70);
                _la = _input.LA(1);
                if (_la == BRK_OP) {
                    {
                        setState(69);
                        attribute();
                    }
                }

                setState(73);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6))) != 0)) {
                    {
                        setState(72);
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
                setState(75);
                match(BRK_OP);
                setState(76);
                expr(0);
                setState(77);
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
        public DirectRelationContext directRelation() {
            return getRuleContext(DirectRelationContext.class, 0);
        }

        public FollowRelationContext followRelation() {
            return getRuleContext(FollowRelationContext.class, 0);
        }

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
            setState(81);
            switch (getInterpreter().adaptivePredict(_input, 6, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(79);
                    directRelation();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(80);
                    followRelation();
                }
                break;
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

    public static class DirectRelationContext extends ParserRuleContext {
        public TerminalNode NAME() {
            return getToken(AstQParser.NAME, 0);
        }

        public DirectRelationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_directRelation;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AstQListener) ((AstQListener) listener).enterDirectRelation(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AstQListener) ((AstQListener) listener).exitDirectRelation(this);
        }
    }

    public final DirectRelationContext directRelation() throws RecognitionException {
        DirectRelationContext _localctx = new DirectRelationContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_directRelation);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(85);
                _la = _input.LA(1);
                if (_la == T__4) {
                    {
                        setState(83);
                        match(T__4);
                        setState(84);
                        match(NAME);
                    }
                }

                setState(87);
                match(T__5);
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

    public static class FollowRelationContext extends ParserRuleContext {
        public TerminalNode NAME() {
            return getToken(AstQParser.NAME, 0);
        }

        public FollowRelationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_followRelation;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AstQListener) ((AstQListener) listener).enterFollowRelation(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AstQListener) ((AstQListener) listener).exitFollowRelation(this);
        }
    }

    public final FollowRelationContext followRelation() throws RecognitionException {
        FollowRelationContext _localctx = new FollowRelationContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_followRelation);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(91);
                _la = _input.LA(1);
                if (_la == T__4) {
                    {
                        setState(89);
                        match(T__4);
                        setState(90);
                        match(NAME);
                    }
                }

                setState(93);
                match(T__6);
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

        public GroupContext group() {
            return getRuleContext(GroupContext.class, 0);
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

        public AliasContext alias() {
            return getRuleContext(AliasContext.class, 0);
        }

        public TerminalNode NAME() {
            return getToken(AstQParser.NAME, 0);
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
        int _startState = 18;
        enterRecursionRule(_localctx, 18, RULE_expr, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(101);
                switch (getInterpreter().adaptivePredict(_input, 9, _ctx)) {
                    case 1: {
                        setState(96);
                        literal();
                    }
                    break;
                    case 2: {
                        setState(97);
                        subQuery();
                    }
                    break;
                    case 3: {
                        setState(98);
                        reference();
                    }
                    break;
                    case 4: {
                        setState(99);
                        func();
                    }
                    break;
                    case 5: {
                        setState(100);
                        group();
                    }
                    break;
                }
                _ctx.stop = _input.LT(-1);
                setState(125);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            setState(123);
                            switch (getInterpreter().adaptivePredict(_input, 10, _ctx)) {
                                case 1: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(103);
                                    if (!(precpred(_ctx, 5)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                    setState(104);
                                    op0();
                                    setState(105);
                                    expr(6);
                                }
                                break;
                                case 2: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(107);
                                    if (!(precpred(_ctx, 4)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    setState(108);
                                    op1();
                                    setState(109);
                                    expr(5);
                                }
                                break;
                                case 3: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(111);
                                    if (!(precpred(_ctx, 3)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                    setState(112);
                                    op2();
                                    setState(113);
                                    expr(4);
                                }
                                break;
                                case 4: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(115);
                                    if (!(precpred(_ctx, 2)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                    setState(116);
                                    op3();
                                    setState(117);
                                    expr(3);
                                }
                                break;
                                case 5: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(119);
                                    if (!(precpred(_ctx, 1)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                    setState(120);
                                    alias();
                                    setState(121);
                                    match(NAME);
                                }
                                break;
                            }
                        }
                    }
                    setState(127);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
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

    public static class GroupContext extends ParserRuleContext {
        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public GroupContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_group;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AstQListener) ((AstQListener) listener).enterGroup(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AstQListener) ((AstQListener) listener).exitGroup(this);
        }
    }

    public final GroupContext group() throws RecognitionException {
        GroupContext _localctx = new GroupContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_group);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(128);
                match(T__7);
                setState(129);
                expr(0);
                setState(130);
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
        enterRule(_localctx, 22, RULE_func);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(132);
                reference();
                setState(133);
                match(T__7);
                setState(146);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__9) | (1L << T__11) | (1L << T__24) | (1L << INT) | (1L << DECIMAL) | (1L << NAME) | (1L << STRING))) != 0)) {
                    {
                        setState(135);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        do {
                            {
                                {
                                    setState(134);
                                    expr(0);
                                }
                            }
                            setState(137);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__9) | (1L << T__11) | (1L << T__24) | (1L << INT) | (1L << DECIMAL) | (1L << NAME) | (1L << STRING))) != 0));
                        setState(143);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == T__1) {
                            {
                                {
                                    setState(139);
                                    match(T__1);
                                    setState(140);
                                    expr(0);
                                }
                            }
                            setState(145);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(148);
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
        enterRule(_localctx, 24, RULE_subQuery);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(150);
                match(T__9);
                setState(151);
                query();
                setState(152);
                match(T__10);
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

        public TerminalNode DECIMAL() {
            return getToken(AstQParser.DECIMAL, 0);
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
        enterRule(_localctx, 26, RULE_literal);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(154);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << DECIMAL) | (1L << STRING))) != 0))) {
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
        enterRule(_localctx, 28, RULE_op0);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(156);
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
        enterRule(_localctx, 30, RULE_op1);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(158);
                _la = _input.LA(1);
                if (!(_la == T__4 || _la == T__13)) {
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
        enterRule(_localctx, 32, RULE_op2);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(160);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__4) | (1L << T__5) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20))) != 0))) {
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
        enterRule(_localctx, 34, RULE_op3);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(162);
                _la = _input.LA(1);
                if (!(_la == T__21 || _la == T__22)) {
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

    public static class AliasContext extends ParserRuleContext {
        public AliasContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_alias;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AstQListener) ((AstQListener) listener).enterAlias(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AstQListener) ((AstQListener) listener).exitAlias(this);
        }
    }

    public final AliasContext alias() throws RecognitionException {
        AliasContext _localctx = new AliasContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_alias);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(164);
                match(T__23);
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
        enterRule(_localctx, 38, RULE_reference);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(166);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__11) | (1L << T__24) | (1L << NAME))) != 0))) {
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
            case 9:
                return expr_sempred((ExprContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expr_sempred(ExprContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 5);
            case 1:
                return precpred(_ctx, 4);
            case 2:
                return precpred(_ctx, 3);
            case 3:
                return precpred(_ctx, 2);
            case 4:
                return precpred(_ctx, 1);
        }
        return true;
    }

    public static final String _serializedATN =
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3%\u00ab\4\2\t\2\4" +
                    "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
                    "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
                    "\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\2\3\2\7\2\60\n\2\f\2\16\2" +
                    "\63\13\2\3\3\3\3\3\3\3\3\3\4\6\4:\n\4\r\4\16\4;\3\4\3\4\5\4@\n\4\3\5\3" +
                    "\5\3\6\5\6E\n\6\3\6\3\6\5\6I\n\6\3\6\5\6L\n\6\3\7\3\7\3\7\3\7\3\b\3\b" +
                    "\5\bT\n\b\3\t\3\t\5\tX\n\t\3\t\3\t\3\n\3\n\5\n^\n\n\3\n\3\n\3\13\3\13" +
                    "\3\13\3\13\3\13\3\13\5\13h\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13" +
                    "\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\7\13~\n\13" +
                    "\f\13\16\13\u0081\13\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\6\r\u008a\n\r\r\r" +
                    "\16\r\u008b\3\r\3\r\7\r\u0090\n\r\f\r\16\r\u0093\13\r\5\r\u0095\n\r\3" +
                    "\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23" +
                    "\3\23\3\24\3\24\3\25\3\25\3\25\2\3\24\26\2\4\6\b\n\f\16\20\22\24\26\30" +
                    "\32\34\36 \"$&(\2\b\4\2\34\35  \3\2\16\17\4\2\7\7\20\20\5\2\5\5\7\b\16" +
                    "\27\3\2\30\31\5\2\16\16\33\33\37\37\u00ab\2*\3\2\2\2\4\64\3\2\2\2\69\3" +
                    "\2\2\2\bA\3\2\2\2\nD\3\2\2\2\fM\3\2\2\2\16S\3\2\2\2\20W\3\2\2\2\22]\3" +
                    "\2\2\2\24g\3\2\2\2\26\u0082\3\2\2\2\30\u0086\3\2\2\2\32\u0098\3\2\2\2" +
                    "\34\u009c\3\2\2\2\36\u009e\3\2\2\2 \u00a0\3\2\2\2\"\u00a2\3\2\2\2$\u00a4" +
                    "\3\2\2\2&\u00a6\3\2\2\2(\u00a8\3\2\2\2*+\7\3\2\2+,\5\32\16\2,\61\5\4\3" +
                    "\2-.\7\4\2\2.\60\5\4\3\2/-\3\2\2\2\60\63\3\2\2\2\61/\3\2\2\2\61\62\3\2" +
                    "\2\2\62\3\3\2\2\2\63\61\3\2\2\2\64\65\5(\25\2\65\66\7\5\2\2\66\67\5\32" +
                    "\16\2\67\5\3\2\2\28:\5\n\6\298\3\2\2\2:;\3\2\2\2;9\3\2\2\2;<\3\2\2\2<" +
                    "?\3\2\2\2=>\7\6\2\2>@\5\b\5\2?=\3\2\2\2?@\3\2\2\2@\7\3\2\2\2AB\5\30\r" +
                    "\2B\t\3\2\2\2CE\7\36\2\2DC\3\2\2\2DE\3\2\2\2EF\3\2\2\2FH\5(\25\2GI\5\f" +
                    "\7\2HG\3\2\2\2HI\3\2\2\2IK\3\2\2\2JL\5\16\b\2KJ\3\2\2\2KL\3\2\2\2L\13" +
                    "\3\2\2\2MN\7#\2\2NO\5\24\13\2OP\7$\2\2P\r\3\2\2\2QT\5\20\t\2RT\5\22\n" +
                    "\2SQ\3\2\2\2SR\3\2\2\2T\17\3\2\2\2UV\7\7\2\2VX\7\37\2\2WU\3\2\2\2WX\3" +
                    "\2\2\2XY\3\2\2\2YZ\7\b\2\2Z\21\3\2\2\2[\\\7\7\2\2\\^\7\37\2\2][\3\2\2" +
                    "\2]^\3\2\2\2^_\3\2\2\2_`\7\t\2\2`\23\3\2\2\2ab\b\13\1\2bh\5\34\17\2ch" +
                    "\5\32\16\2dh\5(\25\2eh\5\30\r\2fh\5\26\f\2ga\3\2\2\2gc\3\2\2\2gd\3\2\2" +
                    "\2ge\3\2\2\2gf\3\2\2\2h\177\3\2\2\2ij\f\7\2\2jk\5\36\20\2kl\5\24\13\b" +
                    "l~\3\2\2\2mn\f\6\2\2no\5 \21\2op\5\24\13\7p~\3\2\2\2qr\f\5\2\2rs\5\"\22" +
                    "\2st\5\24\13\6t~\3\2\2\2uv\f\4\2\2vw\5$\23\2wx\5\24\13\5x~\3\2\2\2yz\f" +
                    "\3\2\2z{\5&\24\2{|\7\37\2\2|~\3\2\2\2}i\3\2\2\2}m\3\2\2\2}q\3\2\2\2}u" +
                    "\3\2\2\2}y\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080" +
                    "\25\3\2\2\2\u0081\177\3\2\2\2\u0082\u0083\7\n\2\2\u0083\u0084\5\24\13" +
                    "\2\u0084\u0085\7\13\2\2\u0085\27\3\2\2\2\u0086\u0087\5(\25\2\u0087\u0094" +
                    "\7\n\2\2\u0088\u008a\5\24\13\2\u0089\u0088\3\2\2\2\u008a\u008b\3\2\2\2" +
                    "\u008b\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u0091\3\2\2\2\u008d\u008e" +
                    "\7\4\2\2\u008e\u0090\5\24\13\2\u008f\u008d\3\2\2\2\u0090\u0093\3\2\2\2" +
                    "\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091" +
                    "\3\2\2\2\u0094\u0089\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0096\3\2\2\2\u0096" +
                    "\u0097\7\13\2\2\u0097\31\3\2\2\2\u0098\u0099\7\f\2\2\u0099\u009a\5\6\4" +
                    "\2\u009a\u009b\7\r\2\2\u009b\33\3\2\2\2\u009c\u009d\t\2\2\2\u009d\35\3" +
                    "\2\2\2\u009e\u009f\t\3\2\2\u009f\37\3\2\2\2\u00a0\u00a1\t\4\2\2\u00a1" +
                    "!\3\2\2\2\u00a2\u00a3\t\5\2\2\u00a3#\3\2\2\2\u00a4\u00a5\t\6\2\2\u00a5" +
                    "%\3\2\2\2\u00a6\u00a7\7\32\2\2\u00a7\'\3\2\2\2\u00a8\u00a9\t\7\2\2\u00a9" +
                    ")\3\2\2\2\21\61;?DHKSW]g}\177\u008b\u0091\u0094";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}