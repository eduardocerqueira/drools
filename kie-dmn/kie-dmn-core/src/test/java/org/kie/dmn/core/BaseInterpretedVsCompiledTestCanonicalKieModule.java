package org.kie.dmn.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.drools.modelcompiler.CanonicalKieModule;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.dmn.core.compiler.ExecModelCompilerOption;

/**
 * DROOLS-3238
 * This base test class purpose is to test DMN model in KJAR containing Drools Model file.<br/>
 * Specifically, as long as the base method {@link #wrapWithDroolsModelResource(KieServices, ReleaseId, Resource...)} is used, this will enable testing a DMN resource
 * running alongside the CanonicalKieModule, which is needed while using the executable model for DRL file.<br/><br/>
 * <i>Please note that these tests don't actually verify the correct behaviour of the compiled DRL files, but they only verify the DMN model.</i> Ref: https://github.com/kiegroup/drools/pull/2460#issue-298982811
 */
@RunWith(Parameterized.class)
public abstract class BaseInterpretedVsCompiledTestCanonicalKieModule {

    @Parameterized.Parameters
    public static Object[] params() {
        return new Object[][]{ { false, true}, {false, false} };
    }

    private final boolean useExecModelCompiler;
    protected final boolean canonicalKieModule;

    public BaseInterpretedVsCompiledTestCanonicalKieModule(boolean useExecModelCompiler, boolean canonicalKieModule) {
        this.useExecModelCompiler = useExecModelCompiler;
        this.canonicalKieModule = canonicalKieModule;
    }

    @Before
    public void before() {
        System.setProperty(ExecModelCompilerOption.PROPERTY_NAME, Boolean.toString(useExecModelCompiler));
    }

    @After
    public void after() {
        System.clearProperty(ExecModelCompilerOption.PROPERTY_NAME);
    }

    public Resource[] wrapWithDroolsModelResource(KieServices ks, ReleaseId releaseId, Resource... original) {
        List<Resource> resources = new ArrayList<>(Arrays.asList(original));
        if(canonicalKieModule) {
            resources.add(getDroolsModelResource(ks, releaseId));
        }
        return resources.toArray(new Resource[0]);
    }

    private Resource getDroolsModelResource(KieServices ks, ReleaseId releaseId) {
        return ks.getResources()
                .newClassPathResource("/org/kie/dmn/core/drools-model", this.getClass())
                .setTargetPath(CanonicalKieModule.getModelFileWithGAV(releaseId));
    }
}
