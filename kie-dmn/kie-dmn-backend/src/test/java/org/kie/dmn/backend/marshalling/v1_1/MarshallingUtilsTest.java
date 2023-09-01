package org.kie.dmn.backend.marshalling.v1_1;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.junit.Test;
import org.kie.dmn.backend.marshalling.v1_1.xstream.MarshallingUtils;
import org.kie.dmn.model.api.DMNModelInstrumentedBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;


public class MarshallingUtilsTest {

    protected static final Logger logger = LoggerFactory.getLogger(MarshallingUtilsTest.class);

    /**
     * Check behavior of MarshallingUtils QName marshaller.
     * Please notice ootb JDK {@link QName#toString()} method does not satisfy requirement of managing qname in DMN as in typeRef="feel:number"
     * because toString would never print out the prefix part.
     * Additionally, for the purpose of marshalling the prefix information might be enough to serialize it back, as the JAXB object would also contain
     * a map of the applicable namespaces/prefix applicable in the hierarchy, see {@link DMNModelInstrumentedBase#getNsContext()}. 
     * 
     * This method checks the expected string serialization of QName and applicable roundtrip, considering the above, that is:
     * if the prefix is available, serialize it in the form: `prefix:localpart`
     * for the purpose of roundtrip, it is interesting to check if the roundtripped QName contains either a NS URI or a prefix, and if the case they
     * should correspond to the original.
     * 
     * @param qname object under test
     * @param formatQName expected string serialization form generated by {@link MarshallingUtils}
     */
    private void checkAndRoundTrip(QName qname, String formatQName) {
        String formatted = MarshallingUtils.formatQName(qname);
        assertThat(formatted).isEqualTo(formatQName);

        QName roundTrip = MarshallingUtils.parseQNameString(formatted);
        assertThat(roundTrip.getLocalPart()).isEqualTo(qname.getLocalPart());

        if (roundTrip.getPrefix() != XMLConstants.DEFAULT_NS_PREFIX) {
            assertThat(roundTrip.getPrefix()).isEqualTo(qname.getPrefix());
        }

        if (roundTrip.getNamespaceURI() != XMLConstants.NULL_NS_URI) {
            assertThat(roundTrip.getNamespaceURI()).isEqualTo(qname.getNamespaceURI());
        }
    }

    @Test 
    public void testLocal() throws Exception {
        QName qname = new QName("local1");
        checkAndRoundTrip(qname, "local1");
    }

    @Test
    public void testPrefixLocal() throws Exception {
        QName qname = new QName(XMLConstants.NULL_NS_URI, "local2", "prefix");
        checkAndRoundTrip(qname, "prefix:local2");
    }

    @Test
    public void testNamespaceLocal() throws Exception {
        QName qname = new QName("http://namespace", "local3");
        checkAndRoundTrip(qname, "{http://namespace}local3");
    }

    @Test
    public void testNamespaceLocal_b() throws Exception {
        QName qname = new QName("http://namespace", "local3", XMLConstants.DEFAULT_NS_PREFIX);
        checkAndRoundTrip(qname, "{http://namespace}local3");
    }

    @Test
    public void testNamespacePrefixLocal() throws Exception {
        QName qname = new QName("http://namespace", "local4", "prefix");
        checkAndRoundTrip(qname, "prefix:local4");
    }
}
