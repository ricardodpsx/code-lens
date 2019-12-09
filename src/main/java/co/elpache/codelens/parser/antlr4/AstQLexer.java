// Generated from co/elpache/codelens/parser/antlr4/AstQ.g4 by ANTLR 4.5
package co.elpache.codelens.parser.antlr4;

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
			T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, INT = 24, PREFIX = 25,
			NAME = 26, STRING = 27, SQSTRING = 28, DQSTRING = 29, BRK_OP = 30, BRK_CL = 31, WHITESPACE = 32;
	public static String[] modeNames = {
			"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8",
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16",
			"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "INT", "PREFIX",
			"NAME", "STRING", "SQSTRING", "DQSTRING", "BRK_OP", "BRK_CL", "WHITESPACE"
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
			"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\"\u00ae\b\1\4\2\t" +
					"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" +
					"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
					"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
					"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
					"\t!\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3" +
					"\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\20" +
					"\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\25" +
					"\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\31\6\31}\n\31\r\31" +
					"\16\31~\3\32\3\32\3\33\3\33\7\33\u0085\n\33\f\33\16\33\u0088\13\33\3\34" +
					"\3\34\5\34\u008c\n\34\3\35\3\35\3\35\3\35\7\35\u0092\n\35\f\35\16\35\u0095" +
					"\13\35\3\35\3\35\3\36\3\36\3\36\3\36\7\36\u009d\n\36\f\36\16\36\u00a0" +
					"\13\36\3\36\3\36\3\37\3\37\3 \3 \3!\6!\u00a9\n!\r!\16!\u00aa\3!\3!\4\u0093" +
					"\u009e\2\"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33" +
					"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67" +
					"\359\36;\37= ?!A\"\3\2\7\3\2\62;\4\2%%\60\60\5\2C\\aac|\6\2\62;C\\aac" +
					"|\5\2\13\f\17\17\"\"\u00b5\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2" +
					"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2" +
					"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3" +
					"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2" +
					"\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67" +
					"\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\3C\3\2" +
					"\2\2\5G\3\2\2\2\7I\3\2\2\2\tK\3\2\2\2\13M\3\2\2\2\rO\3\2\2\2\17Q\3\2\2" +
					"\2\21S\3\2\2\2\23U\3\2\2\2\25W\3\2\2\2\27Y\3\2\2\2\31[\3\2\2\2\33]\3\2" +
					"\2\2\35_\3\2\2\2\37b\3\2\2\2!e\3\2\2\2#h\3\2\2\2%j\3\2\2\2\'m\3\2\2\2" +
					")p\3\2\2\2+s\3\2\2\2-v\3\2\2\2/y\3\2\2\2\61|\3\2\2\2\63\u0080\3\2\2\2" +
					"\65\u0082\3\2\2\2\67\u008b\3\2\2\29\u008d\3\2\2\2;\u0098\3\2\2\2=\u00a3" +
					"\3\2\2\2?\u00a5\3\2\2\2A\u00a8\3\2\2\2CD\7U\2\2DE\7G\2\2EF\7V\2\2F\4\3" +
					"\2\2\2GH\7.\2\2H\6\3\2\2\2IJ\7?\2\2J\b\3\2\2\2KL\7~\2\2L\n\3\2\2\2MN\7" +
					"@\2\2N\f\3\2\2\2OP\7*\2\2P\16\3\2\2\2QR\7+\2\2R\20\3\2\2\2ST\7}\2\2T\22" +
					"\3\2\2\2UV\7\177\2\2V\24\3\2\2\2WX\7,\2\2X\26\3\2\2\2YZ\7\61\2\2Z\30\3" +
					"\2\2\2[\\\7-\2\2\\\32\3\2\2\2]^\7/\2\2^\34\3\2\2\2_`\7`\2\2`a\7?\2\2a" +
					"\36\3\2\2\2bc\7,\2\2cd\7?\2\2d \3\2\2\2ef\7&\2\2fg\7?\2\2g\"\3\2\2\2h" +
					"i\7>\2\2i$\3\2\2\2jk\7@\2\2kl\7?\2\2l&\3\2\2\2mn\7>\2\2no\7?\2\2o(\3\2" +
					"\2\2pq\7#\2\2qr\7?\2\2r*\3\2\2\2st\7(\2\2tu\7(\2\2u,\3\2\2\2vw\7~\2\2" +
					"wx\7~\2\2x.\3\2\2\2yz\7&\2\2z\60\3\2\2\2{}\t\2\2\2|{\3\2\2\2}~\3\2\2\2" +
					"~|\3\2\2\2~\177\3\2\2\2\177\62\3\2\2\2\u0080\u0081\t\3\2\2\u0081\64\3" +
					"\2\2\2\u0082\u0086\t\4\2\2\u0083\u0085\t\5\2\2\u0084\u0083\3\2\2\2\u0085" +
					"\u0088\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\66\3\2\2" +
					"\2\u0088\u0086\3\2\2\2\u0089\u008c\59\35\2\u008a\u008c\5;\36\2\u008b\u0089" +
					"\3\2\2\2\u008b\u008a\3\2\2\2\u008c8\3\2\2\2\u008d\u0093\7)\2\2\u008e\u008f" +
					"\7^\2\2\u008f\u0092\7)\2\2\u0090\u0092\13\2\2\2\u0091\u008e\3\2\2\2\u0091" +
					"\u0090\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0094\3\2\2\2\u0093\u0091\3\2" +
					"\2\2\u0094\u0096\3\2\2\2\u0095\u0093\3\2\2\2\u0096\u0097\7)\2\2\u0097" +
					":\3\2\2\2\u0098\u009e\7$\2\2\u0099\u009a\7^\2\2\u009a\u009d\7$\2\2\u009b" +
					"\u009d\13\2\2\2\u009c\u0099\3\2\2\2\u009c\u009b\3\2\2\2\u009d\u00a0\3" +
					"\2\2\2\u009e\u009f\3\2\2\2\u009e\u009c\3\2\2\2\u009f\u00a1\3\2\2\2\u00a0" +
					"\u009e\3\2\2\2\u00a1\u00a2\7$\2\2\u00a2<\3\2\2\2\u00a3\u00a4\7]\2\2\u00a4" +
					">\3\2\2\2\u00a5\u00a6\7_\2\2\u00a6@\3\2\2\2\u00a7\u00a9\t\6\2\2\u00a8" +
					"\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2" +
					"\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad\b!\2\2\u00adB\3\2\2\2\13\2~\u0086" +
					"\u008b\u0091\u0093\u009c\u009e\u00aa\3\b\2\2";
	public static final ATN _ATN =
			new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}