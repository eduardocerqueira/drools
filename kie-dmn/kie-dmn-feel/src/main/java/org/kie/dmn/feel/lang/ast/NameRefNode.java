package org.kie.dmn.feel.lang.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.kie.dmn.api.feel.runtime.events.FEELEvent;
import org.kie.dmn.feel.lang.EvaluationContext;
import org.kie.dmn.feel.lang.Type;
import org.kie.dmn.feel.util.EvalHelper;
import org.kie.dmn.feel.util.Msg;

public class NameRefNode
        extends BaseNode {

    private Type resultType;

    public NameRefNode(ParserRuleContext ctx, Type type) {
        super( ctx );
        this.resultType = type;
    }

    public NameRefNode(ParserRuleContext ctx, String text, Type type) {
        super( ctx );
        this.resultType = type;
        this.setText(text);
    }

    @Override
    public Object evaluate(EvaluationContext ctx) {
        String varName = EvalHelper.normalizeVariableName( getText() );
        if( ! ctx.isDefined( varName ) ) {
            ctx.notifyEvt( astEvent( FEELEvent.Severity.ERROR, Msg.createMessage( Msg.UNKNOWN_VARIABLE_REFERENCE, getText()), null) );
            return null;
        }
        return ctx.getValue( varName );
    }

    @Override
    public Type getResultType() {
        return resultType;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
