package org.drools.example.api.defaultkiesessionfromfile;

import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintStream;


public class DefaultKieSessionFromFileExample {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultKieSessionFromFileExample.class);

    public void go(PrintStream out) {
        KieServices ks = KieServices.Factory.get();
        KieRepository kr = ks.getRepository();

        KieModule kModule = kr.addKieModule(ks.getResources().newFileSystemResource(getFile("default-kiesession")));

        KieContainer kContainer = ks.newKieContainer(kModule.getReleaseId());

        KieSession kSession = kContainer.newKieSession();
        kSession.setGlobal("out", out);

        Object msg1 = createMessage(kContainer, "Dave", "Hello, HAL. Do you read me, HAL?");
        kSession.insert(msg1);
        kSession.fireAllRules();
    }

    public static void main(String[] args) {
        new DefaultKieSessionFromFileExample().go(System.out);
    }

    private static Object createMessage(KieContainer kContainer, String name, String text) {
        Object o = null;
        try {
            Class cl = kContainer.getClassLoader().loadClass("org.drools.example.api.defaultkiesession.Message");
            o = cl.getConstructor(new Class[]{String.class, String.class}).newInstance(name, text);
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return o;
    }

    public static File getFile(String exampleName) {
        File folder = new File("drools-examples-api").getAbsoluteFile();
        File exampleFolder = null;
        while (folder != null) {
            exampleFolder = new File(folder,
                                     exampleName);
            if (exampleFolder.exists()) {
                break;
            }
            exampleFolder = null;
            folder = folder.getParentFile();
        }

        if (exampleFolder != null) {

            File targetFolder = new File(exampleFolder,
                                         "target");
            if (!targetFolder.exists()) {
                throw new RuntimeException("The target folder does not exist, please build project " + exampleName + " first");
            }

            for (String str : targetFolder.list()) {
                if (str.startsWith(exampleName) && !str.endsWith("-sources.jar") && !str.endsWith("-tests.jar") && !str.endsWith("-javadoc.jar")) {
                    return new File(targetFolder, str);
                }
            }
        }

        throw new RuntimeException("The target jar does not exist, please build project " + exampleName + " first");
    }

}
