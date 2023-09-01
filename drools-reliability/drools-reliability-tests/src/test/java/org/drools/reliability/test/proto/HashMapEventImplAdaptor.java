package org.drools.reliability.test.proto;

import java.util.Map;
import java.util.UUID;

import org.drools.modelcompiler.facttemplate.HashMapEventImpl;
import org.drools.reliability.infinispan.proto.ProtoStreamUtils;
import org.infinispan.protostream.annotations.ProtoAdapter;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.types.protobuf.AnySchema;

import static org.drools.modelcompiler.facttemplate.FactFactory.prototypeToFactTemplate;
import static org.drools.reliability.test.util.PrototypeUtils.getPrototype;

/**
 * This adopter resides in test module, because main drools-reliability-infinispan module does not have dependency on drools-model-compiler module.
 */
@ProtoAdapter(HashMapEventImpl.class)
public class HashMapEventImplAdaptor {

    @ProtoFactory
    HashMapEventImpl create(String uuid, String factTemplate, AnySchema.Any valuesMapObject, long timestamp, long expiration) {
        Map<String, Object> valuesMap = (Map<String, Object>) ProtoStreamUtils.fromAnySchema(valuesMapObject);
        return new HashMapEventImpl(UUID.fromString(uuid), prototypeToFactTemplate(getPrototype(factTemplate)), valuesMap, timestamp, expiration);
    }

    @ProtoField(1)
    String getUuid(HashMapEventImpl event) {
        return event.getUuid().toString();
    }

    @ProtoField(2)
    String getFactTemplate(HashMapEventImpl event) {
        return event.getFactTemplate().getName();
    }

    @ProtoField(number = 3, required = true)
    AnySchema.Any getValuesMapObject(HashMapEventImpl event) {
        Map<String, Object> map = event.asMap();
        return ProtoStreamUtils.toAnySchema(map);
    }

    @ProtoField(number = 4, defaultValue = "-1")
    long getTimestamp(HashMapEventImpl event) {
        return event.getTimestamp();
    }

    @ProtoField(number = 5, defaultValue = "10000")
    long getExpiration(HashMapEventImpl event) {
        return event.getExpiration();
    }
}
