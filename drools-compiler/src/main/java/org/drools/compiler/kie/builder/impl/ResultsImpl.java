package org.drools.compiler.kie.builder.impl;

import java.util.ArrayList;
import java.util.List;

import org.drools.drl.parser.MessageImpl;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.Results;
import org.kie.internal.builder.InternalMessage;
import org.kie.internal.builder.KnowledgeBuilderResult;
import org.kie.internal.jci.CompilationProblem;

public class ResultsImpl
    implements
    Results {
    private List<Message> messages    = new ArrayList<>();

    private long          idGenerator = 1L;

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public boolean hasMessages(Level... levels) {
        return !filterMessages( levels ).isEmpty();
    }

    @Override
    public List<Message> getMessages(Level... levels) {
        return filterMessages( levels );
    }

    public void addMessage(CompilationProblem problem) {
        messages.add( new MessageImpl( idGenerator++,
                                       problem ) );
    }

    public InternalMessage addMessage(KnowledgeBuilderResult result) {
        InternalMessage message = result.asMessage(idGenerator++);
        messages.add( message );
        return message;
    }

    public InternalMessage addMessage(Level level, String path, String text) {
        InternalMessage message = new MessageImpl(idGenerator++, level, path, text);
        messages.add( message );
        return message;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public long getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(long idGenerator) {
        this.idGenerator = idGenerator;
    }

    public List<Message> filterMessages(Level... levels) {
        return MessageImpl.filterMessages( messages,
                                           levels );
    }

    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append( "Error Messages:\n" );
        for ( Message msg : filterMessages( Level.ERROR ) ) {
            sBuilder.append( msg.toString() );
            sBuilder.append( "\n" );
        }

        sBuilder.append( "---\n" );
        sBuilder.append( "Warning Messages:\n" );
        for ( Message msg : filterMessages( Level.WARNING ) ) {
            sBuilder.append( msg.toString() );
            sBuilder.append( "\n" );
        }
        
        sBuilder.append( "---\n" );
        sBuilder.append( "Info Messages:\n" );
        for ( Message msg : filterMessages( Level.INFO ) ) {
            sBuilder.append( msg.toString() );
            sBuilder.append( "\n" );
        }        
        return sBuilder.toString();
    }

}
