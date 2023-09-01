package org.drools.model.codegen.execmodel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenersTest extends BaseModelTest {

    public ListenersTest( RUN_TYPE testRunType ) {
        super( testRunType );
    }

    @Test
    public void testInsert() {
        String str =
                "rule R\n" +
                "when\n" +
                "  $i: Integer()\n" +
                "then\n" +
                "  insert(\"\" + $i);\n" +
                "end";

        KieSession ksession = getKieSession(str);

        List<String> results = new ArrayList<>();

        final RuleRuntimeEventListener workingMemoryListener = new RuleRuntimeEventListener() {
            public void objectInserted( ObjectInsertedEvent event) {
                if (event.getObject() instanceof String) {
                    results.add( event.getRule().getName() );
                }
            }

            public void objectUpdated( ObjectUpdatedEvent event) {
            }

            public void objectDeleted( ObjectDeletedEvent event) {
            }

        };

        ksession.addEventListener( workingMemoryListener );

        ksession.insert(42);
        assertThat(ksession.fireAllRules()).isEqualTo(1);
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isEqualTo("R");
    }
}
