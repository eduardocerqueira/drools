package org.kie.dmn.backend.marshalling.v1_2.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kie.dmn.model.api.DMNElementReference;
import org.kie.dmn.model.api.DMNModelInstrumentedBase;
import org.kie.dmn.model.v1_2.TDMNElementReference;

public class DMNElementReferenceConverter
        extends DMNModelInstrumentedBaseConverter {

    private static final String HREF = "href";

    public DMNElementReferenceConverter(XStream xstream) {
        super( xstream );
    }

    public boolean canConvert(Class clazz) {
        return clazz.equals(TDMNElementReference.class);
    }

    @Override
    protected void assignChildElement(Object parent, String nodeName, Object child) {
        super.assignChildElement( parent, nodeName, child );
    }

    @Override
    protected void assignAttributes(HierarchicalStreamReader reader, Object parent) {
        super.assignAttributes( reader, parent );
        DMNElementReference er = (DMNElementReference) parent;

        String href = reader.getAttribute( HREF );

        er.setHref( href );
    }

    @Override
    protected DMNModelInstrumentedBase createModelObject() {
        return new TDMNElementReference();
    }

    @Override
    protected void writeChildren(HierarchicalStreamWriter writer, MarshallingContext context, Object parent) {
        super.writeChildren(writer, context, parent);
        
        // no children nodes.
    }
    @Override
    protected void writeAttributes(HierarchicalStreamWriter writer, Object parent) {
        super.writeAttributes(writer, parent);
        DMNElementReference er = (DMNElementReference) parent;
        
        if ( er.getHref() != null ) writer.addAttribute(HREF, er.getHref()); 
    }

}
