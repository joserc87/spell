
package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.parser.SpellBaseListener;
import nl.thedocumentwizard.spell.parser.SpellParser;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MySpellListener extends SpellBaseListener {

    @Override
    public void enterWizard(SpellParser.WizardContext ctx) {
         System.out.println("Detected wizard:");
         System.out.println(ctx.getText());
    }

    @Override
    public void enterStep(SpellParser.StepContext ctx) {
        String name = ctx.STRING(0).getText();
        name = name.substring(1, name.length()-1);
        String group = null;
        if (ctx.STRING().size() > 1) {
            group = ctx.STRING(1).getText();
            group = group.substring(1, group.length()-1);
        }
        System.out.println("Detected step with name " + name + (group == null ? " and no group" : " and group " + group));
    }

    private String getString(TerminalNode node) {
        String s = null;
        if (node != null && node.getText() != null) {
            s = node.getText();
            if (s.length() < 2) {
                System.err.println("String " + s + " is not of correct size");
            } else {
                s = s.substring(1, s.length() - 1);
            }
        }
        return s;
    }

}
