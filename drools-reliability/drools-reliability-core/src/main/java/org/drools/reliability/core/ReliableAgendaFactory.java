package org.drools.reliability.core;


import org.drools.core.common.AgendaFactory;
import org.drools.core.common.InternalAgenda;
import org.drools.core.common.InternalWorkingMemory;

import java.io.Serializable;

public class ReliableAgendaFactory implements AgendaFactory, Serializable {

    private static final AgendaFactory INSTANCE = new ReliableAgendaFactory();

    public static AgendaFactory getInstance() {
        return INSTANCE;
    }

    private ReliableAgendaFactory() { }

    public InternalAgenda createAgenda(InternalWorkingMemory workingMemory) {
        return new ReliableAgenda( workingMemory );
    }
}
