package org.drools.core.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Optional;

import org.drools.base.base.ObjectType;
import org.drools.base.rule.ContextEntry;
import org.drools.base.rule.MutableTypeConstraint;
import org.drools.base.rule.Pattern;
import org.drools.base.rule.constraint.BetaNodeFieldConstraint;
import org.drools.core.RuleBaseConfiguration;
import org.drools.core.reteoo.BetaMemory;
import org.drools.core.reteoo.Tuple;
import org.drools.core.reteoo.builder.BuildContext;
import org.drools.core.util.bitmask.BitMask;
import org.kie.api.runtime.rule.FactHandle;

public class SingleNonIndexSkipBetaConstraints 
    implements
    BetaConstraints {
    
    private SingleBetaConstraints constraints;
    
    private BetaNodeFieldConstraint constraint;
    
    public SingleNonIndexSkipBetaConstraints() {

    }

    public SingleNonIndexSkipBetaConstraints cloneIfInUse() {
        if (constraint instanceof MutableTypeConstraint && ((MutableTypeConstraint)constraint).setInUse()) {
            return new SingleNonIndexSkipBetaConstraints(constraints.cloneIfInUse());
        }
        return this;
    }

    public SingleNonIndexSkipBetaConstraints(SingleBetaConstraints constraints) {
        this.constraints = constraints;
        this.constraint = constraints.getConstraint();
    }

    public void init(BuildContext context, short betaNodeType) {
        constraints.init(context, betaNodeType);
    }

    public void initIndexes(int depth, short betaNodeType, RuleBaseConfiguration config) {
        constraints.initIndexes(depth, betaNodeType, config);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException( );
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        throw new UnsupportedOperationException( );
    }
    
    public BetaConstraints getOriginalConstraint() {
        return this.constraints;
    }

    public ContextEntry[] createContext() {
        return constraints.createContext();
    }

    public void updateFromTuple(ContextEntry[] context,
                                ReteEvaluator reteEvaluator,
                                Tuple tuple) {
        constraints.updateFromTuple( context, reteEvaluator, tuple );
    }

    public void updateFromFactHandle(ContextEntry[] context,
                                     ReteEvaluator reteEvaluator,
                                     FactHandle handle) {
        constraints.updateFromFactHandle( context, reteEvaluator, handle );
    }

    public boolean isIndexed() {
        return constraints.isIndexed();
    }

    public int getIndexCount() {
        return constraints.getIndexCount();
    }

    public boolean isEmpty() {
        return constraints.isEmpty();
    }

    public BetaMemory createBetaMemory(final RuleBaseConfiguration config,
                                       final short nodeType) {
        return constraints.createBetaMemory( config,
                                             nodeType );
    }

    public int hashCode() {
        return constraints.hashCode();
    }

    public BetaNodeFieldConstraint[] getConstraints() {
        return constraints.getConstraints();
    }

    public boolean equals(Object object) {
        return object instanceof SingleNonIndexSkipBetaConstraints && constraints.equals( ((SingleNonIndexSkipBetaConstraints)object).constraints );
    }

    public void resetFactHandle(ContextEntry[] context) {
        constraints.resetFactHandle( context );
    }

    public void resetTuple(ContextEntry[] context) {
        constraints.resetTuple( context );
    }

    public String toString() {
        return constraints.toString();
    }

    public boolean isAllowedCachedLeft(final ContextEntry[] context,
                                       final FactHandle handle) {
        return this.constraint.isAllowedCachedLeft( context[0],
                                                     handle );
    }

    public boolean isAllowedCachedRight(ContextEntry[] context,
                                        Tuple tuple) {
        return this.constraints.isAllowedCachedRight( context, tuple );
    }

    public BitMask getListenedPropertyMask(Pattern pattern, ObjectType modifiedType, List<String> settableProperties) {
        return constraint.getListenedPropertyMask(Optional.of(pattern), modifiedType, settableProperties);
    }

    public boolean isLeftUpdateOptimizationAllowed() {
        return true;
    }

    public void registerEvaluationContext(BuildContext buildContext) {
        this.constraint.registerEvaluationContext(buildContext);
    }
}
