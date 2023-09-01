package org.drools.modelcompiler.constraints;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import org.drools.base.base.ValueResolver;
import org.drools.base.reteoo.BaseTuple;
import org.drools.base.rule.Declaration;
import org.drools.base.rule.accessor.Accumulator;
import org.drools.core.common.ReteEvaluator;
import org.drools.core.reteoo.SubnetworkTuple;
import org.kie.api.runtime.rule.AccumulateFunction;
import org.kie.api.runtime.rule.FactHandle;

public abstract class LambdaAccumulator implements Accumulator {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LambdaAccumulator that = (LambdaAccumulator) o;
        return Objects.equals(accumulateFunction, that.accumulateFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accumulateFunction);
    }

    private final AccumulateFunction accumulateFunction;

    protected LambdaAccumulator(AccumulateFunction accumulateFunction) {
        this.accumulateFunction = accumulateFunction;
    }

    @Override
    public Object createWorkingMemoryContext() {
        // no working memory context needed
        return null;
    }

    @Override
    public Object createContext() {
        return accumulateFunction.createContext();
    }

    @Override
    public Object init(Object workingMemoryContext, Object context, BaseTuple leftTuple, Declaration[] declarations, ValueResolver valueResolver) {
        context = accumulateFunction.initContext( (Serializable) context );
        return context;
    }

    @Override
    public Object accumulate(Object workingMemoryContext, Object context, BaseTuple leftTuple, FactHandle handle, Declaration[] declarations, Declaration[] innerDeclarations, ValueResolver valueResolver) {
        final Object accumulatedObject = getAccumulatedObject(declarations, innerDeclarations, handle, leftTuple, (ReteEvaluator) valueResolver);
        return accumulateFunction.accumulateValue( (Serializable) context, accumulatedObject);
    }

    protected abstract Object getAccumulatedObject( Declaration[] declarations, Declaration[] innerDeclarations, FactHandle handle, BaseTuple tuple, ReteEvaluator reteEvaluator );

    @Override
    public boolean supportsReverse() {
        return accumulateFunction.supportsReverse();
    }

    @Override
    public boolean tryReverse(Object workingMemoryContext, Object context, BaseTuple leftTuple, FactHandle handle, Object value,
                              Declaration[] declarations, Declaration[] innerDeclarations, ValueResolver valueResolver) {
        if (value == null) {
            throw new IllegalStateException("Reversing a not existing accumulated object for fact " + handle);
        }
        return accumulateFunction.tryReverse( (Serializable) context, value);
    }

    @Override
    public Object getResult(Object workingMemoryContext, Object context, BaseTuple leftTuple, Declaration[] declarations, ValueResolver valueResolver) {
        try {
            return accumulateFunction.getResult( (Serializable) context );
        } catch (Exception e) {
            throw new RuntimeException( e );
        }
    }

    public static class BindingAcc extends LambdaAccumulator {
        private final BindingEvaluator binding;
        private final Collection<String> sourceVariables;

        public BindingAcc(AccumulateFunction accumulateFunction, Collection<String> sourceVariables, BindingEvaluator binding) {
            super(accumulateFunction);
            this.binding = binding;
            this.sourceVariables = sourceVariables;
        }

        @Override
        protected Object getAccumulatedObject( Declaration[] declarations, Declaration[] innerDeclarations, FactHandle handle, BaseTuple tuple, ReteEvaluator reteEvaluator ) {
            Object accumulateObject = handle.getObject();
            if (accumulateObject instanceof SubnetworkTuple ) {
                Declaration[] bindingDeclarations = binding.getDeclarations();
                Object[] args;
                if (bindingDeclarations == null || bindingDeclarations.length == 0) {
                    args = new Object[ sourceVariables.size() ];
                    int i = 0;
                    for (String sourceVariable : sourceVariables) {
                        for (Declaration d : innerDeclarations) {
                            if (d.getIdentifier().equals( sourceVariable )) {
                                args[i] = d.getValue( ( SubnetworkTuple ) accumulateObject );
                                break;
                            }
                        }
                        i++;
                    }
                } else { // Return values in the order required by the binding.
                    args = new Object[ bindingDeclarations.length ];
                    for (int i = 0; i < bindingDeclarations.length; i++) {
                        args[i] = bindingDeclarations[i].getValue( ( SubnetworkTuple ) accumulateObject );
                    }
                }
                return binding.evaluate(args);
            } else {
                return binding.evaluate(handle, tuple, reteEvaluator, declarations, innerDeclarations);
            }
        }
    }

    public static class NotBindingAcc extends LambdaAccumulator {

        public NotBindingAcc(AccumulateFunction accumulateFunction) {
            super(accumulateFunction);
        }

        @Override
        protected Object getAccumulatedObject( Declaration[] declarations, Declaration[] innerDeclarations, FactHandle handle, BaseTuple tuple, ReteEvaluator reteEvaluator ) {
            Object accumulateObject = handle.getObject();
            if (accumulateObject instanceof SubnetworkTuple && declarations.length > 0) {
                return declarations[0].getValue( ( SubnetworkTuple ) accumulateObject );
            } else {
                return accumulateObject;
            }
        }
    }

    public static class FixedValueAcc extends LambdaAccumulator {

        private final Object value;

        public FixedValueAcc(AccumulateFunction accumulateFunction, Object value) {
            super(accumulateFunction);
            this.value = value;
        }

        @Override
        protected Object getAccumulatedObject( Declaration[] declarations, Declaration[] innerDeclarations, FactHandle handle, BaseTuple tuple, ReteEvaluator reteEvaluator ) {
            return value;
        }
    }

}
