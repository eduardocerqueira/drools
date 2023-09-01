package org.drools.reliability.infinispan;

import org.drools.core.common.Storage;
import org.drools.reliability.core.SimpleReliableObjectStore;
import org.drools.reliability.core.SimpleReliableObjectStoreFactory;
import org.drools.reliability.core.SimpleSerializationReliableObjectStore;
import org.drools.reliability.core.SimpleSerializationReliableRefObjectStore;
import org.drools.reliability.core.StorageManagerFactory;
import org.drools.reliability.core.StoredObject;
import org.drools.reliability.infinispan.proto.SimpleProtoStreamReliableObjectStore;
import org.kie.api.runtime.conf.PersistedSessionOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleInfinispanReliableObjectStoreFactory implements SimpleReliableObjectStoreFactory {

    static int servicePriorityValue = 0; // package access for test purposes

    private static final Logger LOG = LoggerFactory.getLogger(SimpleInfinispanReliableObjectStoreFactory.class);

    public SimpleReliableObjectStore createSimpleReliableObjectStore(Storage<Long, StoredObject> storage, PersistedSessionOption persistedSessionOption) {
        if (((InfinispanStorageManager)StorageManagerFactory.get().getStorageManager()).isProtoStream()) {
            LOG.debug("Using SimpleProtoStreamReliableObjectStore");
            return new SimpleProtoStreamReliableObjectStore(storage);
        } else {
            if (persistedSessionOption.getPersistenceObjectsStrategy()== PersistedSessionOption.PersistenceObjectsStrategy.OBJECT_REFERENCES){
                LOG.debug("Using SimpleSerializationReliableRefObjectStore");
                return new SimpleSerializationReliableRefObjectStore(storage);
            }else{
                LOG.debug("Using SimpleSerializationReliableObjectStore");
                return new SimpleSerializationReliableObjectStore(storage);
            }
        }
    }

    @Override
    public int servicePriority() {
        return servicePriorityValue;
    }

}
