package org.kie.dmn.model.v1_3.dmndi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

public class DMNDiagram extends Diagram implements org.kie.dmn.model.api.dmndi.DMNDiagram {

    protected org.kie.dmn.model.api.dmndi.Dimension size;
    protected List<org.kie.dmn.model.api.dmndi.DiagramElement> dmnDiagramElement;

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link Dimension }
     *     
     */
    public org.kie.dmn.model.api.dmndi.Dimension getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link Dimension }
     *     
     */
    public void setSize(org.kie.dmn.model.api.dmndi.Dimension value) {
        this.size = value;
    }

    /**
     * Gets the value of the dmnDiagramElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dmnDiagramElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDMNDiagramElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link DMNShape }{@code >}
     * {@link JAXBElement }{@code <}{@link DiagramElement }{@code >}
     * {@link JAXBElement }{@code <}{@link DMNEdge }{@code >}
     * 
     * 
     */
    public List<org.kie.dmn.model.api.dmndi.DiagramElement> getDMNDiagramElement() {
        if (dmnDiagramElement == null) {
            dmnDiagramElement = new ArrayList<>();
        }
        return this.dmnDiagramElement;
    }

}
