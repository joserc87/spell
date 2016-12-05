// Generated from Spell.g4 by ANTLR 4.5.3

package nl.thedocumentwizard.spell.parser.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SpellParser}.
 */
public interface SpellListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SpellParser#wizard}.
	 * @param ctx the parse tree
	 */
	void enterWizard(SpellParser.WizardContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpellParser#wizard}.
	 * @param ctx the parse tree
	 */
	void exitWizard(SpellParser.WizardContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpellParser#step}.
	 * @param ctx the parse tree
	 */
	void enterStep(SpellParser.StepContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpellParser#step}.
	 * @param ctx the parse tree
	 */
	void exitStep(SpellParser.StepContext ctx);
}