package org.drools.modelcompiler.facttemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.base.definitions.InternalKnowledgePackage;
import org.drools.base.facttemplates.Event;
import org.drools.base.facttemplates.Fact;
import org.drools.base.facttemplates.FactTemplate;
import org.drools.base.facttemplates.FactTemplateImpl;
import org.drools.base.facttemplates.FieldTemplate;
import org.drools.base.facttemplates.FieldTemplateImpl;
import org.drools.core.reteoo.CoreComponentFactory;
import org.drools.model.Prototype;

public class FactFactory {

    private static final Map<Prototype, FactTemplate> factTemplateCache = new ConcurrentHashMap<>();

    public static Fact createMapBasedFact(FactTemplate factTemplate) {
        return new HashMapFactImpl( factTemplate );
    }

    public static Fact createMapBasedFact(FactTemplate factTemplate, Map<String, Object> valuesMap) {
        return new HashMapFactImpl( factTemplate, valuesMap );
    }

    public static Fact createMapBasedFact(Prototype prototype) {
        return createMapBasedFact( prototypeToFactTemplate( prototype ) );
    }

    public static Fact createMapBasedFact(Prototype prototype, Map<String, Object> valuesMap) {
        return createMapBasedFact( prototypeToFactTemplate( prototype ), valuesMap );
    }

    public static Event createMapBasedEvent(FactTemplate factTemplate) {
        return new HashMapEventImpl( factTemplate );
    }

    public static Event createMapBasedEvent(FactTemplate factTemplate, Map<String, Object> valuesMap) {
        return new HashMapEventImpl( factTemplate, valuesMap );
    }

    public static Event createMapBasedEvent(Prototype prototype) {
        return createMapBasedEvent( prototypeToFactTemplate( prototype ) );
    }

    public static Event createMapBasedEvent(Prototype prototype, Map<String, Object> valuesMap) {
        return createMapBasedEvent( prototypeToFactTemplate( prototype ), valuesMap );
    }

    public static FactTemplate prototypeToFactTemplate( Prototype prototype ) {
        return factTemplateCache.computeIfAbsent(prototype, p -> prototypeToFactTemplate( p, CoreComponentFactory.get().createKnowledgePackage( p.getPackage() ) ) );
    }

    public static FactTemplate prototypeToFactTemplate( Prototype prototype, InternalKnowledgePackage pkg ) {
        FieldTemplate[] fieldTemplates = new FieldTemplate[prototype.getFieldNames().size()];
        int i = 0;
        for (String fieldName : prototype.getFieldNames()) {
            fieldTemplates[i++] = new FieldTemplateImpl( fieldName, prototype.getField(fieldName).getType() );
        }
        return new FactTemplateImpl( pkg, prototype.getName(), fieldTemplates );
    }
}
