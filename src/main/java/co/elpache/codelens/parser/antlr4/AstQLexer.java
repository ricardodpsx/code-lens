package co.elpache.codelens.parser.antlr4;// Generated from co/elpache/codelens/parser/antlr4/AstQ.g4 by ANTLR 4.5

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AstQLexer extends Lexer {
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
    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

    public static final String[] ruleNames = {
            "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8",
            "T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16",
            "T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24",
            "INT", "DECIMAL", "PREFIX", "NAME", "STRING", "SQSTRING", "DQSTRING",
            "BRK_OP", "BRK_CL", "WHITESPACE"
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


    public AstQLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
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
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public static final String _serializedATN =
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2%\u00cd\b\1\4\2\t" +
                    "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" +
                    "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
                    "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
                    "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
                    "\t!\4\"\t\"\4#\t#\4$\t$\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3" +
                    "\6\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16" +
                    "\3\16\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23" +
                    "\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30" +
                    "\3\30\3\31\3\31\3\31\3\32\3\32\3\33\5\33\u0089\n\33\3\33\6\33\u008c\n" +
                    "\33\r\33\16\33\u008d\3\34\5\34\u0091\n\34\3\34\6\34\u0094\n\34\r\34\16" +
                    "\34\u0095\3\34\5\34\u0099\n\34\3\34\6\34\u009c\n\34\r\34\16\34\u009d\3" +
                    "\35\3\35\3\36\3\36\7\36\u00a4\n\36\f\36\16\36\u00a7\13\36\3\37\3\37\5" +
                    "\37\u00ab\n\37\3 \3 \3 \3 \7 \u00b1\n \f \16 \u00b4\13 \3 \3 \3!\3!\3" +
                    "!\3!\7!\u00bc\n!\f!\16!\u00bf\13!\3!\3!\3\"\3\"\3#\3#\3$\6$\u00c8\n$\r" +
                    "$\16$\u00c9\3$\3$\4\u00b2\u00bd\2%\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n" +
                    "\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30" +
                    "/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%\3\2\b\3\2//\3\2\62;" +
                    "\4\2%%\60\60\7\2--<<C\\aac|\6\2\62;C\\aac|\5\2\13\f\17\17\"\"\u00d9\2" +
                    "\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2" +
                    "\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2" +
                    "\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2" +
                    "\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2" +
                    "\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2" +
                    "\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2" +
                    "\3I\3\2\2\2\5M\3\2\2\2\7O\3\2\2\2\tQ\3\2\2\2\13S\3\2\2\2\rU\3\2\2\2\17" +
                    "W\3\2\2\2\21Z\3\2\2\2\23\\\3\2\2\2\25^\3\2\2\2\27`\3\2\2\2\31b\3\2\2\2" +
                    "\33d\3\2\2\2\35f\3\2\2\2\37h\3\2\2\2!k\3\2\2\2#n\3\2\2\2%q\3\2\2\2\'s" +
                    "\3\2\2\2)v\3\2\2\2+y\3\2\2\2-|\3\2\2\2/\177\3\2\2\2\61\u0082\3\2\2\2\63" +
                    "\u0085\3\2\2\2\65\u0088\3\2\2\2\67\u0090\3\2\2\29\u009f\3\2\2\2;\u00a1" +
                    "\3\2\2\2=\u00aa\3\2\2\2?\u00ac\3\2\2\2A\u00b7\3\2\2\2C\u00c2\3\2\2\2E" +
                    "\u00c4\3\2\2\2G\u00c7\3\2\2\2IJ\7U\2\2JK\7G\2\2KL\7V\2\2L\4\3\2\2\2MN" +
                    "\7.\2\2N\6\3\2\2\2OP\7?\2\2P\b\3\2\2\2QR\7~\2\2R\n\3\2\2\2ST\7/\2\2T\f" +
                    "\3\2\2\2UV\7@\2\2V\16\3\2\2\2WX\7@\2\2XY\7@\2\2Y\20\3\2\2\2Z[\7*\2\2[" +
                    "\22\3\2\2\2\\]\7+\2\2]\24\3\2\2\2^_\7}\2\2_\26\3\2\2\2`a\7\177\2\2a\30" +
                    "\3\2\2\2bc\7,\2\2c\32\3\2\2\2de\7\61\2\2e\34\3\2\2\2fg\7-\2\2g\36\3\2" +
                    "\2\2hi\7`\2\2ij\7?\2\2j \3\2\2\2kl\7,\2\2lm\7?\2\2m\"\3\2\2\2no\7&\2\2" +
                    "op\7?\2\2p$\3\2\2\2qr\7>\2\2r&\3\2\2\2st\7@\2\2tu\7?\2\2u(\3\2\2\2vw\7" +
                    ">\2\2wx\7?\2\2x*\3\2\2\2yz\7#\2\2z{\7?\2\2{,\3\2\2\2|}\7(\2\2}~\7(\2\2" +
                    "~.\3\2\2\2\177\u0080\7~\2\2\u0080\u0081\7~\2\2\u0081\60\3\2\2\2\u0082" +
                    "\u0083\7c\2\2\u0083\u0084\7u\2\2\u0084\62\3\2\2\2\u0085\u0086\7&\2\2\u0086" +
                    "\64\3\2\2\2\u0087\u0089\t\2\2\2\u0088\u0087\3\2\2\2\u0088\u0089\3\2\2" +
                    "\2\u0089\u008b\3\2\2\2\u008a\u008c\t\3\2\2\u008b\u008a\3\2\2\2\u008c\u008d" +
                    "\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\66\3\2\2\2\u008f" +
                    "\u0091\t\2\2\2\u0090\u008f\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0098\3\2" +
                    "\2\2\u0092\u0094\t\3\2\2\u0093\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095" +
                    "\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0099\7\60" +
                    "\2\2\u0098\u0093\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009b\3\2\2\2\u009a" +
                    "\u009c\t\3\2\2\u009b\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009b\3\2" +
                    "\2\2\u009d\u009e\3\2\2\2\u009e8\3\2\2\2\u009f\u00a0\t\4\2\2\u00a0:\3\2" +
                    "\2\2\u00a1\u00a5\t\5\2\2\u00a2\u00a4\t\6\2\2\u00a3\u00a2\3\2\2\2\u00a4" +
                    "\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6<\3\2\2\2" +
                    "\u00a7\u00a5\3\2\2\2\u00a8\u00ab\5? \2\u00a9\u00ab\5A!\2\u00aa\u00a8\3" +
                    "\2\2\2\u00aa\u00a9\3\2\2\2\u00ab>\3\2\2\2\u00ac\u00b2\7)\2\2\u00ad\u00ae" +
                    "\7^\2\2\u00ae\u00b1\7)\2\2\u00af\u00b1\13\2\2\2\u00b0\u00ad\3\2\2\2\u00b0" +
                    "\u00af\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b2\u00b0\3\2" +
                    "\2\2\u00b3\u00b5\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00b6\7)\2\2\u00b6" +
                    "@\3\2\2\2\u00b7\u00bd\7$\2\2\u00b8\u00b9\7^\2\2\u00b9\u00bc\7$\2\2\u00ba" +
                    "\u00bc\13\2\2\2\u00bb\u00b8\3\2\2\2\u00bb\u00ba\3\2\2\2\u00bc\u00bf\3" +
                    "\2\2\2\u00bd\u00be\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be\u00c0\3\2\2\2\u00bf" +
                    "\u00bd\3\2\2\2\u00c0\u00c1\7$\2\2\u00c1B\3\2\2\2\u00c2\u00c3\7]\2\2\u00c3" +
                    "D\3\2\2\2\u00c4\u00c5\7_\2\2\u00c5F\3\2\2\2\u00c6\u00c8\t\7\2\2\u00c7" +
                    "\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00ca\3\2" +
                    "\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cc\b$\2\2\u00ccH\3\2\2\2\20\2\u0088" +
                    "\u008d\u0090\u0095\u0098\u009d\u00a5\u00aa\u00b0\u00b2\u00bb\u00bd\u00c9" +
                    "\3\b\2\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}