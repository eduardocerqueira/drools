package org.drools.core.base.accumulators;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * An implementation of an accumulator capable of calculating maximum values
 */
public class MaxAccumulateFunction extends AbstractAccumulateFunction<MaxAccumulateFunction.MaxData> {

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }

    public void writeExternal(ObjectOutput out) throws IOException {

    }

    protected static class MaxData implements Externalizable {
        public Comparable max = null;

        public MaxData() {}

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            max = (Comparable) in.readObject();
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(max);
        }

        @Override
        public String toString() {
            return "max";
        }
    }

    public MaxData createContext() {
        return new MaxData();
    }

    public void init(MaxData data) {
        data.max = null;
    }

    public void accumulate(MaxData data,
                           Object value) {
        if (value != null) {
            data.max = data.max == null || data.max.compareTo( value ) < 0 ?
                       (Comparable) value :
                       data.max;
        }
    }

    public void reverse(MaxData data,
                        Object value) {
    }

    @Override
    public boolean tryReverse( MaxData data, Object value ) {
        if (value != null) {
            return data.max.compareTo( value ) > 0;
        }
        return true;
    }

    public Object getResult(MaxData data) {
        return data.max;
    }

    public boolean supportsReverse() {
        return false;
    }

    public Class<?> getResultType() {
        return Comparable.class;
    }
}
