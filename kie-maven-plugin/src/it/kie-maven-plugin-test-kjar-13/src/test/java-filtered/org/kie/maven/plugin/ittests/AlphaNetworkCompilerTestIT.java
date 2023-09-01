package org.kie.maven.plugin.ittests;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.drools.ancompiler.CompiledNetwork;
import org.drools.ancompiler.ObjectTypeNodeCompiler;
import org.drools.kiesession.rulebase.InternalKnowledgeBase;
import org.drools.core.reteoo.ObjectSinkPropagator;
import org.drools.core.reteoo.ObjectTypeNode;
import org.drools.core.reteoo.Rete;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlphaNetworkCompilerTestIT {

    private static final String GAV_ARTIFACT_ID = "kie-maven-plugin-test-kjar-13";
    private static final String GAV_VERSION = "${org.kie.version}";
    private final static String KBASE_NAME = "kbase-compiled-alphanetwork";

    @Test
    public void testAlphaNetworkCompiler() throws Exception {
        final URL targetLocation = AlphaNetworkCompilerTestIT.class.getProtectionDomain().getCodeSource().getLocation();
        final KieContainer kieContainer = ITTestsUtils.getKieContainer(targetLocation, GAV_ARTIFACT_ID, GAV_VERSION);
        final KieBase kieBase = kieContainer.getKieBase(KBASE_NAME);
        Assertions.assertThat(kieBase).isNotNull();
        KieSession kSession = null;
        try {

            kSession = kieBase.newKieSession();
            Assertions.assertThat(kSession).isNotNull();

            ClassLoader classLoader = kieContainer.getClassLoader();
            Class<?> aClass = Class.forName("org.compiledalphanetwork.Person", true, classLoader);
            Constructor<?> constructor = aClass.getConstructor(String.class);
            Object lucaFactA = constructor.newInstance("Luca");

            kSession.insert(lucaFactA);
            int rulesFired = kSession.fireAllRules();
            kSession.dispose();

            assertEquals(1, rulesFired);

            assertReteIsAlphaNetworkCompiled(kSession);

        } finally {
            kSession.dispose();
        }
    }

    protected void assertReteIsAlphaNetworkCompiled(KieSession ksession) {
        Rete rete = ((InternalKnowledgeBase) ksession.getKieBase()).getRete();
        List<ObjectTypeNode> objectTypeNodes = ObjectTypeNodeCompiler.objectTypeNodes(rete);
        for(ObjectTypeNode otn : objectTypeNodes) {
            ObjectSinkPropagator objectSinkPropagator = otn.getObjectSinkPropagator();
            System.out.println(objectSinkPropagator.getClass().getCanonicalName());
            assertTrue(objectSinkPropagator instanceof CompiledNetwork);
        }
    }
}