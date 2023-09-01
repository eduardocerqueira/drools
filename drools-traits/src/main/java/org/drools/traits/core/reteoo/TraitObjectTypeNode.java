package org.drools.traits.core.reteoo;

import java.util.BitSet;
import java.util.Collection;

import org.drools.core.common.InternalFactHandle;
import org.drools.core.common.ReteEvaluator;
import org.drools.base.factmodel.traits.Thing;
import org.drools.base.factmodel.traits.TraitType;
import org.drools.core.reteoo.EntryPointNode;
import org.drools.core.reteoo.ModifyPreviousTuples;
import org.drools.core.reteoo.ObjectTypeNode;
import org.drools.base.reteoo.PropertySpecificUtil;
import org.drools.core.reteoo.RuntimeComponentFactory;
import org.drools.core.reteoo.builder.BuildContext;
import org.drools.base.base.ObjectType;
import org.drools.core.common.PropagationContext;
import org.drools.core.util.bitmask.BitMask;
import org.drools.traits.core.factmodel.TraitProxyImpl;
import org.drools.traits.core.factmodel.TraitTypeMapImpl;

import static org.drools.traits.core.base.TraitUtils.supersetOrEqualset;

public class TraitObjectTypeNode extends ObjectTypeNode {

    private BitSet typeMask;

    public TraitObjectTypeNode() { }

    public TraitObjectTypeNode(int id, EntryPointNode source, ObjectType objectType, BuildContext context ) {
        super( id, source, objectType, context );

        typeMask = ((TraitRuntimeComponentFactory) RuntimeComponentFactory.get()).getTraitRegistry(context.getRuleBase()).getHierarchy().getCode( objectType.getClassName() );
    }

    @Override
    public void propagateAssert( InternalFactHandle factHandle, PropagationContext context, ReteEvaluator reteEvaluator ) {
        if ( isAssertAllowed( factHandle ) ) {
            super.propagateAssert( factHandle, context, reteEvaluator );
        }
    }

    private boolean isAssertAllowed( InternalFactHandle factHandle ) {
        if ( factHandle.isTraiting() )  {
            TraitProxyImpl proxy = (TraitProxyImpl) factHandle.getObject();
            BitSet vetoMask = proxy.computeInsertionVetoMask();
            boolean vetoed = ( vetoMask != null
                               && ! typeMask.isEmpty()
                               && supersetOrEqualset( vetoMask, this.typeMask ) );

            boolean allowed = ! vetoed || sameAndNotCoveredByDescendants((TraitProxyImpl) factHandle.getObject(), typeMask );
            if ( allowed ) {
                //System.err.println(" INSERT PASS !! " + factHandle.getObject() + " " + ( (TraitProxy) factHandle.getObject() )._getTypeCode() + " >> " + vetoMask + " checks in " + typeMask );
                proxy.assignOtn( this.typeMask );
            } else {
                //System.err.println(" INSERT BLOCK !! " + factHandle.getObject() + " " + ( (TraitProxy) factHandle.getObject() )._getTypeCode() + " >> " + vetoMask + " checks in " + typeMask );
            }
            return allowed;
        }
        return true;
    }

    /**
     *  Edge case: due to the way traits are encoded, consider this hierarchy:
     *  A    B
     *    C
     *    D
     *  On don/insertion of C, C may be vetoed by its parents, but might have been
     *  already covered by one of its descendants (D)
     */
    private boolean sameAndNotCoveredByDescendants(TraitProxyImpl proxy, BitSet typeMask ) {
        boolean isSameType = typeMask.equals( proxy._getTypeCode() );
        if ( isSameType ) {
            TraitTypeMapImpl<String,Thing<?>,?> ttm = (TraitTypeMapImpl<String,Thing<?>,?>) proxy.getObject()._getTraitMap();
            Collection<Thing<?>> descs = ttm.lowerDescendants( typeMask );
            // we have to exclude the "mock" bottom proxy
            if ( descs == null || descs.isEmpty() ) {
                return true;
            } else {
                for ( Thing sub : descs ) {
                    TraitType tt = (TraitType) sub;
                    if ( tt != proxy && tt._hasTypeCode( typeMask ) ) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean isModifyAllowed( InternalFactHandle factHandle ) {
        if ( factHandle.isTraiting() ) {
            TraitProxyImpl proxy = ( (TraitProxyImpl) factHandle.getObject() );
            return proxy.listAssignedOtnTypeCodes().contains( this.typeMask );
        }
        return true;
    }


    public void modifyObject( InternalFactHandle factHandle,
                              ModifyPreviousTuples modifyPreviousTuples,
                              PropagationContext context,
                              ReteEvaluator reteEvaluator ) {

        if (!isModifyAllowed( factHandle )) {
            return;
        }

        checkDirty();

        if (factHandle.isTraiting()) {
            if (isModifyAllowed(factHandle)) {
                this.sink.propagateModifyObject(factHandle,
                                                modifyPreviousTuples,
                                                context.adaptModificationMaskForObjectType(objectType, reteEvaluator),
                                                reteEvaluator);
            } else {
                //System.err.println( ((ClassObjectType) this.getObjectType()).getClassName() + " : MODIFY BLOCK !! " + ( (TraitProxy) factHandle.getObject() ).getTraitName() + " " + ( (TraitProxy) factHandle.getObject() )._getTypeCode() + " >> " + " checks in " + typeMask );
            }
        } else {
            this.sink.propagateModifyObject(factHandle,
                                            modifyPreviousTuples,
                                            !context.getModificationMask().isSet(PropertySpecificUtil.TRAITABLE_BIT) ?
                                                    context.adaptModificationMaskForObjectType(objectType, reteEvaluator) :
                                                    context,
                                            reteEvaluator);
        }
    }

    @Override
    public BitMask updateMask(BitMask mask) {
        BitMask returnMask;
        returnMask = declaredMask.clone().setAll( mask );
        inferredMask = inferredMask.setAll( returnMask );
        return returnMask;
    }

    public BitSet getLocalTypeCode() {
        return typeMask;
    }
}
