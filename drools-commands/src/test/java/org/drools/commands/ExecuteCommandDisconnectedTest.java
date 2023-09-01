package org.drools.commands;

import java.util.ArrayList;
import java.util.List;

import org.drools.commands.runtime.rule.InsertObjectCommand;
import org.drools.core.common.DefaultFactHandle;
import org.drools.commands.runtime.ExecutionResultImpl;
import org.drools.kiesession.rulebase.KnowledgeBaseFactory;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.runtime.ExecutableRunner;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.RequestContext;
import org.kie.internal.command.CommandFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ExecuteCommandDisconnectedTest {

    @Test
    public void executeDisconnected() {
        KieBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

        KieSession ksession = kbase.newKieSession();
        ExecutionResultImpl localKresults = new ExecutionResultImpl();

        RequestContext context = RequestContext.create().with( ksession );

        ExecutableRunner runner = ExecutableRunner.create();

        List cmds = new ArrayList();
        cmds.add(new InsertObjectCommand(new String("Hi!"), "handle"));

        BatchExecutionCommand batchCmd = CommandFactory.newBatchExecution(cmds, "kresults");
        ExecuteCommand execCmd = new ExecuteCommand(batchCmd,true);

        ExecutionResults results = execCmd.execute( context );

        assertThat(results).isNotNull();

        assertThat(results.getFactHandle("handle")).isNotNull();

        assertThat(((DefaultFactHandle) results.getFactHandle("handle")).isDisconnected()).isTrue();

        cmds = new ArrayList();
        cmds.add(new InsertObjectCommand(new String("Hi!"), "handle"));
        batchCmd = CommandFactory.newBatchExecution(cmds, "kresults");
        execCmd = new ExecuteCommand(batchCmd);

        results = execCmd.execute( context );

        assertThat(results).isNotNull();

        assertThat(results.getFactHandle("handle")).isNotNull();

        assertThat(((DefaultFactHandle) results.getFactHandle("handle")).isDisconnected()).isFalse();

    }
}
