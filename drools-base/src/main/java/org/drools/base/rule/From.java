package org.drools.base.rule;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.drools.base.rule.accessor.DataProvider;
import org.drools.base.rule.accessor.Wireable;
import org.drools.base.base.ClassObjectType;
import org.drools.base.facttemplates.Fact;

public class From extends ConditionalElement
        implements
        Externalizable,
        Wireable,
        PatternSource {

    private static final long serialVersionUID = 510l;

    private DataProvider dataProvider;

    private Pattern           resultPattern;

    private Class<?>          resultClass;

    public From() {
    }

    public From(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        dataProvider    = ( DataProvider ) in.readObject();
        resultPattern   = ( Pattern ) in.readObject();
        resultClass     = ( Class<?> ) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject( dataProvider );
        out.writeObject(  resultPattern );
        out.writeObject(  resultClass );
    }

    public boolean isReactive() {
        return dataProvider.isReactive();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if (!(obj instanceof From)) {
            return false;
        }

        return dataProvider.equals( ((From) obj).dataProvider );
    }

    @Override
    public int hashCode() {
        return dataProvider.hashCode();
    }

    public void wire( Object object ) {
        this.dataProvider = ( DataProvider ) object;
    }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }

    public From clone() {
        return new From( this.dataProvider.clone() );
    }

    public Map getInnerDeclarations() {
        return Collections.EMPTY_MAP;
    }

    public Map getOuterDeclarations() {
        return Collections.EMPTY_MAP;
    }

    /**
     * @inheritDoc
     */
    public Declaration resolveDeclaration(final String identifier) {
        return null;
    }

    public List getNestedElements() {
        return Collections.EMPTY_LIST;
    }

    public boolean isPatternScopeDelimiter() {
        return true;
    }

    public void setResultPattern(Pattern pattern) {
        this.resultPattern = pattern;
    }

    public Pattern getResultPattern() {
        return this.resultPattern;
    }

    public Class<?> getResultClass() {
        return resultClass != null ?
                resultClass :
                (resultPattern.getObjectType().isTemplate() ? Fact.class : ((ClassObjectType) resultPattern.getObjectType()).getClassType());
    }

    public void setResultClass(Class<?> resultClass) {
        this.resultClass = resultClass;
    }

    @Override
    public boolean requiresLeftActivation() {
        return true;
    }
}
