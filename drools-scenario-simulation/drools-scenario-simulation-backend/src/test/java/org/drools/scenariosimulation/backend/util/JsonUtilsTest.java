package org.drools.scenariosimulation.backend.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.drools.scenariosimulation.backend.util.JsonUtils.convertFromStringToJSONNode;

public class JsonUtilsTest {

    @Test
    public void convertFromStringToJSONNode_manyCases() {
        assertThat(convertFromStringToJSONNode(null)).isNotPresent();
        assertThat(convertFromStringToJSONNode("Not json")).isNotPresent();
        assertThat(convertFromStringToJSONNode("\"Not json")).isNotPresent();
        assertThat(convertFromStringToJSONNode("key : notJson\"")).isNotPresent();
        assertThat(convertFromStringToJSONNode("[key : 100]")).isNotPresent();
        assertThat(convertFromStringToJSONNode("{\"key\" : 100{")).isNotPresent();
        assertThat(convertFromStringToJSONNode("{key : 100}")).isNotPresent();
        assertThat(convertFromStringToJSONNode("\"Json\"")).isPresent();
        assertThat(convertFromStringToJSONNode("\"key : Json\"")).isPresent();
        assertThat(convertFromStringToJSONNode("{ \"id\": 2, \"username\": \"user\", \"num\": 12, \"name\": \"Mr Yellow\"\n }")).isPresent();
        assertThat(convertFromStringToJSONNode("{ \"users\": [\n" +
                "\t\t{ \"id\": 3, \"username\": \"user45\", \"num\": 24, \"name\": \"Mr White\" },\n" +
                "\t\t{ \"id\": 4, \"username\": \"user65\", \"num\": 32, \"name\": \"Mr Red\" }\n" +
                "\t]}")).isPresent();
        assertThat(convertFromStringToJSONNode("[{\"name\": \"\\\"John\\\"\"}, " +
                "{\"name\": \"\\\"John\\\"\", \"names\" : [{\"value\": \"\\\"Anna\\\"\"}, {\"value\": \"\\\"Mario\\\"\"}]}]")).isPresent();
        assertThat(convertFromStringToJSONNode("[1,2,3]")).isPresent();
        assertThat(convertFromStringToJSONNode("{\"id\": 23, \"num\": 34, \"time\" : 56}")).isPresent();
        assertThat(convertFromStringToJSONNode("{\"married\":true, \"num\":34, \"name\": \"john\"}")).as("Combine three data types in object").isPresent();
        assertThat(convertFromStringToJSONNode("[{\"married\":true,\"num\":34,\"name\":\"john\"}," +
                "{\"married\":false,\"num\":43,\"name\":\"jane\"}]")).as("Combine three data types in array").isPresent();
        assertThat(convertFromStringToJSONNode("{\"is married\":\"yes, is\"}")).as("Whitespaces").isPresent();
    }
}
