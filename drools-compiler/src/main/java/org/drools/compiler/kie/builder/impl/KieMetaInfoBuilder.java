package org.drools.compiler.kie.builder.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.compiler.builder.InternalKnowledgeBuilder;
import org.kie.memorycompiler.resources.ResourceStore;
import org.drools.compiler.compiler.PackageRegistry;
import org.drools.compiler.kproject.models.KieModuleModelImpl;
import org.drools.base.factmodel.ClassDefinition;
import org.drools.core.rule.JavaDialectRuntimeData;
import org.drools.base.rule.KieModuleMetaInfo;
import org.drools.base.rule.TypeDeclaration;
import org.drools.base.rule.TypeMetaInfo;
import org.drools.util.IoUtils;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.definition.type.FactType;
import org.kie.internal.builder.KnowledgeBuilder;

import static org.drools.base.util.Drools.hasMvel;

public class KieMetaInfoBuilder {

    protected final InternalKieModule kModule;

    public KieMetaInfoBuilder( InternalKieModule kModule) {
        this.kModule = kModule;
    }

    public void writeKieModuleMetaInfo(ResourceStore trgMfs) {
        if (hasMvel()) {
            KieModuleMetaInfo info = generateKieModuleMetaInfo( trgMfs );
            trgMfs.write( KieModuleModelImpl.KMODULE_INFO_JAR_PATH,
                    info.marshallMetaInfos().getBytes( IoUtils.UTF8_CHARSET ),
                    true );
        }
    }

    public KieModuleMetaInfo getKieModuleMetaInfo(){
        return generateKieModuleMetaInfo(null);
    }

    public KieModuleMetaInfo generateKieModuleMetaInfo(ResourceStore trgMfs) {
        Map<String, TypeMetaInfo> typeInfos = new HashMap<>();
        Map<String, Set<String>> rulesPerPackage = new HashMap<>();

        KieModuleModel kieModuleModel = kModule.getKieModuleModel();
        for ( String kieBaseName : kieModuleModel.getKieBaseModels().keySet() ) {
            KnowledgeBuilder kBuilder = kModule.getKnowledgeBuilderForKieBase( kieBaseName );

            for ( KiePackage kPkg : kBuilder.getKnowledgePackages() ) {
                PackageRegistry pkgRegistry = (( InternalKnowledgeBuilder ) kBuilder).getPackageRegistry( kPkg.getName() );
                JavaDialectRuntimeData runtimeData = (JavaDialectRuntimeData) pkgRegistry.getDialectRuntimeRegistry().getDialectData( "java" );

                List<String> types = new ArrayList<>();
                for ( FactType factType : kPkg.getFactTypes() ) {
                    Class< ? > typeClass = ((ClassDefinition) factType).getDefinedClass();
                    TypeDeclaration typeDeclaration = pkgRegistry.getPackage().getTypeDeclaration( typeClass );
                    if ( typeDeclaration != null ) {
                        typeInfos.put( typeClass.getName(), new TypeMetaInfo(typeDeclaration) );
                    }

                    String className = factType.getName();
                    String internalName = className.replace('.', '/') + ".class";
                    if (trgMfs != null) {
                        byte[] bytes = runtimeData.getBytecode( internalName );
                        if ( bytes != null ) {
                            trgMfs.write( internalName, bytes, true );
                        }
                    }
                    types.add( internalName );
                }

                Set<String> rules = rulesPerPackage.get( kPkg.getName() );
                if( rules == null ) {
                    rules = new HashSet<>();
                }
                for ( Rule rule : kPkg.getRules() ) {
                    rules.add(rule.getName());
                }
                if (!rules.isEmpty()) {
                    rulesPerPackage.put(kPkg.getName(), rules);
                }
            }
        }
        return new KieModuleMetaInfo(typeInfos, rulesPerPackage);
    }
}
