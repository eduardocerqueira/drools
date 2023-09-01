package org.drools.compiler.builder.impl;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.drools.compiler.builder.PackageRegistryManager;
import org.drools.compiler.compiler.PackageRegistry;
import org.drools.base.base.ObjectType;
import org.drools.base.rule.TypeDeclaration;
import org.drools.drl.ast.descr.PackageDescr;
import org.drools.kiesession.rulebase.InternalKnowledgeBase;
import org.kie.internal.builder.ResourceChange;

public class TypeDeclarationContextImpl implements TypeDeclarationContext {

    private KnowledgeBuilderConfigurationImpl configuration;
    private final PackageRegistryManager packageRegistryManager;
    private GlobalVariableContext globalVariableContext;

    private TypeDeclarationManagerImpl typeDeclarationManager;

    public TypeDeclarationContextImpl(KnowledgeBuilderConfigurationImpl configuration, PackageRegistryManager packageRegistryManager, GlobalVariableContext globalVariableContext) {
        this.configuration = configuration;
        this.packageRegistryManager = packageRegistryManager;
        this.globalVariableContext = globalVariableContext;
    }

    public void setTypeDeclarationManager(TypeDeclarationManagerImpl typeDeclarationManagerImpl) {
        this.typeDeclarationManager = typeDeclarationManagerImpl;
    }

    @Override
    public TypeDeclarationBuilder getTypeBuilder() {
        return typeDeclarationManager.getTypeDeclarationBuilder();
    }

    @Override
    public boolean filterAccepts(ResourceChange.Type declaration, String namespace, String typeName) {
        return false;
    }

    @Override
    public TypeDeclaration getAndRegisterTypeDeclaration(Class<?> cls, String name) {
        return typeDeclarationManager.getAndRegisterTypeDeclaration(cls, name);
    }

    @Override
    public TypeDeclaration getTypeDeclaration(ObjectType objectType) {
        return typeDeclarationManager.getTypeDeclaration(objectType);
    }

    @Override
    public TypeDeclaration getTypeDeclaration(Class<?> objectType) {
        return typeDeclarationManager.getTypeDeclaration(objectType);
    }

    @Override
    public List<PackageDescr> getPackageDescrs(String namespace) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public PackageRegistry getPackageRegistry(String packageName) {
        return packageRegistryManager.getPackageRegistry(packageName);
    }

    @Override
    public PackageRegistry getOrCreatePackageRegistry(PackageDescr packageDescr) {
        return packageRegistryManager.getOrCreatePackageRegistry(packageDescr);
    }

    @Override
    public Map<String, PackageRegistry> getPackageRegistry() {
        return packageRegistryManager.getPackageRegistry();
    }

    @Override
    public Collection<String> getPackageNames() {
        // this is not really used by TypeDeclarationContext!!
        return packageRegistryManager.getPackageNames();
    }

    @Override
    public KnowledgeBuilderConfigurationImpl getBuilderConfiguration() {
        return configuration;
    }

    @Override
    public InternalKnowledgeBase getKnowledgeBase() {
        return null;
    }

    @Override
    public ClassLoader getRootClassLoader() {
        return configuration.getClassLoader();
    }

    @Override
    public Map<String, Type> getGlobals() {
        return globalVariableContext.getGlobals();
    }

    @Override
    public void addGlobal(String identifier, Type type) {
        globalVariableContext.addGlobal(identifier, type);
    }
}
