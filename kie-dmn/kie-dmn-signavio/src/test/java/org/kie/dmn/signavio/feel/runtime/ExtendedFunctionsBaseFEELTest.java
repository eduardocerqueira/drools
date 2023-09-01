package org.kie.dmn.signavio.feel.runtime;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kie.dmn.api.feel.runtime.events.FEELEvent;
import org.kie.dmn.api.feel.runtime.events.FEELEventListener;
import org.kie.dmn.feel.FEEL;
import org.kie.dmn.signavio.KieDMNSignavioProfile;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public abstract class ExtendedFunctionsBaseFEELTest {

    private final FEEL feel = FEEL.newInstance(List.of(new KieDMNSignavioProfile()));

    @Parameterized.Parameter(0)
    public String expression;

    @Parameterized.Parameter(1)
    public Object result;

    @Parameterized.Parameter(2)
    public FEELEvent.Severity severity;

    @Test
    public void testExpression() {
        FEELEventListener listener = mock( FEELEventListener.class );
        feel.addListener( listener );
        feel.addListener( evt -> {
            System.out.println(evt);
        } );
        assertResult(expression, result);

        if( severity != null ) {
            ArgumentCaptor<FEELEvent> captor = ArgumentCaptor.forClass( FEELEvent.class );
            verify( listener , atLeastOnce()).onEvent( captor.capture() );
            assertThat(captor.getValue().getSeverity()).isEqualTo(severity);
        } else {
            verify( listener, never() ).onEvent( any(FEELEvent.class) );
        }
    }

    protected void assertResult(String expression, Object result) {
        if (result == null) {
            assertThat(feel.evaluate(expression)).as("Evaluating: '" + expression + "'").isNull();
        } else if (result instanceof Class<?>) {
        	assertThat(feel.evaluate(expression)).as("Evaluating: '" + expression + "'").isInstanceOf((Class<?>) result);
        } else {
        	assertThat(feel.evaluate(expression)).as("Evaluating: '" + expression + "'").isEqualTo(result);
        }
    }
}
