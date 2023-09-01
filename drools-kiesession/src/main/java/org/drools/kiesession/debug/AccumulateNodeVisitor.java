package org.drools.kiesession.debug;

import org.drools.base.common.NetworkNode;
import org.drools.core.reteoo.AccumulateNode;
import org.drools.core.reteoo.AccumulateNode.AccumulateContext;
import org.drools.core.reteoo.AccumulateNode.AccumulateMemory;
import org.drools.core.reteoo.BetaNode;
import org.drools.core.reteoo.Tuple;
import org.drools.core.util.FastIterator;

import java.util.Collection;

public class AccumulateNodeVisitor extends AbstractNetworkNodeVisitor {
    
    public static final AccumulateNodeVisitor INSTANCE = new AccumulateNodeVisitor();
    
    protected AccumulateNodeVisitor() {
    }

    @Override
    protected void doVisit(NetworkNode node,
                           Collection<NetworkNode> nodeStack,
                           StatefulKnowledgeSessionInfo info) {
        AccumulateNode an = (AccumulateNode) node;
        DefaultNodeInfo ni = info.getNodeInfo( node );
        final AccumulateMemory memory = (AccumulateMemory) info.getSession().getNodeMemory( an );
        
        if( an.isObjectMemoryEnabled() ) {
            ni.setFactMemorySize( memory.getBetaMemory().getRightTupleMemory().size() );
        }
        if( an.isLeftTupleMemoryEnabled() ) {
            ni.setTupleMemorySize( memory.getBetaMemory().getLeftTupleMemory().size() );
            FastIterator it =  memory.getBetaMemory().getLeftTupleMemory().fullFastIterator();
            
            int i = 0;
            for ( Tuple leftTuple = BetaNode.getFirstTuple( memory.getBetaMemory().getLeftTupleMemory(), it ); leftTuple != null; leftTuple = ( Tuple) it.next( leftTuple  )) {
                AccumulateContext ctx = (AccumulateContext) leftTuple.getContextObject();
                if ( ctx != null ) {
                    i++;
                }
            }
             
            ni.setCreatedFactHandles( i );
        }

    }

}
