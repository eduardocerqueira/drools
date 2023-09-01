package org.drools.mvel.parser.ast.expr;

import java.util.List;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import org.drools.mvel.parser.ast.visitor.DrlGenericVisitor;
import org.drools.mvel.parser.ast.visitor.DrlVoidVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

public class OOPathChunk extends Expression {

    private final SimpleName field;
    private final SimpleName inlineCast;
    private final List<DrlxExpression> condition;
    private boolean singleValue;
    private boolean passive;

    public OOPathChunk(TokenRange range, SimpleName field, SimpleName inlineCast, List<DrlxExpression> condition ) {
        super( range );
        this.field = field;
        this.inlineCast = inlineCast;
        this.condition = condition;
    }

    public SimpleName getField() {
        return field;
    }

    public SimpleName getInlineCast() {
        return inlineCast;
    }

    public List<DrlxExpression> getConditions() {
        return condition;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return ((DrlGenericVisitor<R, A>)v).visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        ((DrlVoidVisitor<A>)v).visit(this, arg);
    }

    public OOPathChunk singleValue() {
        singleValue = true;
        return this;
    }

    public boolean isSingleValue() {
        return singleValue;
    }

    public OOPathChunk passive() {
        passive = true;
        return this;
    }

    public boolean isPassive() {
        return passive;
    }
}
