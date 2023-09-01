package org.kie.dmn.trisotech.backend.marshalling.v1_3.xstream;

import javax.xml.namespace.QName;

import org.kie.dmn.api.marshalling.DMNExtensionRegister;
import org.kie.dmn.trisotech.model.v1_3.TConditional;
import org.kie.dmn.trisotech.model.v1_3.TFilter;
import org.kie.dmn.trisotech.model.v1_3.TIterator;
import org.kie.dmn.trisotech.model.v1_3.TNamedExpression;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;

public class TrisotechBoxedExtensionRegister implements DMNExtensionRegister {

    @Override
    public void registerExtensionConverters(XStream xStream) {
        xStream.alias("conditional", TConditional.class);
        xStream.alias("else", TNamedExpression.class);
        xStream.alias("filter", TFilter.class);
        xStream.alias("if", TNamedExpression.class);
        xStream.alias("in", TNamedExpression.class);
        xStream.alias("iterator", TIterator.class);
        xStream.alias("match", TNamedExpression.class);
        xStream.alias("return", TNamedExpression.class);
        xStream.alias("then", TNamedExpression.class);
        xStream.registerConverter(new ConditionalConverter( xStream ) );
        xStream.registerConverter(new FilterConverter( xStream ) );
        xStream.registerConverter(new IteratorConverter(xStream));
        xStream.registerConverter(new NamedExpressionConverter( xStream ) );

    }

    @Override
    public void beforeMarshal(Object o, QNameMap qmap) {
        qmap.registerMapping(new QName("https://www.trisotech.com/spec/DMN/20191111/EXT/", "conditional", "boxedext"), "conditional");
        qmap.registerMapping(new QName("https://www.trisotech.com/spec/DMN/20191111/EXT/", "if", "boxedext"), "if");
        qmap.registerMapping(new QName("https://www.trisotech.com/spec/DMN/20191111/EXT/", "then", "boxedext"), "then");
        qmap.registerMapping(new QName("https://www.trisotech.com/spec/DMN/20191111/EXT/", "else", "boxedext"), "else");
        qmap.registerMapping(new QName("https://www.trisotech.com/spec/DMN/20191111/EXT/", "filter", "boxedext"), "filter");
        qmap.registerMapping(new QName("https://www.trisotech.com/spec/DMN/20191111/EXT/", "in", "boxedext"), "in");
        qmap.registerMapping(new QName("https://www.trisotech.com/spec/DMN/20191111/EXT/", "return", "boxedext"), "return");
        qmap.registerMapping(new QName("https://www.trisotech.com/spec/DMN/20191111/EXT/", "iterator", "boxedext"), "iterator");
        qmap.registerMapping(new QName("https://www.trisotech.com/spec/DMN/20191111/EXT/", "match", "boxedext"), "match");

    }

}
