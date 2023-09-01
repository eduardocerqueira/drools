package org.drools.modelcompiler.facttemplate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.drools.base.facttemplates.Fact;
import org.drools.base.facttemplates.FactTemplate;
import org.drools.model.PrototypeFact;

public class HashMapFactImpl implements Fact, PrototypeFact, Serializable {

    protected final UUID uuid;

    protected final FactTemplate factTemplate;

    protected final Map<String, Object> valuesMap;

    public HashMapFactImpl( FactTemplate factTemplate ) {
        this( factTemplate, new HashMap<>() );
    }

    public HashMapFactImpl( FactTemplate factTemplate, Map<String, Object> valuesMap ) {
        this.uuid = UUID.randomUUID();
        this.factTemplate = factTemplate;
        this.valuesMap = valuesMap;
    }

    public HashMapFactImpl( UUID uuid, FactTemplate factTemplate, Map<String, Object> valuesMap ) {
        this.uuid = uuid;
        this.factTemplate = factTemplate;
        this.valuesMap = valuesMap;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public FactTemplate getFactTemplate() {
        return factTemplate;
    }

    @Override
    public boolean has( String name ) {
        return valuesMap.containsKey( name );
    }

    @Override
    public Object get( String name ) {
        return valuesMap.get(name);
    }

    @Override
    public void set( String name, Object value ) {
        valuesMap.put(name, value);
    }

    @Override
    public Map<String, Object> asMap() {
        return valuesMap;
    }

    @Override
    public String toString() {
        return "Fact " + factTemplate.getName() + " with values = " + valuesMap;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HashMapFactImpl other = (HashMapFactImpl) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

}
