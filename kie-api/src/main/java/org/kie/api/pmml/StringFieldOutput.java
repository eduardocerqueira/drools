package org.kie.api.pmml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="value")
@XmlAccessorType(XmlAccessType.FIELD)
public class StringFieldOutput extends AbstractOutput<String> {
    private String value;

    public StringFieldOutput() {
        super();
    }
    
    public StringFieldOutput(String correlationId, String name, String displayValue, Double weight, String value) {
        super(correlationId, name, displayValue, weight);
        this.value = value;
    }

    public StringFieldOutput(String correlationId, String segmentationId, String segmentId, String name,
            String displayValue, Double weight, String value) {
        super(correlationId, segmentationId, segmentId, name, displayValue, weight);
        this.value = value;
    }

    public StringFieldOutput(String correlationId, String segmentationId, String segmentId, String name, String value) {
        super(correlationId, segmentationId, segmentId, name);
        this.value = value;
    }

    public StringFieldOutput(String correlationId, String name, String value) {
        super(correlationId, name);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StringFieldOutput other = (StringFieldOutput) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StringFieldOutput [correlationId=" + getCorrelationId() + ", segmentationId="
                + getSegmentationId() + ", segmentId=" + getSegmentId() + ", name=" + getName()
                + ", displayValue=" + getDisplayValue() + ", value=" + value + ", weight=" + weight + "]";
    }
    
}
