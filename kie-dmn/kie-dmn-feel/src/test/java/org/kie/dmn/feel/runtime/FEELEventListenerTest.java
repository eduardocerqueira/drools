package org.kie.dmn.feel.runtime;

import org.junit.Before;
import org.junit.Test;
import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.FEEL;

import static org.assertj.core.api.Assertions.assertThat;

public class FEELEventListenerTest {

    private static final String LISTENER_OUTPUT = "Listener output";

    private FEEL feel;

    private String testVariable;

    @Before
    public void setup() {
        testVariable = null;
        feel = FEEL.newInstance();
        feel.addListener(event -> testVariable = LISTENER_OUTPUT);
        feel.addListener(System.out::println);
        feel.addListener( (evt) -> { if (evt.getSeverity() == Severity.ERROR) System.err.println(evt); } );
    }

    @Test
    public void testParserError() {
        feel.evaluate( "10 + / 5" );
        assertThat(testVariable).isEqualTo(LISTENER_OUTPUT);
    }
    
    @Test
    public void testSomeBuiltinFunctions() {
        System.out.println( feel.evaluate("append( null, 1, 2 )") );
        assertThat(testVariable).isEqualTo(LISTENER_OUTPUT);
    }
}
