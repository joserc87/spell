// Generated from Spell.g4 by ANTLR 4.5.3

package nl.thedocumentwizard.spell.parser.antlr;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SpellLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, STEP=2, STRING=3, COLON=4, NEWLINE=5, INT=6;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "STEP", "STRING", "STRING_ESCAPE_SEQ", "COLON", "NEWLINE", "INT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'step '", "'step'", null, "':'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, "STEP", "STRING", "COLON", "NEWLINE", "INT"
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


	public SpellLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Spell.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\b?\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\7\4 \n\4\f\4\16\4#\13\4\3\4\3\4\3\4\3"+
		"\4\7\4)\n\4\f\4\16\4,\13\4\3\4\5\4/\n\4\3\5\3\5\3\5\3\6\3\6\3\7\6\7\67"+
		"\n\7\r\7\16\78\3\b\6\b<\n\b\r\b\16\b=\2\2\t\3\3\5\4\7\5\t\2\13\6\r\7\17"+
		"\b\3\2\6\6\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\4\2\f\f\17\17\3\2\62;D\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\3\21\3\2\2\2\5\27\3\2\2\2\7.\3\2\2\2\t\60\3\2\2\2\13\63\3\2\2\2\r\66"+
		"\3\2\2\2\17;\3\2\2\2\21\22\7u\2\2\22\23\7v\2\2\23\24\7g\2\2\24\25\7r\2"+
		"\2\25\26\7\"\2\2\26\4\3\2\2\2\27\30\7u\2\2\30\31\7v\2\2\31\32\7g\2\2\32"+
		"\33\7r\2\2\33\6\3\2\2\2\34!\7)\2\2\35 \5\t\5\2\36 \n\2\2\2\37\35\3\2\2"+
		"\2\37\36\3\2\2\2 #\3\2\2\2!\37\3\2\2\2!\"\3\2\2\2\"$\3\2\2\2#!\3\2\2\2"+
		"$/\7)\2\2%*\7$\2\2&)\5\t\5\2\')\n\3\2\2(&\3\2\2\2(\'\3\2\2\2),\3\2\2\2"+
		"*(\3\2\2\2*+\3\2\2\2+-\3\2\2\2,*\3\2\2\2-/\7$\2\2.\34\3\2\2\2.%\3\2\2"+
		"\2/\b\3\2\2\2\60\61\7^\2\2\61\62\13\2\2\2\62\n\3\2\2\2\63\64\7<\2\2\64"+
		"\f\3\2\2\2\65\67\t\4\2\2\66\65\3\2\2\2\678\3\2\2\28\66\3\2\2\289\3\2\2"+
		"\29\16\3\2\2\2:<\t\5\2\2;:\3\2\2\2<=\3\2\2\2=;\3\2\2\2=>\3\2\2\2>\20\3"+
		"\2\2\2\n\2\37!(*.8=\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}