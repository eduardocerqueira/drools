package org.drools.verifier.visitor;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;

import org.drools.drl.ast.descr.PackageDescr;
import org.drools.drl.parser.DrlParser;
import org.drools.verifier.components.Pattern;
import org.drools.verifier.components.Restriction;
import org.drools.verifier.components.VerifierComponentType;
import org.drools.verifier.data.VerifierComponent;
import org.drools.verifier.data.VerifierData;
import org.drools.verifier.data.VerifierReportFactory;
import org.junit.jupiter.api.Test;
import org.kie.internal.builder.conf.LanguageLevelOption;

import static org.assertj.core.api.Assertions.assertThat;

public class NestedPatternsTest {

    @Test
    void runVisitor() throws Exception {
        VerifierData data = VerifierReportFactory.newVerifierData();
        PackageDescrVisitor visitor = new PackageDescrVisitor(data,
                Collections.EMPTY_LIST);

        assertThat(data).isNotNull();

        Reader drlReader = new InputStreamReader(getClass().getResourceAsStream("NestedPatterns.drl"));
        PackageDescr packageDescr = new DrlParser(LanguageLevelOption.DRL5).parse(drlReader);

        assertThat(packageDescr).isNotNull();

        visitor.visitPackageDescr(packageDescr);

        Collection<VerifierComponent> all = data.getAll();
        int patternCount = 0;
        for (VerifierComponent verifierComponent : all) {

            if (verifierComponent.getVerifierComponentType().equals(VerifierComponentType.PATTERN)) {
                patternCount++;
            }
        }
        assertThat(patternCount).isEqualTo(4);

        Collection<Pattern> patterns = data.getAll(VerifierComponentType.PATTERN);

//        for ( Pattern pattern : patterns ) {
//            System.out.println( pattern.getPath() + " " + pattern );
//        }

        assertThat(patterns).isNotNull();
        assertThat(patterns.size()).isEqualTo(4);

        Collection<Restriction> restrictions = data.getAll(VerifierComponentType.RESTRICTION);

//        for ( Restriction restriction : restrictions ) {
//            System.out.println( restriction.getPath() + " " + restriction );
//        }

        assertThat(restrictions).isNotNull();
        assertThat(restrictions.size()).isEqualTo(3);

    }
}
