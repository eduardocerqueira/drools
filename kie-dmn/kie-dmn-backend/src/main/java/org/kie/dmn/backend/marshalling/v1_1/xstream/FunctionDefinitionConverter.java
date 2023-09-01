package org.kie.dmn.backend.marshalling.v1_1.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kie.dmn.model.api.DMNModelInstrumentedBase;
import org.kie.dmn.model.api.Expression;
import org.kie.dmn.model.api.FunctionDefinition;
import org.kie.dmn.model.api.InformationItem;
import org.kie.dmn.model.v1_1.TFunctionDefinition;

public class FunctionDefinitionConverter extends ExpressionConverter {
    public static final String EXPRESSION = "expression";
    public static final String FORMAL_PARAMETER = "formalParameter";

    @Override
    protected void assignChildElement(Object parent, String nodeName, Object child) {
        FunctionDefinition fd = (FunctionDefinition) parent;
        
        if (FORMAL_PARAMETER.equals(nodeName)) {
            fd.getFormalParameter().add((InformationItem) child);
        } else if (child instanceof Expression) {
            fd.setExpression((Expression) child);
        } else {
            super.assignChildElement(parent, nodeName, child);
        }
    }

    @Override
    protected void assignAttributes(HierarchicalStreamReader reader, Object parent) {
        super.assignAttributes(reader, parent);

        // no attributes (drools:kind is now managed fully as a generic additional attribute in DMNModelInstrumentedBase)
    }

    @Override
    protected void writeChildren(HierarchicalStreamWriter writer, MarshallingContext context, Object parent) {
        super.writeChildren(writer, context, parent);
        FunctionDefinition fd = (FunctionDefinition) parent;
        
        for (InformationItem fparam : fd.getFormalParameter()) {
            writeChildrenNode(writer, context, fparam, FORMAL_PARAMETER);
        }
        if (fd.getExpression() != null) writeChildrenNode(writer, context, fd.getExpression(), MarshallingUtils.defineExpressionNodeName(fd.getExpression()));
    }

    @Override
    protected void writeAttributes(HierarchicalStreamWriter writer, Object parent) {
        super.writeAttributes(writer, parent);
        
        // no attributes.
    }

    public FunctionDefinitionConverter(XStream xstream) {
        super(xstream);
    }

    @Override
    protected DMNModelInstrumentedBase createModelObject() {
        return new TFunctionDefinition();
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(TFunctionDefinition.class);
    }

}
