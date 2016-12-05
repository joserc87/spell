
package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.parser.SpellBaseListener;
import nl.thedocumentwizard.spell.parser.SpellParser;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ObjectFactory;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Wizard;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MySpellListener extends SpellBaseListener {

    private ObjectFactory objectFactory;
    private Wizard wizard;
    public MySpellListener(ObjectFactory factory) {
        this.objectFactory = factory;
    }

    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public void enterWizard(SpellParser.WizardContext ctx) {
        this.wizard = objectFactory.createWizard();
        this.wizard.setSteps(objectFactory.createArrayOfSteptype());
        for (SpellParser.StepContext stepContext : ctx.step()) {
            this.wizard.getSteps().getStep().add(this.createStep(stepContext));
        }
        System.out.println("Wizard parsed");
    }

    // Helper methods

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

    protected Steptype createStep(SpellParser.StepContext ctx) {
        Steptype step = objectFactory.createSteptype();
        String name = ctx.STRING(0).getText();
        step.setName(name.substring(1, name.length()-1));
        String group = null;
        if (ctx.STRING().size() > 1) {
            group = ctx.STRING(1).getText();
            step.setGroupName(group.substring(1, group.length()-1));
        }
        return step;
    }

}
