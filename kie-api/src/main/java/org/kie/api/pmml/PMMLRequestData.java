package org.kie.api.pmml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class provides a vanilla mechanism for sending data to
 * the rule engine, for use in having a PMML model applied against it
 * 
 */

@XmlType(name="PMMLRequestData")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="pmmlRequestData")
public class PMMLRequestData {
    @XmlAttribute(name="correlationId", required=true)
    private String correlationId;
    @XmlAttribute(name="modelName", required=true)
    private String modelName;
    @XmlAttribute(name="source")
    private String source;
    @XmlElementWrapper(name="requestParams")
    private List<ParameterInfo<?>> requestParams;

    public PMMLRequestData() {
        this.requestParams = new ArrayList<>();
    }
    
    public PMMLRequestData(String correlationId) {
        this.correlationId = correlationId;
        this.requestParams = new ArrayList<>();
    }

    public PMMLRequestData(String correlationId, String modelName) {
        this.correlationId = correlationId;
        this.modelName = modelName;
        this.requestParams = new ArrayList<>();
    }

    public String getModelName() {
        return this.modelName;
    }

    public String getCompactCapitalizedModelName() {
        String compactName = modelName.replaceAll("\\s", "");
        return compactName.substring(0,1).toUpperCase()+compactName.substring(1);
    }

    public synchronized Map<String, ParameterInfo> getMappedRequestParams() {
        return requestParams.stream().collect(Collectors.toMap(pi -> pi.getName(),pi -> pi));
    }

    public synchronized Collection<ParameterInfo> getRequestParams() {
        return new ArrayList<>(this.requestParams);
    }

    public synchronized boolean addRequestParam(ParameterInfo parameter) {
        this.requestParams.removeIf(pi -> parameter.getName().equals(pi.getName()));
        return this.requestParams.add(parameter);
    }

    public synchronized boolean removeRequestParam(ParameterInfo parameter) {
        return this.requestParams.remove(parameter);
    }

    public synchronized boolean addRequestParam(String paramName, Object value) {
        if (paramName == null || paramName.trim().isEmpty() || value == null) {
            return false;
        }
        Class<?> clazz = value.getClass();
        ParameterInfo parameter = new ParameterInfo(this.correlationId, paramName, clazz, value);
        return this.addRequestParam(parameter);
    }
    
    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((correlationId == null) ? 0 : correlationId.hashCode());
        result = prime * result + ((modelName == null) ? 0 : modelName.hashCode());
        result = prime * result + ((requestParams == null) ? 0 : requestParams.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PMMLRequestData other = (PMMLRequestData) obj;
        if (correlationId == null) {
            if (other.correlationId != null) {
                return false;
            }
        } else if (!correlationId.equals(other.correlationId)) {
            return false;
        }
        if (modelName == null) {
            if (other.modelName != null) {
                return false;
            }
        } else if (!modelName.equals(other.modelName)) {
            return false;
        }
        if (requestParams == null) {
            if (other.requestParams != null) {
                return false;
            }
        } else if (!requestParams.equals(other.requestParams)) {
            return false;
        }
        if (source == null) {
            if (other.source != null) {
                return false;
            }
        } else if (!source.equals(other.source)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("PMMLRequestData( ");
        stringBuilder.append("correlationId=").append(correlationId).append(", ");
        stringBuilder.append("modelName=").append(modelName).append(", ");
        stringBuilder.append("source=").append(source).append(", requestParams=[");
        Iterator<ParameterInfo<?>> iter = requestParams.iterator();
        boolean firstParam = true;
        while (iter.hasNext()) {
            if (!firstParam) {
                stringBuilder.append(", ");
            } else {
                firstParam = false;
            }
            ParameterInfo<?> pi = iter.next();
            stringBuilder.append(pi.toString());
        }
        stringBuilder.append("] )");
        return stringBuilder.toString();
    }
}
