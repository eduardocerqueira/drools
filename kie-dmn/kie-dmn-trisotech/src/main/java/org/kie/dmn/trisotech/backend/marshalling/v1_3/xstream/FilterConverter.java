package org.kie.dmn.trisotech.backend.marshalling.v1_3.xstream;

import org.kie.dmn.backend.marshalling.v1_3.xstream.ExpressionConverter;
import org.kie.dmn.backend.marshalling.v1x.ConverterDefinesExpressionNodeName;
import org.kie.dmn.model.api.DMNModelInstrumentedBase;
import org.kie.dmn.model.api.Expression;
import org.kie.dmn.trisotech.model.api.Filter;
import org.kie.dmn.trisotech.model.api.NamedExpression;
import org.kie.dmn.trisotech.model.v1_3.TFilter;
import org.kie.dmn.trisotech.model.v1_3.TNamedExpression;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class FilterConverter extends ExpressionConverter implements ConverterDefinesExpressionNodeName {

    public static final String IN = "in";
    public static final String MATCH = "match";

    @Override
    protected void assignChildElement(Object parent, String nodeName, Object child) {
        Filter filter = (Filter) parent;

        if (IN.equals(nodeName)) {
            filter.setIn(((NamedExpression) child).getExpression());
        } else if (MATCH.equals(nodeName)) {
            filter.setMatch(((NamedExpression) child).getExpression());
        } else {
            super.assignChildElement(parent, nodeName, child);
        }

    }

    @Override
    protected void writeChildren(HierarchicalStreamWriter writer, MarshallingContext context, Object parent) {
        super.writeChildren(writer, context, parent);
        Filter filter = (Filter) parent;
        writeChildrenNode(writer, context, new TNamedExpression(IN, filter.getIn()), IN);
        writeChildrenNode(writer, context, new TNamedExpression(MATCH, filter.getMatch()), MATCH);

    }

    public FilterConverter(XStream xstream) {
        super(xstream);
    }

    @Override
    protected DMNModelInstrumentedBase createModelObject() {
        return new TFilter();
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(TFilter.class);
    }

    @Override
    public String defineExpressionNodeName(Expression e) {
        return "filter";
    }

}
