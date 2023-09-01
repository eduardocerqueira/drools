package org.drools.verifier.overlaps;

import org.drools.verifier.TestBaseOld;
import org.drools.verifier.Verifier;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;
import org.drools.verifier.builder.VerifierImpl;
import org.drools.verifier.report.components.Overlap;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kie.internal.io.ResourceFactory;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.ClassObjectFilter;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class OverlappingRestrictionsTest extends TestBaseOld {

    // TODO: Add this feature
    @Test
    @Disabled
    void testOverlap() {
        VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();

        Verifier verifier = vBuilder.newVerifier();

        verifier.addResourcesToVerify(ResourceFactory.newClassPathResource(
                "RestrictionsTest.drl", getClass()), ResourceType.DRL);

        assertThat(verifier.hasErrors()).isFalse();

        boolean noProblems = verifier.fireAnalysis();
        assertThat(noProblems).isTrue();

        Collection<? extends Object> overlaps = ((VerifierImpl) verifier)
                .getKnowledgeSession().getObjects(
                new ClassObjectFilter(Overlap.class));

        for (Object object : overlaps) {
            System.out.println(object);
        }

        assertThat(overlaps.size()).isEqualTo(3);

        verifier.dispose();

    }

    @Test
    void testDUMMY() throws Exception {
        assertThat(true).isTrue();
    }
}
