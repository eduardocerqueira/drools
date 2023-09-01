package org.drools.core.util.index;

import org.drools.base.base.ValueResolver;
import org.drools.base.base.ValueType;
import org.drools.base.reteoo.BaseTuple;
import org.drools.base.reteoo.NodeTypeEnums;
import org.drools.base.rule.ContextEntry;
import org.drools.base.rule.Declaration;
import org.drools.base.rule.IndexableConstraint;
import org.drools.base.rule.accessor.FieldValue;
import org.drools.base.rule.accessor.ReadAccessor;
import org.drools.base.rule.accessor.TupleValueExtractor;
import org.drools.base.rule.constraint.BetaNodeFieldConstraint;
import org.drools.base.rule.constraint.Constraint;
import org.drools.base.util.FieldIndex;
import org.drools.base.util.index.ConstraintTypeOperator;
import org.drools.base.util.index.IndexUtil;
import org.drools.core.RuleBaseConfiguration;
import org.drools.core.reteoo.BetaMemory;
import org.drools.core.util.AbstractHashTable.DoubleCompositeIndex;
import org.drools.core.util.AbstractHashTable.Index;
import org.junit.Test;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.conf.CompositeConfiguration;
import org.kie.internal.conf.IndexPrecedenceOption;
import org.kie.internal.utils.ChainedProperties;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexUtilTest {

    @Test
    public void isEqualIndexable() {
        FakeBetaNodeFieldConstraint intEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.PINTEGER_TYPE));
        assertThat(IndexUtil.isEqualIndexable(intEqualsConstraint)).isTrue();

        FakeBetaNodeFieldConstraint intLessThanConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.LESS_THAN, new FakeReadAccessor(ValueType.PINTEGER_TYPE));
        assertThat(IndexUtil.isEqualIndexable(intLessThanConstraint)).isFalse();

        FakeBetaNodeFieldConstraint bigDecimalEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.BIG_DECIMAL_TYPE));
        assertThat(IndexUtil.isEqualIndexable(bigDecimalEqualsConstraint)).as("BigDecimal equality cannot be indexed because of scale").isFalse();
    }

    @Test
    public void createBetaMemoryWithIntEquals_shouldBeTupleIndexHashTable() {
        RuleBaseConfiguration config = getRuleBaseConfiguration();
        FakeBetaNodeFieldConstraint intEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.PINTEGER_TYPE));
        BetaMemory betaMemory = IndexFactory.createBetaMemory(config, NodeTypeEnums.JoinNode, intEqualsConstraint);
        assertThat(betaMemory.getLeftTupleMemory()).isInstanceOf(TupleIndexHashTable.class);
        assertThat(betaMemory.getRightTupleMemory()).isInstanceOf(TupleIndexHashTable.class);
    }

    private RuleBaseConfiguration getRuleBaseConfiguration() {
        return new RuleBaseConfiguration(new CompositeConfiguration<>(ChainedProperties.getChainedProperties(null), null));
    }

    @Test
    public void createBetaMemoryWithBigDecimalEquals_shouldNotBeTupleIndexHashTable() {
        RuleBaseConfiguration config = getRuleBaseConfiguration();
        FakeBetaNodeFieldConstraint bigDecimalEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.BIG_DECIMAL_TYPE));
        BetaMemory betaMemory = IndexFactory.createBetaMemory(config, NodeTypeEnums.JoinNode, bigDecimalEqualsConstraint);
        assertThat(betaMemory.getLeftTupleMemory()).isInstanceOf(TupleList.class);
        assertThat(betaMemory.getRightTupleMemory()).isInstanceOf(TupleList.class);
    }

    @Test
    public void createBetaMemoryWithBigDecimalEqualsAndOtherIndexableConstraints_shouldBeTupleIndexHashTableButBigDecimalIsNotIndexed() {
        RuleBaseConfiguration config = getRuleBaseConfiguration();
        FakeBetaNodeFieldConstraint intEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.PINTEGER_TYPE));
        FakeBetaNodeFieldConstraint bigDecimalEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.BIG_DECIMAL_TYPE));
        FakeBetaNodeFieldConstraint stringEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.STRING_TYPE));
        BetaMemory betaMemory = IndexFactory.createBetaMemory(config, NodeTypeEnums.JoinNode, intEqualsConstraint, bigDecimalEqualsConstraint, stringEqualsConstraint);

        // BigDecimal is not included in Indexes
        assertThat(betaMemory.getLeftTupleMemory()).isInstanceOf(TupleIndexHashTable.class);
        Index leftIndex = ((TupleIndexHashTable) betaMemory.getLeftTupleMemory()).getIndex();
        assertThat(leftIndex).isInstanceOf(DoubleCompositeIndex.class);
        FieldIndex leftFieldIndex0 = leftIndex.getFieldIndex(0);
        assertThat(leftFieldIndex0.getLeftExtractor().getValueType()).isEqualTo(ValueType.PINTEGER_TYPE);
        FieldIndex leftFieldIndex1 = leftIndex.getFieldIndex(1);
        assertThat(leftFieldIndex1.getLeftExtractor().getValueType()).isEqualTo(ValueType.STRING_TYPE);

        assertThat(betaMemory.getRightTupleMemory()).isInstanceOf(TupleIndexHashTable.class);
        Index rightIndex = ((TupleIndexHashTable) betaMemory.getRightTupleMemory()).getIndex();
        assertThat(rightIndex).isInstanceOf(DoubleCompositeIndex.class);
        FieldIndex rightFieldIndex0 = rightIndex.getFieldIndex(0);
        assertThat(rightFieldIndex0.getRightExtractor().getValueType()).isEqualTo(ValueType.PINTEGER_TYPE);
        FieldIndex rightFieldIndex1 = rightIndex.getFieldIndex(1);
        assertThat(rightFieldIndex1.getRightExtractor().getValueType()).isEqualTo(ValueType.STRING_TYPE);
    }

    @Test
    public void isIndexableForNodeWithIntAndString() {
        RuleBaseConfiguration config = getRuleBaseConfiguration();
        FakeBetaNodeFieldConstraint intEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.PINTEGER_TYPE));
        FakeBetaNodeFieldConstraint stringEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.STRING_TYPE));
        BetaNodeFieldConstraint[] constraints = new FakeBetaNodeFieldConstraint[]{intEqualsConstraint, stringEqualsConstraint};
        boolean[] indexed = IndexUtil.isIndexableForNode(IndexPrecedenceOption.EQUALITY_PRIORITY, NodeTypeEnums.JoinNode, config.getCompositeKeyDepth(), constraints, config);
        assertThat(indexed).containsExactly(true, true);
    }

    @Test
    public void isIndexableForNodeWithIntAndBigDecimalAndString() {
        RuleBaseConfiguration config = getRuleBaseConfiguration();
        FakeBetaNodeFieldConstraint intEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.PINTEGER_TYPE));
        FakeBetaNodeFieldConstraint bigDecimalEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.BIG_DECIMAL_TYPE));
        FakeBetaNodeFieldConstraint stringEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.STRING_TYPE));
        BetaNodeFieldConstraint[] constraints = new FakeBetaNodeFieldConstraint[]{intEqualsConstraint, bigDecimalEqualsConstraint, stringEqualsConstraint};
        boolean[] indexed = IndexUtil.isIndexableForNode(IndexPrecedenceOption.EQUALITY_PRIORITY, NodeTypeEnums.JoinNode, config.getCompositeKeyDepth(), constraints, config);
        assertThat(indexed).as("BigDecimal is sorted to the last").containsExactly(true, true, false);
    }

    @Test
    public void isIndexableForNodeWithBigDecimal() {
        RuleBaseConfiguration config = getRuleBaseConfiguration();
        FakeBetaNodeFieldConstraint bigDecimalEqualsConstraint = new FakeBetaNodeFieldConstraint(ConstraintTypeOperator.EQUAL, new FakeReadAccessor(ValueType.BIG_DECIMAL_TYPE));
        BetaNodeFieldConstraint[] constraints = new FakeBetaNodeFieldConstraint[]{bigDecimalEqualsConstraint};
        boolean[] indexed = IndexUtil.isIndexableForNode(IndexPrecedenceOption.EQUALITY_PRIORITY, NodeTypeEnums.JoinNode, config.getCompositeKeyDepth(), constraints, config);
        assertThat(indexed).as("BigDecimal is not indexed").containsExactly(false);
    }

    static class FakeBetaNodeFieldConstraint implements BetaNodeFieldConstraint,
                                                        IndexableConstraint {

        private ConstraintTypeOperator constraintType;
        private ReadAccessor fieldExtractor;

        public FakeBetaNodeFieldConstraint() {}

        public FakeBetaNodeFieldConstraint(ConstraintTypeOperator constraintType, ReadAccessor fieldExtractor) {
            this.constraintType = constraintType;
            this.fieldExtractor = fieldExtractor;
        }

        @Override
        public boolean isUnification() {
            return false;
        }

        @Override
        public boolean isIndexable(short nodeType, KieBaseConfiguration config) {
            return false;
        }

        @Override
        public ConstraintTypeOperator getConstraintType() {
            return constraintType;
        }

        @Override
        public FieldValue getField() {
            return null;
        }

        @Override
        public FieldIndex getFieldIndex() {
            return new FieldIndex(fieldExtractor, new Declaration("$p1", fieldExtractor, null));
        }

        @Override
        public ReadAccessor getFieldExtractor() {
            return fieldExtractor;
        }

        @Override
        public TupleValueExtractor getIndexExtractor() {
            return null;
        }

        @Override
        public boolean isAllowedCachedLeft(ContextEntry context, FactHandle handle) {
            return false;
        }

        @Override
        public boolean isAllowedCachedRight(BaseTuple tuple, ContextEntry context) {
            return false;
        }

        @Override
        public ContextEntry createContextEntry() {
            return null;
        }

        @Override
        public BetaNodeFieldConstraint cloneIfInUse() {
            return null;
        }

        @Override
        public Declaration[] getRequiredDeclarations() {
            return new Declaration[0];
        }

        @Override
        public void replaceDeclaration(Declaration oldDecl, Declaration newDecl) {

        }

        @Override
        public Constraint clone() {
            return null;
        }

        @Override
        public ConstraintType getType() {
            return null;
        }

        @Override
        public boolean isTemporal() {
            return false;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {

        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        }
    }

    static class FakeReadAccessor implements ReadAccessor {

        private final ValueType valueType;

        private FakeReadAccessor(ValueType valueType) {
            this.valueType = valueType;
        }

        @Override
        public Object getValue(Object object) {
            return null;
        }

        @Override
        public ValueType getValueType() {
            return valueType;
        }

        @Override
        public Class<?> getExtractToClass() {
            return null;
        }

        @Override
        public String getExtractToClassName() {
            return null;
        }

        @Override
        public Method getNativeReadMethod() {
            return null;
        }

        @Override
        public String getNativeReadMethodName() {
            return null;
        }

        @Override
        public int getHashCode(Object object) {
            return 0;
        }

        @Override
        public int getIndex() {
            return 0;
        }

        @Override
        public Object getValue(ValueResolver valueResolver, Object object) {
            return null;
        }

        @Override
        public char getCharValue(ValueResolver valueResolver, Object object) {
            return 0;
        }

        @Override
        public int getIntValue(ValueResolver valueResolver, Object object) {
            return 0;
        }

        @Override
        public byte getByteValue(ValueResolver valueResolver, Object object) {
            return 0;
        }

        @Override
        public short getShortValue(ValueResolver valueResolver, Object object) {
            return 0;
        }

        @Override
        public long getLongValue(ValueResolver valueResolver, Object object) {
            return 0;
        }

        @Override
        public float getFloatValue(ValueResolver valueResolver, Object object) {
            return 0;
        }

        @Override
        public double getDoubleValue(ValueResolver valueResolver, Object object) {
            return 0;
        }

        @Override
        public boolean getBooleanValue(ValueResolver valueResolver, Object object) {
            return false;
        }

        @Override
        public boolean isNullValue(ValueResolver valueResolver, Object object) {
            return false;
        }

        @Override
        public int getHashCode(ValueResolver valueResolver, Object object) {
            return 0;
        }

        @Override
        public boolean isGlobal() {
            return false;
        }

        @Override
        public boolean isSelfReference() {
            return false;
        }
    }
}
