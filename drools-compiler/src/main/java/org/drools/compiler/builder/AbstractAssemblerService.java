package org.drools.compiler.builder;

import java.util.Collection;

import org.kie.api.internal.assembler.KieAssemblerService;
import org.kie.api.internal.assembler.ProcessedResource;
import org.kie.api.internal.io.ResourceTypePackage;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceConfiguration;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.AssemblerContext;
import org.kie.internal.builder.KnowledgeBuilderError;

/**
 * A base implementation for an {@link KieAssemblerService}, following simple conventions.
 * @param <T> type of the package that is being assembled
 * @param <U> type of the processed resource that will be assembled for each given {@link Resource}
 */
public abstract class AbstractAssemblerService<T extends ResourceTypePackage<U>, U extends ProcessedResource> implements KieAssemblerService {

    /**
     * Factory for a package of type T
     * @param namespace namespace of the package (e.g. a Java package name)
     */
    protected abstract T createPackage(String namespace);

    /**
     * Factory for the ResourceProcessor subclass that will process Resources that this assembler supports
     */
    protected abstract ResourceProcessor<U> createResourceProcessor(Resource resource);

    @Override
    public final void addResourceAfterRules(Object kbuilder, Resource resource, ResourceType type, ResourceConfiguration configuration) throws Exception {
        AssemblerContext kb = (AssemblerContext) kbuilder;
        ResourceProcessor<U> processor = createResourceProcessor(resource);
        processor.process();
        Collection<? extends KnowledgeBuilderError> errors = processor.getErrors();

        if (errors.isEmpty()) {
            U compiledResource = processor.getProcessedResource();
            kb.computeIfAbsent(
                    getResourceType(),
                    compiledResource.getNamespace(),
                    resourceType -> createPackage(compiledResource.getNamespace()))
                    .add(compiledResource);
        } else {
            errors.forEach(kb::reportError);
        }
    }
}
