package org.drools.metric.common;

import org.drools.core.RuleBaseConfiguration;
import org.drools.core.common.DoubleBetaConstraints;
import org.drools.base.rule.ContextEntry;
import org.drools.base.rule.MutableTypeConstraint;
import org.drools.base.rule.constraint.BetaNodeFieldConstraint;
import org.drools.core.reteoo.Tuple;
import org.drools.metric.util.MetricLogUtils;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.conf.IndexPrecedenceOption;

public class DoubleBetaConstraintsMetric extends DoubleBetaConstraints {

    private static final long serialVersionUID = 510l;

    public DoubleBetaConstraintsMetric() {}

    public DoubleBetaConstraintsMetric(final BetaNodeFieldConstraint[] constraints,
                                       final RuleBaseConfiguration conf) {
        super(constraints, conf);
    }

    public DoubleBetaConstraintsMetric(final BetaNodeFieldConstraint[] constraints,
                                       final RuleBaseConfiguration conf,
                                       final boolean disableIndexing) {
        super(constraints, conf, disableIndexing);
    }

    protected DoubleBetaConstraintsMetric(BetaNodeFieldConstraint[] constraints,
                                          IndexPrecedenceOption indexPrecedenceOption,
                                          boolean disableIndexing) {
        super(constraints, indexPrecedenceOption, disableIndexing);
    }

    @Override
    public DoubleBetaConstraintsMetric cloneIfInUse() {
        if (constraints[0] instanceof MutableTypeConstraint && ((MutableTypeConstraint) constraints[0]).setInUse()) {
            BetaNodeFieldConstraint[] clonedConstraints = new BetaNodeFieldConstraint[constraints.length];
            for (int i = 0; i < constraints.length; i++) {
                clonedConstraints[i] = constraints[i].cloneIfInUse();
            }
            DoubleBetaConstraintsMetric clone = new DoubleBetaConstraintsMetric(clonedConstraints, indexPrecedenceOption, disableIndexing);
            clone.indexed = indexed;
            return clone;
        }
        return this;
    }

    @Override
    public boolean isAllowedCachedLeft(final ContextEntry[] context,
                                       final FactHandle handle) {
        MetricLogUtils.getInstance().incrementEvalCount();
        return super.isAllowedCachedLeft(context, handle);
    }

    @Override
    public boolean isAllowedCachedRight(final ContextEntry[] context,
                                        final Tuple tuple) {
        MetricLogUtils.getInstance().incrementEvalCount();
        return super.isAllowedCachedRight(context, tuple);
    }
}
