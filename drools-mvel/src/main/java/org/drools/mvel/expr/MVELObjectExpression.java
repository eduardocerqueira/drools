package org.drools.mvel.expr;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.base.base.ValueResolver;
import org.drools.base.definitions.InternalKnowledgePackage;
import org.drools.base.definitions.rule.impl.RuleImpl;
import org.drools.base.reteoo.BaseTuple;
import org.drools.base.rule.Declaration;
import org.drools.core.time.TimerExpression;
import org.drools.kiesession.rulebase.InternalKnowledgeBase;
import org.drools.mvel.MVELDialectRuntimeData;
import org.mvel2.ParserConfiguration;
import org.mvel2.integration.VariableResolverFactory;

import static org.drools.mvel.expr.MvelEvaluator.createMvelEvaluator;

public class MVELObjectExpression implements MVELCompileable, TimerExpression, Externalizable {

    private static final long   serialVersionUID = 510l;

    private MVELCompilationUnit unit;
    private String              id;

    private MvelEvaluator<Object> evaluator;

    public MVELObjectExpression() {
    }

    public MVELObjectExpression(final MVELCompilationUnit unit,
                                final String id) {
        this.unit = unit;
        this.id = id;
    }

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        unit = (MVELCompilationUnit) in.readObject();
        id = in.readUTF();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject( unit );
        out.writeUTF( id );
    }
    
    public MVELCompilationUnit getMVELCompilationUnit() {
        return this.unit;
    }    

    public void compile( MVELDialectRuntimeData runtimeData) {
        evaluator = createMvelEvaluator( unit.getCompiledExpression( runtimeData ) );
    }

    public void compile(ParserConfiguration conf) {
        evaluator = createMvelEvaluator( unit.getCompiledExpression( conf ) );
    }

    public void compile( MVELDialectRuntimeData runtimeData, RuleImpl rule ) {
        evaluator = createMvelEvaluator( unit.getCompiledExpression( runtimeData, rule.toRuleNameAndPathString() ) );
    }

    @Override
    public Declaration[] getDeclarations() {
        return unit.getPreviousDeclarations();
    }

    @Override
    public Object getValue(BaseTuple leftTuple, Declaration[] declrs, ValueResolver valueResolver) {
        VariableResolverFactory factory = unit.getFactory( null, declrs,
                                                           null, null, leftTuple, null, valueResolver, valueResolver.getGlobalResolver() );
        
        // do we have any functions for this namespace?
        InternalKnowledgePackage pkg = ((InternalKnowledgeBase)valueResolver.getRuleBase()).getPackage("MAIN");
        if ( pkg != null ) {
            MVELDialectRuntimeData data = ( MVELDialectRuntimeData ) pkg.getDialectRuntimeRegistry().getDialectData( this.id );
            factory.setNextFactory( data.getFunctionFactory() );
        }

        return evaluator.evaluate(factory);
    }
    
    public String toString() {
        return this.unit.getExpression();
    }

}
