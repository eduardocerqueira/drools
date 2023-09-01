package org.drools.core.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.base.base.ClassObjectType;
import org.drools.base.facttemplates.Fact;
import org.drools.base.facttemplates.FactImpl;
import org.drools.core.impl.InternalRuleBase;
import org.drools.core.reteoo.ClassObjectTypeConf;
import org.drools.core.reteoo.FactTemplateTypeConf;
import org.drools.core.reteoo.ObjectTypeConf;
import org.drools.base.rule.EntryPointId;
import org.drools.core.reteoo.RuleTerminalNodeLeftTuple;
import org.drools.core.rule.consequence.InternalMatch;
import org.drools.base.base.ObjectType;

public class ObjectTypeConfigurationRegistry implements Serializable {
    private static final long serialVersionUID = 510l;

    private final Map<Object, ObjectTypeConf> typeConfMap = new ConcurrentHashMap<>();

    private final InternalRuleBase ruleBase;

    public ObjectTypeConfigurationRegistry(InternalRuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }

    public ObjectTypeConf getObjectTypeConf(Object object) {
        return this.typeConfMap.get( getKey( object ) );
    }

    /**
     * Returns the ObjectTypeConfiguration object for the given object or
     * creates a new one if none is found in the cache
     */
    public ObjectTypeConf getOrCreateObjectTypeConf(EntryPointId entrypoint, Object object) {
        Object key = getKey( object );
        ObjectTypeConf conf = this.typeConfMap.get( key );
        if (conf == null) {
            conf = createObjectTypeConf( entrypoint, key, object );
            ObjectTypeConf existingConf = this.typeConfMap.putIfAbsent( key, conf );
            if (existingConf != null) {
                conf = existingConf;
            }
        }
        return conf;
    }

    // Avoid secondary super cache invalidation by testing for abstract classes first
    // Then interfaces
    // See: https://issues.redhat.com/browse/DROOLS-7521
    private Object getKey( Object object ) {
        if (object instanceof RuleTerminalNodeLeftTuple) {
            return ClassObjectType.Match_ObjectType.getClassType();
        } else if (object instanceof FactImpl) {
            return ((FactImpl) object).getFactTemplate().getName();
        } else if (object instanceof Fact) {
            return ((Fact) object).getFactTemplate().getName();
        } else if (object instanceof InternalMatch) {
            return ClassObjectType.Match_ObjectType.getClassType();
        }
        return object.getClass();
    }

    private ObjectTypeConf createObjectTypeConf(EntryPointId entrypoint, Object key, Object object) {
        return object instanceof Fact ?
                new FactTemplateTypeConf( entrypoint, ((Fact) object).getFactTemplate(), this.ruleBase ) :
                new ClassObjectTypeConf( entrypoint, (Class<?>) key, this.ruleBase );
    }

    public ObjectTypeConf getConfForObjectType(ObjectType objectType) {
        return typeConfMap.get(objectType.getTypeKey());
    }

    public Collection<ObjectTypeConf> values() {
        return this.typeConfMap.values();
    }
}
