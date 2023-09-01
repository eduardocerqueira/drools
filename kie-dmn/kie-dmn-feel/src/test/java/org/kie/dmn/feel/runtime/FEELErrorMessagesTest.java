package org.kie.dmn.feel.runtime;

import org.junit.Test;
import org.kie.dmn.api.feel.runtime.events.FEELEvent;
import org.kie.dmn.api.feel.runtime.events.FEELEventListener;
import org.kie.dmn.feel.FEEL;
import org.kie.dmn.feel.lang.CompilerContext;
import org.kie.dmn.feel.runtime.events.SyntaxErrorEvent;
import org.kie.dmn.feel.runtime.events.UnknownVariableErrorEvent;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;



public class FEELErrorMessagesTest {

    @Test
    public void unknownVariable() {
        final FEEL feel = FEEL.newInstance();
        final FEELEventListener fel = Mockito.mock(FEELEventListener.class );
        feel.addListener( fel );

        final CompilerContext ctx = feel.newCompilerContext();
        feel.compile( "a variable name", ctx );

        final ArgumentCaptor<FEELEvent> captor = ArgumentCaptor.forClass(FEELEvent.class );
        verify( fel, times(2) ).onEvent( captor.capture() );

        assertThat(captor.getAllValues()).hasSize(2);
        assertThat(captor.getAllValues().get(1)).isInstanceOf( UnknownVariableErrorEvent.class);
        assertThat(captor.getAllValues().get(1).getOffendingSymbol()).isEqualTo("a variable name");
    }

    @Test
    public void ifWithoutElse() {
        final FEEL feel = FEEL.newInstance();
        final FEELEventListener fel = Mockito.mock(FEELEventListener.class);
        feel.addListener(fel);

        final CompilerContext ctx = feel.newCompilerContext();
        feel.compile("if true then 123", ctx);

        final ArgumentCaptor<FEELEvent> captor = ArgumentCaptor.forClass(FEELEvent.class);
        verify(fel, times(1)).onEvent(captor.capture());

        assertThat(captor.getAllValues()).hasSize(1);
        assertThat(captor.getAllValues().get(0)).isInstanceOfAny(SyntaxErrorEvent.class);
        assertThat(captor.getAllValues().get(0).getMessage()).startsWith("Detected 'if' expression without 'else' part");
    }

    @Test
    public void ifWithoutElse2() {
        final FEEL feel = FEEL.newInstance();
        final FEELEventListener fel = Mockito.mock(FEELEventListener.class);
        feel.addListener(fel);

        final CompilerContext ctx = feel.newCompilerContext();
        feel.compile("if true then 123 456", ctx);

        final ArgumentCaptor<FEELEvent> captor = ArgumentCaptor.forClass(FEELEvent.class);
        verify(fel, times(1)).onEvent(captor.capture());

        assertThat(captor.getAllValues()).hasSize(1);
        assertThat(captor.getAllValues().get(0)).isInstanceOf(SyntaxErrorEvent.class);
        assertThat(captor.getAllValues().get(0).getMessage()).isEqualTo("missing 'else' at '456'");
    }

    @Test
    public void ifWithoutThen() {
        final FEEL feel = FEEL.newInstance();
        final FEELEventListener fel = Mockito.mock(FEELEventListener.class);
        feel.addListener(fel);

        final CompilerContext ctx = feel.newCompilerContext();
        feel.compile("if true", ctx);

        final ArgumentCaptor<FEELEvent> captor = ArgumentCaptor.forClass(FEELEvent.class);
        verify(fel, times(1)).onEvent(captor.capture());

        assertThat(captor.getAllValues()).hasSize(1);
        assertThat(captor.getAllValues().get(0)).isInstanceOf(SyntaxErrorEvent.class);
        assertThat(captor.getAllValues().get(0).getMessage()).startsWith("Detected 'if' expression without 'then' part");
    }

    @Test
    public void ifWithoutThen2() {
        final FEEL feel = FEEL.newInstance();
        final FEELEventListener fel = Mockito.mock(FEELEventListener.class);
        feel.addListener(fel);

        final CompilerContext ctx = feel.newCompilerContext();
        feel.compile("if true 123", ctx);

        final ArgumentCaptor<FEELEvent> captor = ArgumentCaptor.forClass(FEELEvent.class);
        verify(fel, times(2)).onEvent(captor.capture());

        assertThat(captor.getAllValues()).hasSize(2);
        assertThat(captor.getAllValues().get(0)).isInstanceOf(SyntaxErrorEvent.class);
        assertThat(captor.getAllValues().get(0).getMessage()).startsWith("missing 'then' at '123'");
        assertThat(captor.getAllValues().get(1)).isInstanceOf(SyntaxErrorEvent.class);
        assertThat(captor.getAllValues().get(1).getMessage()).startsWith("Detected 'if' expression without 'then' part");
    }

}
