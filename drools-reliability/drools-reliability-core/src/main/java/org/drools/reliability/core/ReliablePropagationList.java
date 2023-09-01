package org.drools.reliability.core;

import org.drools.core.common.ReteEvaluator;
import org.drools.core.common.Storage;
import org.drools.core.phreak.PropagationEntry;
import org.drools.core.phreak.SynchronizedPropagationList;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ReliablePropagationList extends SynchronizedPropagationList implements Externalizable {

    public static final String PROPAGATION_LIST = "PropagationList";

    public ReliablePropagationList(ReteEvaluator reteEvaluator) {
        super(reteEvaluator);
    }

    public ReliablePropagationList(){super();}

    public void setReteEvaluator(ReteEvaluator reteEvaluator){this.reteEvaluator=reteEvaluator;}

    public ReliablePropagationList(ReteEvaluator reteEvaluator, ReliablePropagationList originalList) {
        super(reteEvaluator);
        this.head = originalList.head;
        this.tail = originalList.tail;
    }

    @Override
    public synchronized PropagationEntry takeAll() {
        PropagationEntry p = super.takeAll();
        Storage<String, Object> componentsStorage = StorageManagerFactory.get().getStorageManager().getOrCreateStorageForSession(this.reteEvaluator, "components");
        componentsStorage.put(PROPAGATION_LIST, this);
        return p;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(head);
        out.writeObject(tail);
        out.writeBoolean(disposed);
        out.writeBoolean(hasEntriesDeferringExpiration);
        out.writeBoolean(firingUntilHalt);
   }
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.head = (PropagationEntry) in.readObject();
        this.tail = (PropagationEntry) in.readObject();
        this.disposed = in.readBoolean();
        this.hasEntriesDeferringExpiration = in.readBoolean();
        this.firingUntilHalt = in.readBoolean();
    }
}
