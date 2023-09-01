package org.drools.decisiontable.project;

import org.junit.After;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.io.KieResources;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiSheetsTest {
    
    private KieSession ksession;
    
    @After
    public void tearDown() {

        if (ksession != null) {
            ksession.dispose();
        }
    }

    @Test
    public void testNoSheet() {
        check(null, "Mario can drink");
    }

    @Test
    public void testSheet1() {
        check("Sheet1", "Mario can drink");
    }

    @Test
    public void testSheet2() {
        check("Sheet2", "Mario can drive");
    }

    @Test
    public void testSheet12() {
        check("Sheet1,Sheet2", "Mario can drink", "Mario can drive");
    }

    private void check(String sheets, String... expectedResults) {
        KieServices ks = KieServices.get();
        KieResources kr = ks.getResources();

        KieFileSystem kfs = ks.newKieFileSystem()
                .write("src/main/resources/org/drools/simple/candrink/CanDrink.drl.xls",
                        kr.newFileSystemResource("src/test/resources/data/CanDrinkAndDrive.drl.xls"))
                .write("src/main/resources/org/drools/simple/candrink/CanDrink.drl.xls.properties",
                        sheets != null ? "sheets="+sheets : "");

        KieModuleModel kproj = ks.newKieModuleModel();
        kproj.newKieBaseModel("dtblaleKB")
                .addPackage("org.drools.simple.candrink")
                .newKieSessionModel("dtable");

        kfs.writeKModuleXML(kproj.toXML());

        KieBuilder kb = ks.newKieBuilder(kfs).buildAll();
        KieContainer kc = ks.newKieContainer(kb.getKieModule().getReleaseId());

        ksession = kc.newKieSession("dtable");
        Result result = new Result();
        ksession.insert(result);
        ksession.insert(new Person("Mario", 45));
        
        ksession.fireAllRules();
        
        assertThat(result.toString()).contains(expectedResults);
    }
}
