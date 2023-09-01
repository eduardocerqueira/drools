package org.kie.efesto.runtimemanager.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.kie.efesto.common.api.identifiers.ModelLocalUriId;
import org.kie.efesto.common.api.io.IndexFile;
import org.kie.efesto.common.api.listener.EfestoListener;
import org.kie.efesto.common.api.model.GeneratedResources;
import org.kie.efesto.common.core.utils.JSONUtils;
import org.kie.efesto.runtimemanager.api.exceptions.EfestoRuntimeManagerException;
import org.kie.efesto.runtimemanager.api.model.EfestoRuntimeContext;
import org.kie.efesto.runtimemanager.api.service.KieRuntimeService;
import org.kie.efesto.runtimemanager.api.utils.SPIUtils;
import org.kie.memorycompiler.KieMemoryCompiler;

public class EfestoRuntimeContextImpl<T extends EfestoListener> implements EfestoRuntimeContext<T> {

    private final KieMemoryCompiler.MemoryCompilerClassLoader memoryCompilerClassLoader;

    protected final Map<String, GeneratedResources> generatedResourcesMap = new HashMap<>();

    protected EfestoRuntimeContextImpl(KieMemoryCompiler.MemoryCompilerClassLoader memoryCompilerClassLoader) {
        this.memoryCompilerClassLoader = memoryCompilerClassLoader;
        prepareClassLoader();
        populateGeneratedResourcesMap();
    }

    protected EfestoRuntimeContextImpl(KieMemoryCompiler.MemoryCompilerClassLoader memoryCompilerClassLoader, Map<String, GeneratedResources> generatedResourcesMap) {
        this.memoryCompilerClassLoader = memoryCompilerClassLoader;
        prepareClassLoader();
        this.generatedResourcesMap.putAll(generatedResourcesMap);
    }

    private void prepareClassLoader() {
        for (ModelLocalUriId modelLocalUriId : localUriIdKeySet()) {
            Map<String, byte[]> generatedClasses = getGeneratedClasses(modelLocalUriId);
            generatedClasses.forEach(memoryCompilerClassLoader::addCodeIfAbsent);
        }
    }

    private void populateGeneratedResourcesMap() {
        Set<String> modelTypes = SPIUtils.collectModelTypes(this);
        Map<String, IndexFile> indexFileMap = IndexFile.findIndexFilesFromClassLoader(memoryCompilerClassLoader,
                                                                                      modelTypes);
        indexFileMap.forEach((model, indexFile) -> {
            try {
                GeneratedResources generatedResources = JSONUtils.getGeneratedResourcesObject(indexFile);
                generatedResourcesMap.put(model, generatedResources);
            } catch (Exception e) {
                throw new EfestoRuntimeManagerException("Failed to read IndexFile content : " + indexFile.getAbsolutePath(), e);
            }
        });
    }

    @Override
    public Map<String, GeneratedResources> getGeneratedResourcesMap() {
        return generatedResourcesMap;
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return memoryCompilerClassLoader.loadClass(className);
    }

    @Override
    public ServiceLoader<KieRuntimeService> getKieRuntimeService() {
        return ServiceLoader.load(KieRuntimeService.class, memoryCompilerClassLoader);
    }
}
