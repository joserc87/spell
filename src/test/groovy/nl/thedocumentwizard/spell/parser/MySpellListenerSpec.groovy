package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.MyObjectFactory;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.nio.charset.StandardCharsets;

class MySpellListenerSpec extends spock.lang.Specification {

    def 'parse spell from string'() {
        given: 'a spell with 2 steps'
            def spell = "\n" +
                "\nstep 'My step':" +
                "\n    'question'" +
                "\ninclude 'file'" +
                "\nstep 'another step':" +
                "\n     'another question'";
            def inputStream = new ByteArrayInputStream(spell.getBytes(StandardCharsets.UTF_8));
        and: 'a spell listener'
            def helper = new ParsingHelper();
            def aliasHelper = new StepAliasHelper();
            def factory = new MyObjectFactory();
            def controlParser = new ControlParser(factory, helper);
            def whenParser = new WhenParser(factory, helper);
            def listener = new MySpellListener(factory, controlParser, whenParser, helper, aliasHelper);
        and: 'a tokenized input stream'
            def lexer = new SpellLexer(new ANTLRInputStream(inputStream));
            def tokens = new CommonTokenStream(lexer);
            def parser = new SpellParser(tokens);
            def wizardSentenceContext = parser.wizard();
        when: 'we walk through the tokens, notifying the listener'
            (new ParseTreeWalker()).walk(listener, wizardSentenceContext);
        then: 'the wizard should have 2 steps'
            def wizard = listener.getWizard()
            wizard.steps.step.size == 2
        and: 'the name of the first step should be "My step"'
            wizard.steps.step[0].name == 'My step'
        and: 'the name of the second step should be "another step"'
            wizard.steps.step[1].name == 'another step'
    }

    def 'parse spell from file'() {
        given: 'a spell file '
            def file = new File(getClass().getResource("/basic_example.spl").getFile());
            def inputStream = new FileInputStream(file);
        and: 'a spell listener'
            def helper = new ParsingHelper();
            def aliasHelper = new StepAliasHelper();
            def factory = new MyObjectFactory();
            def controlParser = new ControlParser(factory, helper);
            def whenParser = new WhenParser(factory, helper);
            def listener = new MySpellListener(factory, controlParser, whenParser, helper, aliasHelper);
        and: 'a tokenized input stream'
            def lexer = new SpellLexer(new ANTLRInputStream(inputStream));
            def tokens = new CommonTokenStream(lexer);
            def parser = new SpellParser(tokens);
            def wizardSentenceContext = parser.wizard();
        when: 'we walk through the tokens, notifying the listener'
            (new ParseTreeWalker()).walk(listener, wizardSentenceContext);
        then: 'the wizard should have 2 steps'
            def wizard = listener.getWizard()
            wizard.steps.step.size == 2
        and: 'the name of the first step should be "Step 1"'
            wizard.steps.step[0].name == 'step 1'
        and: 'the name of the second step should be "Step 2"'
            wizard.steps.step[1].name == 'step 2'
    }

}
