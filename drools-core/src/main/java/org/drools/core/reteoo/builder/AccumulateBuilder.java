package org.drools.core.reteoo.builder;

import java.util.ArrayList;
import java.util.List;

import org.drools.core.common.BetaConstraints;
import org.drools.core.reteoo.AccumulateNode;
import org.drools.core.reteoo.CoreComponentFactory;
import org.drools.core.reteoo.LeftTupleSource;
import org.drools.core.reteoo.RightInputAdapterNode;
import org.drools.base.rule.Accumulate;
import org.drools.base.rule.GroupElement;
import org.drools.base.rule.RuleConditionElement;
import org.drools.base.rule.constraint.AlphaNodeFieldConstraint;
import org.drools.base.rule.constraint.BetaNodeFieldConstraint;

public class AccumulateBuilder
        implements
        ReteooComponentBuilder {

    /**
     * @inheritDoc
     */
    public void build(final BuildContext context,
                      final BuildUtils utils,
                      final RuleConditionElement rce) {
        final Accumulate accumulate = (Accumulate) rce;
        context.pushRuleComponent( accumulate );

        final List<BetaNodeFieldConstraint> resultBetaConstraints = context.getBetaconstraints();
        final List<AlphaNodeFieldConstraint> resultAlphaConstraints = context.getAlphaConstraints();

        RuleConditionElement source = accumulate.getSource();
        if( source instanceof GroupElement ) {
            GroupElement ge = (GroupElement) source;
            if( ge.isAnd() && ge.getChildren().size() == 1 ) {
                source = ge.getChildren().get( 0 );
            }
        }

        // get builder for the pattern
        final ReteooComponentBuilder builder = utils.getBuilderFor( source );

        // save tuple source and current pattern offset for later if needed
        LeftTupleSource tupleSource = context.getTupleSource();

        // builds the source pattern
        builder.build( context,
                       utils,
                       source );

        // if object source is null, then we need to adapt tuple source into a subnetwork
        if ( context.getObjectSource() == null ) {
            // attach right input adapter node to convert tuple source into an object source
            RightInputAdapterNode riaNode = CoreComponentFactory.get().getNodeFactoryService().buildRightInputNode( context.getNextNodeId(),
                                                                                                                       context.getTupleSource(),
                                                                                                                       tupleSource,
                                                                                                                       context );

            // attach right input adapter node to convert tuple source into an object source
            context.setObjectSource( utils.attachNode( context, riaNode ) );

            // restore tuple source from before the start of the sub network
            context.setTupleSource( tupleSource );

            // create a tuple start equals constraint and set it in the context
            final List<BetaNodeFieldConstraint> betaConstraints = new ArrayList<>();
            context.setBetaconstraints( betaConstraints ); // Empty list ensures EmptyBetaConstraints is assigned
        }

        NodeFactory nfactory = CoreComponentFactory.get().getNodeFactoryService();

        final BetaConstraints resultsBinder = utils.createBetaNodeConstraint( context,
                                                                              resultBetaConstraints,
                                                                              true );
        final BetaConstraints sourceBinder = utils.createBetaNodeConstraint( context,
                                                                             context.getBetaconstraints(),
                                                                             false );

        AccumulateNode accNode = nfactory.buildAccumulateNode(context.getNextNodeId(),
                                                              context.getTupleSource(),
                                                              context.getObjectSource(),
                                                              resultAlphaConstraints.toArray(new AlphaNodeFieldConstraint[resultAlphaConstraints.size()]),
                                                              sourceBinder,
                                                              resultsBinder,
                                                              accumulate,
                                                              context);

        context.setTupleSource( utils.attachNode( context, accNode ) );

        // source pattern was bound, so nulling context
        context.setObjectSource( null );
        context.popRuleComponent();
    }

    /**
     * @inheritDoc
     */
    public boolean requiresLeftActivation(final BuildUtils utils,
                                          final RuleConditionElement rce) {
        return true;
    }

}
