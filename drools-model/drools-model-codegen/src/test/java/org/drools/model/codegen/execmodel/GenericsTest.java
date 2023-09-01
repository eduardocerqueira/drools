package org.drools.model.codegen.execmodel;

import java.util.ArrayList;
import java.util.List;

import org.drools.model.codegen.execmodel.domain.Address;
import org.drools.model.codegen.execmodel.domain.Person;

import org.junit.Test;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericsTest extends BaseModelTest {

    public GenericsTest(RUN_TYPE testRunType) {
        super(testRunType);
    }

    @Test
    public void testGenericsAccumulateInlineCode() {
        // accumulate inline code supports generics
        String str =
                "import " + Person.class.getCanonicalName() + ";\n" +
                "import " + Address.class.getCanonicalName() + ";\n" +
                "import " + List.class.getCanonicalName() + ";\n" +
                "import " + ArrayList.class.getCanonicalName() + ";\n" +
                "global List results;\n" +
                "dialect \"mvel\"\n" +
                "rule R when\n" +
                "  $l : List() from accumulate (Person($addrList : addresses),\n" +
                "         init( List<String> cityList = new ArrayList(); ),\n" +
                "         action( for(Address addr: $addrList){String city = addr.getCity(); cityList.add(city);} ),\n" +
                "         result( cityList )\n" +
                "       )\n" +
                "then\n" +
                "  results.add($l);\n" +
                "end";

        KieSession ksession = getKieSession(str);
        List<List<String>> results = new ArrayList<>();
        ksession.setGlobal("results", results);

        Person john = new Person("John");
        Address addr1 = new Address("1");
        Address addr2 = new Address("2");
        john.getAddresses().add(addr1);
        john.getAddresses().add(addr2);

        Person paul = new Person("Paul");
        Address addr3 = new Address("3");
        Address addr4 = new Address("4");
        john.getAddresses().add(addr3);
        john.getAddresses().add(addr4);

        ksession.insert(john);
        ksession.insert(paul);
        ksession.fireAllRules();
        assertThat(results.get(0).size()).isEqualTo(4);
    }
}