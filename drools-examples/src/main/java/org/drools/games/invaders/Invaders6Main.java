package org.drools.games.invaders;

import org.drools.games.GameUI;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Invaders6Main {

    private static final Logger LOG = LoggerFactory.getLogger(Invaders6Main.class);

    public static void main(String[] args) {
        new Invaders6Main().init(true);
    }

    public Invaders6Main() {
    }

    public void init(boolean exitOnClose) {
        KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
        final KieSession ksession = kc.newKieSession( "Invaders6KS");

        InvadersConfiguration conf = new InvadersConfiguration();
        conf.setExitOnClose(exitOnClose);

        GameUI ui = new GameUI(ksession, conf);
        ui.init();

        ksession.setGlobal( "conf", conf );
        ksession.setGlobal( "ui", ui );
        ksession.setGlobal( "fpsTimer", new FPSTimer(10) );

        runKSession(ksession);
    }

    public void runKSession(final KieSession ksession) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            public void run() {
                // run forever
                try {
                    ksession.fireUntilHalt();
                } catch ( Exception e ) {
                    LOG.error("Exception", e);
                }
            }
        });
    }
}
