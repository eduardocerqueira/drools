package org.drools.core.common;

import org.drools.core.impl.InternalRuleBase;
import org.drools.core.reteoo.SegmentMemory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A concurrent implementation for the node memories interface
 */
public class ConcurrentNodeMemories implements NodeMemories {

    private AtomicReferenceArray<Memory> memories;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final InternalRuleBase ruleBase;

    public ConcurrentNodeMemories( InternalRuleBase ruleBase) {
        this.ruleBase = ruleBase;
        this.memories = new AtomicReferenceArray<>( this.ruleBase.getMemoryCount() );
    }

    public void clearNodeMemory( MemoryFactory node ) {
        if ( peekNodeMemory(node.getMemoryId()) != null ) {
            this.memories.set(node.getMemoryId(), null);
        }
    }

    public void clear() {
        this.memories = new AtomicReferenceArray<>( this.ruleBase.getMemoryCount() );
    }

    public void resetAllMemories(StatefulKnowledgeSession session) {
        InternalRuleBase kBase = (InternalRuleBase) session.getKieBase();
        Set<SegmentMemory> smemSet = new HashSet<>();

        for (int i = 0; i < memories.length(); i++) {
            Memory memory = memories.get(i);
            if (memory != null) {
                memory.reset();
                smemSet.add(memory.getSegmentMemory());
            }
        }

        smemSet.forEach(smem -> resetSegmentMemory(session, kBase, smem));
    }

    private void resetSegmentMemory(StatefulKnowledgeSession session, InternalRuleBase kBase, SegmentMemory smem) {
        if (smem != null) {
            smem.reset(kBase.getSegmentPrototype(smem));
            if (smem.isSegmentLinked()) {
                smem.notifyRuleLinkSegment((InternalWorkingMemory) session);
            }
        }
    }

    /**
     * The implementation tries to delay locking as much as possible, by running
     * some potentially unsafe operations out of the critical session. In case it
     * fails the checks, it will move into the critical sessions and re-check everything
     * before effectively doing any change on data structures.
     */
    public Memory getNodeMemory(MemoryFactory node, ReteEvaluator reteEvaluator) {
        if( node.getMemoryId() >= this.memories.length() ) {
            resize( node.getMemoryId() );
        }

        Memory memory = this.memories.get( node.getMemoryId() );
        return memory != null ? memory : createNodeMemory( node, reteEvaluator );
    }


    /**
     * Checks if a memory does not exists for the given node and
     * creates it.
     */
    private Memory createNodeMemory( MemoryFactory node, ReteEvaluator reteEvaluator ) {
        try {
            this.lock.readLock().lock();
            // need to try again in a synchronized code block to make sure
            // it was not created yet
            Memory memory = node.createMemory( this.ruleBase.getRuleBaseConfiguration(), reteEvaluator );
            return this.memories.compareAndSet( node.getMemoryId(), null, memory ) ?
                    memory :
                    this.memories.get( node.getMemoryId() );
        } finally {
            this.lock.readLock().unlock();
        }
    }

    private void resize( int newSize ) {
        try {
            this.lock.writeLock().lock();
            if ( newSize >= this.memories.length() ) {
                // adding some buffer for new nodes, so that we reduce array copies
                int size = Math.max(this.ruleBase.getMemoryCount(), newSize + 32);
                AtomicReferenceArray<Memory> newMem = new AtomicReferenceArray<>(size);
                for (int i = 0; i < this.memories.length(); i++) {
                    newMem.set(i, this.memories.get(i));
                }
                this.memories = newMem;
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public Memory peekNodeMemory(int memoryId ) {
        if ( memoryId < this.memories.length() ) {
            return this.memories.get( memoryId );
        } else {
            return null;
        }
    }

    public int length() {
        return this.memories.length();
    }

}
