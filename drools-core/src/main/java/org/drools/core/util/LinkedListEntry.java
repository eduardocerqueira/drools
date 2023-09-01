package org.drools.core.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The idea behind <code>LinkedListNodeWrapper</code> is to be able to add
 * the same <code>LinkedListNode</code> to multiple <code>LinkedList</code>s
 * where the node can have different previous and next nodes in each list.
 */
public class LinkedListEntry<T1 extends LinkedListNode<T1>, T2> extends AbstractBaseLinkedListNode<T1> implements Externalizable {

    private static final long serialVersionUID = 510l;
    private T2 object;

    public LinkedListEntry() {
    }

    public LinkedListEntry(final T2 object) {
        this.object = object;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        object = (T2) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(object);
    }

    public T2 getObject() {
        return this.object;
    }

    public void setObject(T2 object) {
        this.object = object;
    }

    public int hashCode() {
        return this.object.hashCode();
    }

    public boolean equals(final Object other) {
        return this.object.equals(other);
    }
}
