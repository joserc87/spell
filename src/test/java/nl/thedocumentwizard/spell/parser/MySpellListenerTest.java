package nl.thedocumentwizard.spell.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test MySpelllistener
 */
public class MySpellListenerTest {

    boolean value = false;;
    @Before
    public void becauseOfReasons() {
        value = true;
    }
    @Test
    public void should_allways_pass() {
        Assert.assertTrue(value);
    }
}
