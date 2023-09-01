package org.kie.pmml.compiler.commons.factories;

import java.util.List;

import org.dmg.pmml.DataType;
import org.dmg.pmml.Field;
import org.dmg.pmml.SimplePredicate;
import org.kie.pmml.api.enums.DATA_TYPE;
import org.kie.pmml.api.enums.OPERATOR;
import org.kie.pmml.commons.model.KiePMMLExtension;
import org.kie.pmml.commons.model.predicates.KiePMMLSimplePredicate;

import static org.kie.pmml.compiler.api.utils.ModelUtils.getDataType;
import static org.kie.pmml.compiler.commons.factories.KiePMMLExtensionInstanceFactory.getKiePMMLExtensions;

/**
 * Class meant to provide <i>helper</i> method to retrieve <code>KiePMMLSimplePredicate</code> instance
 * out of <code>SimplePredicate</code>s
 */
public class KiePMMLSimplePredicateInstanceFactory {

    private KiePMMLSimplePredicateInstanceFactory() {
        // Avoid instantiation
    }

    static KiePMMLSimplePredicate getKiePMMLSimplePredicate(final SimplePredicate simplePredicate,
                                                            final List<Field<?>> fields) {
        final List<KiePMMLExtension> kiePMMLExtensions = getKiePMMLExtensions(simplePredicate.getExtensions());
        DataType dataType = getDataType(fields, simplePredicate.getField().getValue());
        Object value = DATA_TYPE.byName(dataType.value()).getActualValue(simplePredicate.getValue());
        return KiePMMLSimplePredicate.builder(simplePredicate.getField().getValue(),
                                              kiePMMLExtensions,
                                              OPERATOR.byName(simplePredicate.getOperator().value()))
                .withValue(value)
                .build();
    }
}
