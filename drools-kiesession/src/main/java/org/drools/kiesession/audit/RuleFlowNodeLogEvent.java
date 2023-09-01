package org.drools.kiesession.audit;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.kie.api.runtime.process.ProcessInstance;

public class RuleFlowNodeLogEvent extends RuleFlowLogEvent {
    
    private String nodeId;
    private String nodeName;
    private String nodeInstanceId;

    public RuleFlowNodeLogEvent(final int type,
                                final String nodeId,
                                final String nodeName,
                                final String nodeInstanceId,
                                ProcessInstance processInstance) {
        this(type, nodeId, nodeName, nodeInstanceId,
                processInstance.getProcessId(), processInstance.getProcessName(), processInstance.getId());
    }

    /**
     * Create a new ruleflow node log event.
     * 
     * @param type The type of event.  This can only be RULEFLOW_NODE_START or RULEFLOW_NODE_END.
     * @param processId The id of the process
     * @param processName The name of the process
     */
    public RuleFlowNodeLogEvent(final int type,
                                final String nodeId,
                                final String nodeName,
                                final String nodeInstanceId,
                                final String processId,
                                final String processName,
                                final Object processInstanceId) {
        super( type, processId, processName, processInstanceId );
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.nodeInstanceId = nodeInstanceId;
    }
    
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        nodeId = (String) in.readObject();
        nodeName = (String) in.readObject();
        nodeInstanceId = (String) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(nodeId);
        out.writeObject(nodeName);
        out.writeObject(nodeInstanceId);
    }

    public String getNodeId() {
        return nodeId;
    }
    
    public String getNodeName() {
        return nodeName;
    }
    
    public String getNodeInstanceId() {
        return nodeInstanceId;
    }
    
    public String toString() {
        String msg = null;
        switch ( this.getType() ) {
            case BEFORE_RULEFLOW_NODE_TRIGGERED :
                msg = "BEFORE PROCESS NODE TRIGGERED";
                break;
            case AFTER_RULEFLOW_NODE_TRIGGERED :
                msg = "AFTER PROCESS NODE TRIGGERED";
                break;
            case BEFORE_RULEFLOW_NODE_EXITED :
                msg = "BEFORE PROCESS NODE EXITED";
                break;
            case AFTER_RULEFLOW_NODE_EXITED :
                msg = "AFTER PROCESS NODE EXITED";
                break;
            default:
                return super.toString();
        }
        return msg + " node:" + nodeName + "[id=" + nodeId + "] process:" + getProcessName() + "[id=" + getProcessId() + "]";
    }

}
