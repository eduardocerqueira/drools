package org.drools.verifier.core.index.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.drools.verifier.core.index.keys.Value;
import org.drools.verifier.core.index.matchers.Matcher;
import org.drools.verifier.core.index.query.Where;
import org.drools.verifier.core.index.select.Listen;
import org.drools.verifier.core.index.select.Select;
import org.drools.verifier.core.maps.KeyTreeMap;
import org.drools.verifier.core.maps.MultiMap;

public class ObjectTypes {

    public final KeyTreeMap<ObjectType> map = new KeyTreeMap<>(ObjectType.keyDefinitions());

    public ObjectTypes(final Collection<ObjectType> map) {
        for (final ObjectType objectType : map) {
            add(objectType);
        }
    }

    public ObjectTypes(final ObjectType[] map) {
        this(Arrays.asList(map));
    }

    public ObjectTypes() {

    }

    public void merge(final ObjectTypes patterns) {
        this.map.merge(patterns.map);
    }

    public Where<ObjectTypesSelect, ObjectTypesListen> where(final Matcher matcher) {
        return new Where<ObjectTypesSelect, ObjectTypesListen>() {
            @Override
            public ObjectTypesSelect select() {
                return new ObjectTypesSelect(matcher);
            }

            @Override
            public ObjectTypesListen listen() {
                return new ObjectTypesListen(matcher);
            }
        };
    }

    public void add(final ObjectType... objectTypes) {
        for (final ObjectType objectType : objectTypes) {
            this.map.put(objectType);
        }
    }

    public class ObjectTypesSelect
            extends Select<ObjectType> {

        public ObjectTypesSelect(final Matcher matcher) {
            super(map.get(matcher.getKeyDefinition()),
                  matcher);
        }

        public ObjectFields fields() {
            final ObjectFields fields = new ObjectFields();

            final MultiMap<Value, ObjectType, List<ObjectType>> subMap = asMap();
            if (subMap != null) {
                final Collection<ObjectType> objectTypes = subMap.allValues();
                for (final ObjectType objectType : objectTypes) {
                    fields.merge(objectType.getFields());
                }
            }

            return fields;
        }
    }

    public class ObjectTypesListen
            extends Listen<ObjectType> {

        public ObjectTypesListen(final Matcher matcher) {
            super(map.get(matcher.getKeyDefinition()),
                  matcher);
        }
    }
}
