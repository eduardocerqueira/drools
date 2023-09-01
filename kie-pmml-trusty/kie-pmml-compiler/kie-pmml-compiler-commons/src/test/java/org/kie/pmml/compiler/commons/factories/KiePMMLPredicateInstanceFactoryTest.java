package org.kie.pmml.compiler.commons.factories;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.dmg.pmml.CompoundPredicate;
import org.dmg.pmml.DataField;
import org.dmg.pmml.False;
import org.dmg.pmml.Field;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.SimpleSetPredicate;
import org.dmg.pmml.True;
import org.junit.jupiter.api.Test;
import org.kie.pmml.commons.model.predicates.KiePMMLPredicate;

import static org.kie.pmml.compiler.api.testutils.PMMLModelTestUtils.getRandomCompoundPredicate;
import static org.kie.pmml.compiler.api.testutils.PMMLModelTestUtils.getRandomDataField;
import static org.kie.pmml.compiler.api.testutils.PMMLModelTestUtils.getRandomSimplePredicate;
import static org.kie.pmml.compiler.api.testutils.PMMLModelTestUtils.getRandomSimpleSetPredicate;
import static org.kie.pmml.compiler.commons.factories.InstanceFactoriesTestCommon.commonVerifyKiePMMLPredicate;

public class KiePMMLPredicateInstanceFactoryTest {

    @Test
    void getKiePMMLPredicate() {
        List<Field<?>> fields = IntStream.range(0, 3).mapToObj(i -> getRandomDataField()).collect(Collectors.toList());
        SimplePredicate simplePredicate1 = getRandomSimplePredicate((DataField) fields.get(0));
        KiePMMLPredicate retrieved = KiePMMLPredicateInstanceFactory.getKiePMMLPredicate(simplePredicate1, fields);
        commonVerifyKiePMMLPredicate(retrieved, simplePredicate1);

        SimpleSetPredicate simpleSetPredicate = getRandomSimpleSetPredicate((DataField) fields.get(2));
        retrieved = KiePMMLPredicateInstanceFactory.getKiePMMLPredicate(simpleSetPredicate, fields);
        commonVerifyKiePMMLPredicate(retrieved, simpleSetPredicate);

        final CompoundPredicate compoundPredicate = getRandomCompoundPredicate(fields);
        retrieved = KiePMMLPredicateInstanceFactory.getKiePMMLPredicate(compoundPredicate, fields);
        commonVerifyKiePMMLPredicate(retrieved, compoundPredicate);

        False falsePredicate = new False();
        retrieved = KiePMMLPredicateInstanceFactory.getKiePMMLPredicate(falsePredicate, fields);
        commonVerifyKiePMMLPredicate(retrieved, falsePredicate);

        True truePredicate = new True();
        retrieved = KiePMMLPredicateInstanceFactory.getKiePMMLPredicate(truePredicate, fields);
        commonVerifyKiePMMLPredicate(retrieved, truePredicate);
    }
}