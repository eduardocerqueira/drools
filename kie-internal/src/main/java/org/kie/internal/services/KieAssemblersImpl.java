package org.kie.internal.services;

import java.util.List;

import org.kie.api.internal.assembler.KieAssemblerService;
import org.kie.api.internal.assembler.KieAssemblers;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceConfiguration;
import org.kie.api.io.ResourceType;
import org.kie.api.io.ResourceWithConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KieAssemblersImpl extends AbstractMultiService<ResourceType, KieAssemblerService> implements KieAssemblers {

    private static final Logger logger = LoggerFactory.getLogger(KieAssemblersImpl.class);

    @Override
    public void addResourceBeforeRules(Object knowledgeBuilder, Resource resource, ResourceType type, ResourceConfiguration configuration) throws Exception {
        if (ResourceType.PMML.equals(type)) {
            // Temporary until Assemblers not fully removed
            logger.info("Ignoring resource {} of type {}", resource, type );
            return;
        }
        KieAssemblerService assembler = getAssembler(type);
        if (assembler != null) {
            assembler.addResourceBeforeRules(knowledgeBuilder,
                                             resource,
                                             type,
                                             configuration);
        } else {
            throw new RuntimeException("Unknown resource type: " + type);
        }
    }

    private KieAssemblerService getAssembler(ResourceType type) {
        return getService(type);
    }

    @Override
    public void addResourceAfterRules(Object knowledgeBuilder, Resource resource, ResourceType type, ResourceConfiguration configuration) throws Exception {
        if (ResourceType.PMML.equals(type)) {
            // Temporary until Assemblers not fully removed
            logger.info("Ignoring resource {} of type {}", resource, type );
            return;
        }
        KieAssemblerService assembler = getAssembler(type);
        if (assembler != null) {
            assembler.addResourceAfterRules(knowledgeBuilder,
                                            resource,
                                            type,
                                            configuration);
        } else {
            throw new RuntimeException("Unknown resource type: " + type);
        }

    }

    @Override
    public void addResourcesAfterRules(Object knowledgeBuilder, List<ResourceWithConfiguration> resources, ResourceType type) throws Exception {
        if (ResourceType.PMML.equals(type)) {
            // Temporary until Assemblers not fully removed
            logger.info("Ignoring resources {} of type {}", resources, type );
            return;
        }
        KieAssemblerService assembler = getAssembler(type);
        if (assembler != null) {
            assembler.addResourcesAfterRules(knowledgeBuilder, resources, type);
        } else {
            throw new RuntimeException("Unknown resource type: " + type);
        }
    }

    @Override
    protected Class<KieAssemblerService> serviceClass() {
        return KieAssemblerService.class;
    }

    @Override
    protected ResourceType serviceKey(KieAssemblerService service) {
        return service.getResourceType();
    }
}
