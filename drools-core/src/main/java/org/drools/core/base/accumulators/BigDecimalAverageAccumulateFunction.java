package org.drools.core.base.accumulators;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An implementation of an accumulator capable of calculating average values
 */
public class BigDecimalAverageAccumulateFunction extends AbstractAccumulateFunction<BigDecimalAverageAccumulateFunction.AverageData> {

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }

    public void writeExternal(ObjectOutput out) throws IOException {

    }

    public static class AverageData implements Externalizable {
        public int          count = 0;
        public BigDecimal   total = BigDecimal.ZERO;

        public AverageData() {}

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            count   = in.readInt();
            total   = (BigDecimal) in.readObject();
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(count);
            out.writeObject(total);
        }

    }

    /* (non-Javadoc)
     * @see org.kie.base.accumulators.AccumulateFunction#createContext()
     */
    public AverageData createContext() {
        return new AverageData();
    }

    /* (non-Javadoc)
     * @see org.kie.base.accumulators.AccumulateFunction#init(java.lang.Object)
     */
    public void init(AverageData data) {
        data.count = 0;
        data.total = BigDecimal.ZERO;
    }

    /* (non-Javadoc)
     * @see org.kie.base.accumulators.AccumulateFunction#accumulate(java.lang.Object, java.lang.Object)
     */
    public void accumulate(AverageData data,
                           Object value) {
        if (value != null) {
            data.count++;
            data.total = data.total.add( (BigDecimal) value );
        }
    }

    /* (non-Javadoc)
     * @see org.kie.base.accumulators.AccumulateFunction#reverse(java.lang.Object, java.lang.Object)
     */
    public void reverse(AverageData data,
                        Object value) {
        if (value != null) {
            data.count--;
            data.total = data.total.subtract( (BigDecimal) value );
        }
    }

    /* (non-Javadoc)
     * @see org.kie.base.accumulators.AccumulateFunction#getResult(java.lang.Object)
     */
    public Object getResult(AverageData data) {
        return data.count == 0 ? BigDecimal.ZERO : data.total.divide(BigDecimal.valueOf( data.count ), RoundingMode.HALF_EVEN );
    }

    /* (non-Javadoc)
     * @see org.kie.base.accumulators.AccumulateFunction#supportsReverse()
     */
    public boolean supportsReverse() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public Class< ? > getResultType() {
        return BigDecimal.class;
    }
}
