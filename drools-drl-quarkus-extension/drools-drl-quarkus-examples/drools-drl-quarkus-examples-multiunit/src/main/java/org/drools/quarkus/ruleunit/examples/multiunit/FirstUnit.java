package org.drools.quarkus.ruleunit.examples.multiunit;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStream;
import org.drools.ruleunits.api.RuleUnitData;

public class FirstUnit implements RuleUnitData {

    private final DataStream<RuleInput> input = DataSource.createStream();
    private final DataStream<RuleOutput1> output = DataSource.createStream();

    public DataStream<RuleInput> getInput() {
        return input;
    }

    public DataStream<RuleOutput1> getOutput() {
        return output;
    }
}
