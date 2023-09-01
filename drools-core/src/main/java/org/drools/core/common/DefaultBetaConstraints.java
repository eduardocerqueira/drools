package org.drools.core.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.drools.base.base.ObjectType;
import org.drools.base.rule.ContextEntry;
import org.drools.base.rule.IndexableConstraint;
import org.drools.base.rule.MutableTypeConstraint;
import org.drools.base.rule.Pattern;
import org.drools.base.rule.constraint.BetaNodeFieldConstraint;
import org.drools.core.RuleBaseConfiguration;
import org.drools.core.reteoo.BetaMemory;
import org.drools.core.reteoo.Tuple;
import org.drools.core.reteoo.builder.BuildContext;
import org.drools.core.util.bitmask.BitMask;
import org.drools.core.util.index.IndexFactory;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.conf.IndexPrecedenceOption;

import static org.drools.base.reteoo.PropertySpecificUtil.getEmptyPropertyReactiveMask;
import static org.drools.base.util.index.IndexUtil.compositeAllowed;
import static org.drools.base.util.index.IndexUtil.isIndexableForNode;


public class DefaultBetaConstraints
    implements
    BetaConstraints {

    private static final long serialVersionUID = 510l;

    protected transient boolean           disableIndexing;

    protected BetaNodeFieldConstraint[]   constraints;

    protected IndexPrecedenceOption       indexPrecedenceOption;

    protected int                         indexed;

    private transient Boolean           leftUpdateOptimizationAllowed;

    public DefaultBetaConstraints() {

    }
    public DefaultBetaConstraints(final BetaNodeFieldConstraint[] constraints,
                                  final RuleBaseConfiguration conf) {
        this( constraints,
              conf,
              false );

    }

    public DefaultBetaConstraints(final BetaNodeFieldConstraint[] constraints,
                                  final RuleBaseConfiguration conf,
                                  final boolean disableIndexing) {
        this.constraints = constraints;
        this.disableIndexing = disableIndexing;
        this.indexPrecedenceOption = conf.getIndexPrecedenceOption();
    }

    public DefaultBetaConstraints cloneIfInUse() {
        if (constraints[0] instanceof MutableTypeConstraint && ((MutableTypeConstraint)constraints[0]).setInUse()) {
            BetaNodeFieldConstraint[] clonedConstraints = new BetaNodeFieldConstraint[constraints.length];
            for (int i = 0; i < constraints.length; i++) {
                clonedConstraints[i] = constraints[i].cloneIfInUse();
            }
            DefaultBetaConstraints clone = new DefaultBetaConstraints();
            clone.constraints = clonedConstraints;
            clone.disableIndexing = disableIndexing;
            clone.indexPrecedenceOption = indexPrecedenceOption;
            clone.indexed = indexed;
            return clone;
        }
        return this;
    }

    public void init(BuildContext context, short betaNodeType) {
        RuleBaseConfiguration config = context.getRuleBase().getRuleBaseConfiguration();

        if ( disableIndexing || (!config.isIndexLeftBetaMemory() && !config.isIndexRightBetaMemory()) ) {
            indexed = 0;
        } else {
            int depth = config.getCompositeKeyDepth();
            if ( !compositeAllowed( constraints, betaNodeType, config ) ) {
                // UnificationRestrictions cannot be allowed in composite indexes
                // We also ensure that if there is a mixture that standard restriction is first
                depth = 1;
            }
            initIndexes( depth, betaNodeType, config );
        }
    }

    public void initIndexes(int depth, short betaNodeType, RuleBaseConfiguration config) {
        indexed = 0;
        boolean[] indexable = isIndexableForNode(indexPrecedenceOption, betaNodeType, depth, constraints, config);
        for (boolean i : indexable) {
            if (i) {
                indexed++;
            } else {
                break;
            }
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        constraints = (BetaNodeFieldConstraint[])in.readObject();
        indexed     = in.readInt();
        indexPrecedenceOption = (IndexPrecedenceOption) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(constraints);
        out.writeInt(indexed);
        out.writeObject(indexPrecedenceOption);
    }

    public ContextEntry[] createContext() {
        ContextEntry[] entries = new ContextEntry[constraints.length];
        for (int i = 0; i < constraints.length; i++) {
            entries[i] = constraints[i].createContextEntry();
        }
        return entries;
    }

    /* (non-Javadoc)
     * @see org.kie.common.BetaNodeConstraints#updateFromTuple(org.kie.reteoo.ReteTuple)
     */
    public void updateFromTuple(final ContextEntry[] context,
                                final ReteEvaluator reteEvaluator,
                                final Tuple tuple) {
        for (ContextEntry aContext : context) {
            aContext.updateFromTuple(reteEvaluator, tuple);
        }
    }

    /* (non-Javadoc)
     * @see org.kie.common.BetaNodeConstraints#updateFromFactHandle(org.kie.common.InternalFactHandle)
     */
    public void updateFromFactHandle(final ContextEntry[] context,
                                     final ReteEvaluator reteEvaluator,
                                     final FactHandle handle) {
        for (ContextEntry aContext : context) {
            aContext.updateFromFactHandle(reteEvaluator, handle);
        }
    }

    public void resetTuple(final ContextEntry[] context) {
        for (ContextEntry aContext : context) {
            aContext.resetTuple();
        }
    }

    public void resetFactHandle(final ContextEntry[] context) {
        for (ContextEntry aContext : context) {
            aContext.resetFactHandle();
        }
    }

    /* (non-Javadoc)
     * @see org.kie.common.BetaNodeConstraints#isAllowedCachedLeft(java.lang.Object)
     */
    public boolean isAllowedCachedLeft(final ContextEntry[] context,
                                       final FactHandle handle) {
        for (int i = indexed; i < constraints.length; i++) {
            if ( !constraints[i].isAllowedCachedLeft(context[i], handle) ) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.kie.common.BetaNodeConstraints#isAllowedCachedRight(org.kie.reteoo.ReteTuple)
     */
    public boolean isAllowedCachedRight(final ContextEntry[] context,
                                        final Tuple tuple) {
        for (int i = indexed; i < constraints.length; i++) {
            if ( !constraints[i].isAllowedCachedRight(tuple, context[i]) ) {
                return false;
            }
        }
        return true;
    }

    public boolean isIndexed() {
        return this.indexed > 0;
    }

    public int getIndexCount() {
        return this.indexed;
    }

    public boolean isEmpty() {
        return false;
    }

    public BetaMemory createBetaMemory(final RuleBaseConfiguration config,
                                       final short nodeType ) {
        return IndexFactory.createBetaMemory(config, nodeType, constraints);
    }

    public int hashCode() {
        return Arrays.hashCode(constraints);
    }

    /* (non-Javadoc)
     * @see org.kie.common.BetaNodeConstraints#getConstraints()
     */
    public BetaNodeFieldConstraint[] getConstraints() {
        return constraints;
    }

    /**
     * Determine if another object is equal to this.
     *
     * @param object
     *            The object to test.
     *
     * @return <code>true</code> if <code>object</code> is equal to this,
     *         otherwise <code>false</code>.
     */
    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }

        if ( !(object instanceof DefaultBetaConstraints) ) {
            return false;
        }

        final DefaultBetaConstraints other = (DefaultBetaConstraints) object;

        if ( this.constraints == other.constraints ) {
            return true;
        }

        if ( this.constraints.length != other.constraints.length ) {
            return false;
        }

        return Arrays.equals(constraints, other.constraints );
    }
    public BetaConstraints getOriginalConstraint() {
        throw new UnsupportedOperationException();
    }

    public BitMask getListenedPropertyMask(Pattern pattern, ObjectType modifiedType, List<String> settableProperties) {
        BitMask mask = getEmptyPropertyReactiveMask(settableProperties.size());
        for (BetaNodeFieldConstraint constraint : constraints) {
            mask = mask.setAll(constraint.getListenedPropertyMask(Optional.of(pattern), modifiedType, settableProperties));
        }
        return mask;
    }

    public boolean isLeftUpdateOptimizationAllowed() {
        if (leftUpdateOptimizationAllowed == null) {
            leftUpdateOptimizationAllowed = calcLeftUpdateOptimizationAllowed();
        }
        return leftUpdateOptimizationAllowed;
    }

    private boolean calcLeftUpdateOptimizationAllowed() {
        for (BetaNodeFieldConstraint constraint : constraints) {
            if ( !(constraint instanceof IndexableConstraint && ((IndexableConstraint)constraint).getConstraintType().isEquality()) ) {
                return false;
            }
        }
        return true;
    }

    public void registerEvaluationContext(BuildContext buildContext) {
        for (int i = 0; i < constraints.length; i++) {
            constraints[i].registerEvaluationContext(buildContext);
        }
    }
}
