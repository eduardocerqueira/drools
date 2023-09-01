package org.drools.core.reteoo;

import org.drools.core.common.Memory;
import org.drools.core.common.ReteEvaluator;
import org.drools.core.common.TupleSets;
import org.drools.core.common.TupleSetsImpl;
import org.drools.core.reteoo.RightInputAdapterNode.RiaPathMemory;
import org.drools.base.rule.ContextEntry;
import org.drools.core.util.AbstractBaseLinkedListNode;

public class BetaMemory extends AbstractBaseLinkedListNode<Memory>
        implements
        SegmentNodeMemory {

    private static final long serialVersionUID = 510l;
    private TupleMemory                leftTupleMemory;
    private TupleMemory                rightTupleMemory;
    private TupleSets<RightTuple>      stagedRightTuples;
    private ContextEntry[]             context;
    // the node type this memory belongs to
    private short                      nodeType;
    private SegmentMemory              segmentMemory;
    private long                       nodePosMaskBit;
    private int                        counter;
    private RiaPathMemory              riaRuleMemory;

    public BetaMemory() {
    }

    public BetaMemory(final TupleMemory tupleMemory,
                      final TupleMemory objectMemory,
                      final ContextEntry[] context,
                      final short nodeType) {
        this.leftTupleMemory = tupleMemory;
        this.rightTupleMemory = objectMemory;
        this.stagedRightTuples = new TupleSetsImpl<>();
        this.context = context;
        this.nodeType = nodeType;
    }

    public TupleSets<RightTuple> getStagedRightTuples() {
        return stagedRightTuples;
    }

    public void setStagedRightTuples(TupleSets<RightTuple> stagedRightTuples) {
        this.stagedRightTuples = stagedRightTuples;
    }

    public TupleMemory getRightTupleMemory() {
        return this.rightTupleMemory;
    }

    public TupleMemory getLeftTupleMemory() {
        return this.leftTupleMemory;
    }

    public RiaPathMemory getRiaRuleMemory() {
        return riaRuleMemory;
    }

    public void setRiaRuleMemory(RiaPathMemory riaRuleMemory) {
        this.riaRuleMemory = riaRuleMemory;
    }

    /**
     * @return the context
     */
    public ContextEntry[] getContext() {
        return context;
    }

    public boolean linkNode(LeftTupleSource tupleSource, ReteEvaluator reteEvaluator) {
        return linkNode(tupleSource, reteEvaluator, true);
    }

    public boolean linkNode(LeftTupleSource tupleSource, ReteEvaluator reteEvaluator, boolean notify) {
        if (segmentMemory == null) {
            segmentMemory = getOrCreateSegmentMemory( tupleSource, reteEvaluator );
        }
        return notify ?
               segmentMemory.linkNode(nodePosMaskBit, reteEvaluator) :
               segmentMemory.linkNodeWithoutRuleNotify(nodePosMaskBit);
    }

    public boolean unlinkNode(ReteEvaluator reteEvaluator) {
        return segmentMemory.unlinkNode(nodePosMaskBit, reteEvaluator);
    }

    public short getNodeType() {
        return this.nodeType;
    }

    public SegmentMemory getSegmentMemory() {
        return segmentMemory;
    }

    public void setSegmentMemory(SegmentMemory segmentMemory) {
        this.segmentMemory = segmentMemory;
    }

    public long getNodePosMaskBit() {
        return nodePosMaskBit;
    }

    public void setNodePosMaskBit(long nodePosMaskBit) {
        this.nodePosMaskBit = nodePosMaskBit;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getAndIncCounter() {
        return counter++;
    }

    public int getAndDecCounter() {
        return counter--;
    }

    public boolean setNodeDirty(LeftTupleSource tupleSource, ReteEvaluator reteEvaluator) {
        return setNodeDirty(tupleSource, reteEvaluator, true);
    }

    public boolean setNodeDirty(LeftTupleSource tupleSource, ReteEvaluator reteEvaluator, boolean notify) {
        if (segmentMemory == null) {
            segmentMemory = getOrCreateSegmentMemory( tupleSource, reteEvaluator );
        }
        return notify ?
               segmentMemory.notifyRuleLinkSegment(reteEvaluator, nodePosMaskBit) :
               segmentMemory.linkSegmentWithoutRuleNotify(nodePosMaskBit);
    }

    public void setNodeDirtyWithoutNotify() {
        if (segmentMemory != null) {
            segmentMemory.updateDirtyNodeMask( nodePosMaskBit );
        }
    }

    public void setNodeCleanWithoutNotify() {
        if (segmentMemory != null) {
            segmentMemory.updateCleanNodeMask( nodePosMaskBit );
        }
    }

    public void reset() {
        if (leftTupleMemory != null) {
            leftTupleMemory.clear();
        }
        if (rightTupleMemory != null) {
            rightTupleMemory.clear();
        }
        stagedRightTuples.resetAll();
        counter = 0;
    }
}
