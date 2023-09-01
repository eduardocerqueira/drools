package org.drools.metric;

import java.util.ArrayList;
import java.util.List;

import org.drools.mvel.compiler.Person;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstraintsTest extends AbstractMetricTest {

    @Test
    public void testDoubleBetaConstraints() {
        String str =
                "import " + Person.class.getCanonicalName() + "\n" +
                     "global java.util.List list;\n" +
                     "rule R when\n" +
                     "  Person( $age : age, $name : name )\n" +
                     "  Person( name == $name, age == $age + 1 )\n" +
                     "then\n" +
                     "  list.add($age);\n" +
                     "end\n";

        KieBase kbase = loadKnowledgeBaseFromString(str);
        KieSession ksession = kbase.newKieSession();

        List<Integer> list = new ArrayList<>();
        ksession.setGlobal("list", list);

        Person p1 = new Person("AAA", 31);
        Person p2 = new Person("AAA", 34);
        Person p3 = new Person("AAA", 33);

        ksession.insert(p1);
        ksession.insert(p2);
        ksession.insert(p3);

        ksession.fireAllRules();
        assertThat(list.size()).isEqualTo(1);
        assertThat((int) list.get(0)).isEqualTo(33);
    }

    @Test
    public void testTripleBetaConstraints() {
        String str =
                "import " + Person.class.getCanonicalName() + "\n" +
                     "global java.util.List list;\n" +
                     "rule R when\n" +
                     "  Person( $age : age, $name : name, $happy : happy )\n" +
                     "  Person( name == $name, age == $age + 1, happy == $happy )\n" +
                     "then\n" +
                     "  list.add($age);\n" +
                     "end\n";

        KieBase kbase = loadKnowledgeBaseFromString(str);
        KieSession ksession = kbase.newKieSession();

        List<Integer> list = new ArrayList<>();
        ksession.setGlobal("list", list);

        Person p1 = new Person("AAA", 31, true);
        Person p2 = new Person("AAA", 34, true);
        Person p3 = new Person("AAA", 33, true);

        ksession.insert(p1);
        ksession.insert(p2);
        ksession.insert(p3);

        ksession.fireAllRules();
        assertThat(list.size()).isEqualTo(1);
        assertThat((int) list.get(0)).isEqualTo(33);
    }

    @Test
    public void testQuadroupleBetaConstraints() {
        String str =
                "import " + Person.class.getCanonicalName() + "\n" +
                     "global java.util.List list;\n" +
                     "rule R when\n" +
                     "  Person( $age : age, $name : name, $happy : happy, $alive : alive )\n" +
                     "  Person( name == $name, age == $age + 1, happy == $happy, alive == $alive )\n" +
                     "then\n" +
                     "  list.add($age);\n" +
                     "end\n";

        KieBase kbase = loadKnowledgeBaseFromString(str);
        KieSession ksession = kbase.newKieSession();

        List<Integer> list = new ArrayList<>();
        ksession.setGlobal("list", list);

        Person p1 = new Person("AAA", 31, true);
        p1.setAlive(true);
        Person p2 = new Person("AAA", 34, true);
        p2.setAlive(true);
        Person p3 = new Person("AAA", 33, true);
        p3.setAlive(true);

        ksession.insert(p1);
        ksession.insert(p2);
        ksession.insert(p3);

        ksession.fireAllRules();
        assertThat(list.size()).isEqualTo(1);
        assertThat((int) list.get(0)).isEqualTo(33);
    }

    @Test
    public void testDefaultBetaConstraints() {
        String str =
                "import " + Person.class.getCanonicalName() + "\n" +
                     "global java.util.List list;\n" +
                     "rule R when\n" +
                     "  Person( $age : age, $name : name, $happy : happy, $alive : alive, $status : status )\n" +
                     "  Person( name == $name, age == $age + 1, happy == $happy, alive == $alive, status == $status )\n" +
                     "then\n" +
                     "  list.add($age);\n" +
                     "end\n";

        KieBase kbase = loadKnowledgeBaseFromString(str);
        KieSession ksession = kbase.newKieSession();

        List<Integer> list = new ArrayList<>();
        ksession.setGlobal("list", list);

        Person p1 = new Person("AAA", 31, true);
        p1.setAlive(true);
        p1.setStatus("OK");
        Person p2 = new Person("AAA", 34, true);
        p2.setAlive(true);
        p2.setStatus("OK");
        Person p3 = new Person("AAA", 33, true);
        p3.setAlive(true);
        p3.setStatus("OK");

        ksession.insert(p1);
        ksession.insert(p2);
        ksession.insert(p3);

        ksession.fireAllRules();
        assertThat(list.size()).isEqualTo(1);
        assertThat((int) list.get(0)).isEqualTo(33);
    }
}
