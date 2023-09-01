package org.drools.games.pong;

import org.drools.games.invaders.FPSTimer;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PongMain {

    private static final Logger LOG = LoggerFactory.getLogger(PongMain.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
        new PongMain().init(kc, true);
    }

    public PongMain() {
    }

    public void init(final KieContainer kc, boolean exitOnClose) {
        final KieSession ksession = kc.newKieSession( "PongKS");
        PongConfiguration pconf = new PongConfiguration();
        pconf.setExitOnClose(exitOnClose);
        ksession.setGlobal("pconf", pconf);
        ksession.setGlobal( "fpsTimer", new FPSTimer(10) );
        //ksession.getAgenda().getAgendaGroup("Init").setFocus( );

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
