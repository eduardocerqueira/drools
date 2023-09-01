package org.drools.persistence.jpa.marshaller;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="mappedVarIdSeq", sequenceName="MAPPED_VAR_ID_SEQ")
public class MappedVariable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="mappedVarIdSeq")
    private Long   mappedVarId;

    @Version
    @Column(name = "OPTLOCK")
    private int    version;

    private Long variableId;

    private String variableType;

    private String processInstanceId;
    private Long taskId;
    private Long workItemId;

    public MappedVariable() {

    }

    public MappedVariable(Long variableId, String variableType, String processInstanceId) {
        this.variableId = variableId;
        this.variableType = variableType;
        this.processInstanceId = processInstanceId;
    }

    public MappedVariable(Long variableId, String variableType, String processInstanceId, Long taskId, Long workItemId) {
        this.variableId = variableId;
        this.variableType = variableType;
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
        this.workItemId = workItemId;
    }

    public Long getMappedVarId() {
        return mappedVarId;
    }

    public void setMappedVarId(Long mappedVarId) {
        this.mappedVarId = mappedVarId;
    }

    public Long getVariableId() {
        return variableId;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

    public String getVariableType() {
        return variableType;
    }

    public void setVariableType(String variableType) {
        this.variableType = variableType;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(Long workItemId) {
        this.workItemId = workItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MappedVariable that = (MappedVariable) o;

        if (processInstanceId != null ? !processInstanceId.equals(that.processInstanceId) : that.processInstanceId != null) {
            return false;
        }
        if (taskId != null ? !taskId.equals(that.taskId) : that.taskId != null) {
            return false;
        }
        if (variableId != null ? !variableId.equals(that.variableId) : that.variableId != null) {
            return false;
        }
        if (variableType != null ? !variableType.equals(that.variableType) : that.variableType != null) {
            return false;
        }
        if (workItemId != null ? !workItemId.equals(that.workItemId) : that.workItemId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = variableId != null ? variableId.hashCode() : 0;
        result = 31 * result + (variableType != null ? variableType.hashCode() : 0);
        result = 31 * result + (processInstanceId != null ? processInstanceId.hashCode() : 0);
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        result = 31 * result + (workItemId != null ? workItemId.hashCode() : 0);
        return result;
    }
}
