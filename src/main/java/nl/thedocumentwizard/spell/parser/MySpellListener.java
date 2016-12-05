
package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.parser.antlr.SpellBaseListener;
import nl.thedocumentwizard.spell.parser.antlr.SpellParser;

public class MySpellListener extends SpellBaseListener {

    @Override
    public void enterWizard(SpellParser.WizardContext ctx) {
         System.out.println("Detected wizard:");
         System.out.println(ctx.getText());
    }

    @Override
    public void enterStep(SpellParser.StepContext ctx) {
         System.out.println("Detected step:");
         System.out.println(ctx.getText());
    }

}
