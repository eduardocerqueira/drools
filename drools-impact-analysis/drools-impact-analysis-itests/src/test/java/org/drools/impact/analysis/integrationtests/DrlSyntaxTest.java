package org.drools.impact.analysis.integrationtests;

import java.util.ArrayList;
import java.util.List;

import org.drools.impact.analysis.graph.Graph;
import org.drools.impact.analysis.graph.ModelToGraphConverter;
import org.drools.impact.analysis.graph.ReactivityType;
import org.drools.impact.analysis.integrationtests.domain.Address;
import org.drools.impact.analysis.integrationtests.domain.Person;
import org.drools.impact.analysis.model.AnalysisModel;
import org.drools.impact.analysis.parser.ModelBuilder;
import org.junit.Test;

/**
 * 
 * Tests related to DRL syntax. exists, not, ,accumulate etc.
 *
 */
public class DrlSyntaxTest extends AbstractGraphTest {

    @Test
    public void testExists1() {
        String str =
                "package mypkg;\n" +
                     "import " + Person.class.getCanonicalName() + ";" +
                     "rule R1 when\n" +
                     "then\n" +
                     "  insert(new Person());" +
                     "end\n" +
                     "rule R2 when\n" +
                     "  exists (Person())\n" +
                     "then\n" +
                     "end\n";

        AnalysisModel analysisModel = new ModelBuilder().build(str);

        ModelToGraphConverter converter = new ModelToGraphConverter();
        Graph graph = converter.toGraph(analysisModel);

        assertLink(graph, "mypkg.R1", "mypkg.R2", ReactivityType.POSITIVE);
    }

    @Test
    public void testExists2() {
        String str =
                "package mypkg;\n" +
                     "import " + Person.class.getCanonicalName() + ";" +
                     "import " + Address.class.getCanonicalName() + ";" +
                     "rule R1 when\n" +
                     "  $p : Person()\n" +
                     "then\n" +
                     "  delete($p);" +
                     "end\n" +
                     "rule R2 when\n" +
                     "  exists (Person())\n" +
                     "then\n" +
                     "end\n";

        AnalysisModel analysisModel = new ModelBuilder().build(str);

        ModelToGraphConverter converter = new ModelToGraphConverter();
        Graph graph = converter.toGraph(analysisModel);

        assertLink(graph, "mypkg.R1", "mypkg.R1", ReactivityType.NEGATIVE);
        assertLink(graph, "mypkg.R1", "mypkg.R2", ReactivityType.NEGATIVE);
    }

    @Test
    public void testNot1() {
        String str =
                "package mypkg;\n" +
                     "import " + Person.class.getCanonicalName() + ";" +
                     "rule R1 when\n" +
                     "then\n" +
                     "  insert(new Person());" +
                     "end\n" +
                     "rule R2 when\n" +
                     "  not (Person())\n" +
                     "then\n" +
                     "end\n";

        AnalysisModel analysisModel = new ModelBuilder().build(str);

        ModelToGraphConverter converter = new ModelToGraphConverter();
        Graph graph = converter.toGraph(analysisModel);

        assertLink(graph, "mypkg.R1", "mypkg.R2", ReactivityType.NEGATIVE);
    }

    @Test
    public void testNot2() {
        String str =
                "package mypkg;\n" +
                     "import " + Person.class.getCanonicalName() + ";" +
                     "import " + Address.class.getCanonicalName() + ";" +
                     "rule R1 when\n" +
                     "  $p : Person()\n" +
                     "then\n" +
                     "  delete($p);" +
                     "end\n" +
                     "rule R2 when\n" +
                     "  not (Person())\n" +
                     "then\n" +
                     "end\n";

        AnalysisModel analysisModel = new ModelBuilder().build(str);

        ModelToGraphConverter converter = new ModelToGraphConverter();
        Graph graph = converter.toGraph(analysisModel);

        assertLink(graph, "mypkg.R1", "mypkg.R1", ReactivityType.NEGATIVE);
        assertLink(graph, "mypkg.R1", "mypkg.R2", ReactivityType.POSITIVE);
    }

    @Test
    public void testGlobal() {
        String str =
                "package mypkg;\n" +
                     "import " + Person.class.getCanonicalName() + ";" +
                     "global java.util.List resultList;" +
                     "rule R1 when\n" +
                     "  $p : Person(name == \"Mario\")\n" +
                     "then\n" +
                     "  modify($p) { setAge( 18 ) };" +
                     "  insert(\"Done\");\n" +
                     "end\n" +
                     "rule R2 when\n" +
                     "  $p : Person(age > 15)\n" +
                     "then\n" +
                     "end\n" +
                     "rule R3 when\n" +
                     "  $p : String(this == \"Done\")\n" +
                     "then\n" +
                     "  resultList.add($p);" +
                     "end\n";

        List<Person> resultList = new ArrayList<>();
        runRuleWithGlobal(str, "resultList", resultList, new Person("Mario", 10));

        AnalysisModel analysisModel = new ModelBuilder().build(str);

        ModelToGraphConverter converter = new ModelToGraphConverter();
        Graph graph = converter.toGraph(analysisModel);

        assertLink(graph, "mypkg.R1", "mypkg.R2", ReactivityType.POSITIVE);
        assertLink(graph, "mypkg.R1", "mypkg.R3", ReactivityType.POSITIVE);
    }
}
