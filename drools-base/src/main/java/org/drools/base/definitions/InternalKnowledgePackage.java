package org.drools.base.definitions;

import java.io.Externalizable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.base.definitions.rule.impl.RuleImpl;
import org.drools.base.facttemplates.FactTemplate;
import org.drools.base.rule.ImportDeclaration;
import org.drools.base.rule.TypeDeclaration;
import org.drools.base.rule.accessor.ReadAccessor;
import org.drools.base.ruleunit.RuleUnitDescriptionLoader;
import org.drools.base.base.AcceptsClassObjectType;
import org.drools.base.base.ObjectType;
import org.drools.base.rule.DialectRuntimeRegistry;
import org.drools.base.rule.Function;
import org.drools.base.rule.WindowDeclaration;
import org.drools.base.rule.accessor.AcceptsReadAccessor;
import org.drools.util.TypeResolver;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.process.Process;
import org.drools.base.RuleBase;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.runtime.rule.AccumulateFunction;
import org.kie.internal.builder.KnowledgeBuilderResult;

public interface InternalKnowledgePackage extends KiePackage,
                                                  Externalizable {

    void clear();

    void checkValidity();

    boolean isValid();

    void setNeedStreamMode();

    void resetErrors();

    void setError(String summary);

    ResourceTypePackageRegistry getResourceTypePackages();

    Map<String, Type> getGlobals();

    @Deprecated
    Map<String, Process> getRuleFlows();

    Map<String, TypeDeclaration> getTypeDeclarations();

    Map<String, Function> getFunctions();

    Map<String, ImportDeclaration> getImports();

    Map<String, WindowDeclaration> getWindowDeclarations();

    Map<String, AccumulateFunction> getAccumulateFunctions();

    Set<String> getEntryPointIds();

    Set<String> getStaticImports();

    void addFunction(Function function);

    void addGlobal(String identifier, Type type);

    void addEntryPointId(String id);

    void addWindowDeclaration(WindowDeclaration window);

    void addRule(RuleImpl rule);

    @Deprecated
    void addProcess(Process process);

    void addTypeDeclaration(TypeDeclaration typeDecl);

    void addFactTemplate(FactTemplate factTemplate);

    void addImport(ImportDeclaration importDecl);

    void addAccumulateFunction(String name, AccumulateFunction function);

    void addStaticImport(String functionImport);

    void removeFunction(String functionName);

    @Deprecated
    void removeRuleFlow(String id);

    void removeRule(RuleImpl rule);

    void removeGlobal(String identifier);

    void removeTypeDeclaration(String type);

    boolean removeObjectsGeneratedFromResource(Resource resource);

    List<TypeDeclaration> removeTypesGeneratedFromResource(Resource resource);

    List<RuleImpl> getRulesGeneratedFromResource(Resource resource);
    List<RuleImpl> removeRulesGeneratedFromResource(Resource resource);

    List<Function> removeFunctionsGeneratedFromResource(Resource resource);

    @Deprecated
    List<Process> removeProcessesGeneratedFromResource(Resource resource);

    boolean removeFromResourceTypePackageGeneratedFromResource(Resource resource);

    DialectRuntimeRegistry getDialectRuntimeRegistry();

    RuleImpl getRule(String name);

    FactType getFactType(String typeName);

    TypeDeclaration getTypeDeclaration(Class<?> clazz);

    TypeDeclaration getTypeDeclaration(String type);

    FactTemplate getFactTemplate(String name);

    ClassLoader getPackageClassLoader();

    TypeResolver getTypeResolver();

    void setClassLoader(ClassLoader classLoader);

    RuleUnitDescriptionLoader getRuleUnitDescriptionLoader();

    InternalKnowledgePackage deepCloneIfAlreadyInUse(ClassLoader classLoader);

    void mergeTraitRegistry(RuleBase knowledgeBase);

    void addCloningResource(String key, Object resource);

    void wireTypeDeclarations();

    default void mergeStore(InternalKnowledgePackage newPkg) { }
    default void wireStore() { }

    default void buildFieldAccessors(TypeDeclaration type) { }

    default void removeClass( Class<?> cls ) { }

    default ObjectType wireObjectType(ObjectType objectType, AcceptsClassObjectType extractor) { return null; }

    default Class<?> getFieldType(Class<?> clazz, String leftValue) { return null; }

    default ReadAccessor getReader(String className, String fieldName, AcceptsReadAccessor target) { return null; }

    default Collection<KnowledgeBuilderResult> getWiringResults(Class<?> classType, String fieldName) { return Collections.emptyList(); }

    default ReadAccessor getFieldExtractor( TypeDeclaration type, String timestampField, Class<?> returnType ) { return null; }

    default void setClassFieldAccessorCache(Object classFieldAccessorCache) { }
}
