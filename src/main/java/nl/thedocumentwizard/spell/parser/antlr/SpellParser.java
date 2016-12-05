// Generated from Spell.g4 by ANTLR 4.5.3

package nl.thedocumentwizard.spell.parser.antlr;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SpellParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, STEP=2, STRING=3, COLON=4, NEWLINE=5, INT=6;
	public static final int
		RULE_wizard = 0, RULE_step = 1;
	public static final String[] ruleNames = {
		"wizard", "step"
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

	@Override
	public String getGrammarFileName() { return "Spell.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SpellParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class WizardContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(SpellParser.EOF, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(SpellParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(SpellParser.NEWLINE, i);
		}
		public List<StepContext> step() {
			return getRuleContexts(StepContext.class);
		}
		public StepContext step(int i) {
			return getRuleContext(StepContext.class,i);
		}
		public WizardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wizard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpellListener ) ((SpellListener)listener).enterWizard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpellListener ) ((SpellListener)listener).exitWizard(this);
		}
	}

	public final WizardContext wizard() throws RecognitionException {
		WizardContext _localctx = new WizardContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_wizard);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0 || _la==NEWLINE) {
				{
				setState(6);
				switch (_input.LA(1)) {
				case NEWLINE:
					{
					setState(4);
					match(NEWLINE);
					}
					break;
				case T__0:
					{
					setState(5);
					step();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(10);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(11);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StepContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(SpellParser.STRING, 0); }
		public TerminalNode COLON() { return getToken(SpellParser.COLON, 0); }
		public TerminalNode NEWLINE() { return getToken(SpellParser.NEWLINE, 0); }
		public StepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_step; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpellListener ) ((SpellListener)listener).enterStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpellListener ) ((SpellListener)listener).exitStep(this);
		}
	}

	public final StepContext step() throws RecognitionException {
		StepContext _localctx = new StepContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_step);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(13);
			match(T__0);
			setState(14);
			match(STRING);
			setState(15);
			match(COLON);
			setState(16);
			match(NEWLINE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\b\25\4\2\t\2\4\3"+
		"\t\3\3\2\3\2\7\2\t\n\2\f\2\16\2\f\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\2\2\4\2\4\2\2\24\2\n\3\2\2\2\4\17\3\2\2\2\6\t\7\7\2\2\7\t\5\4\3\2\b\6"+
		"\3\2\2\2\b\7\3\2\2\2\t\f\3\2\2\2\n\b\3\2\2\2\n\13\3\2\2\2\13\r\3\2\2\2"+
		"\f\n\3\2\2\2\r\16\7\2\2\3\16\3\3\2\2\2\17\20\7\3\2\2\20\21\7\5\2\2\21"+
		"\22\7\6\2\2\22\23\7\7\2\2\23\5\3\2\2\2\4\b\n";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}