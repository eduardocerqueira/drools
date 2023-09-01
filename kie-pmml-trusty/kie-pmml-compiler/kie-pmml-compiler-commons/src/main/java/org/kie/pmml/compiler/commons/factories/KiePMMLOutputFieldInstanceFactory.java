package org.kie.pmml.compiler.commons.factories;

import java.util.Collections;

import org.dmg.pmml.OutputField;
import org.kie.pmml.api.enums.DATA_TYPE;
import org.kie.pmml.api.enums.RESULT_FEATURE;
import org.kie.pmml.commons.model.KiePMMLOutputField;
import org.kie.pmml.commons.model.expressions.KiePMMLExpression;

import static org.kie.pmml.compiler.commons.factories.KiePMMLExpressionInstanceFactory.getKiePMMLExpression;

/**
 * Class meant to provide <i>helper</i> method to retrieve <code>KiePMMLOutputField</code> instance
 * out of <code>OutputField</code>
 */
public class KiePMMLOutputFieldInstanceFactory {

    private KiePMMLOutputFieldInstanceFactory() {
    }

    public static KiePMMLOutputField getKiePMMLOutputField(final OutputField outputField) {
        String name = outputField.getName() != null ? outputField.getName().getValue() : "" + outputField.hashCode();
        final String targetField = outputField.getTargetField() != null ? outputField.getTargetField().getValue() :
                null;
        final RESULT_FEATURE resultFeature = outputField.getResultFeature() != null ?
                RESULT_FEATURE.byName(outputField.getResultFeature().value()) : null;
        final DATA_TYPE dataType = outputField.getDataType() != null ?
                DATA_TYPE.byName(outputField.getDataType().value()) : null;
        final KiePMMLExpression kiePMMLExpression = outputField.getExpression() != null ?
                getKiePMMLExpression(outputField.getExpression()) : null;
        final KiePMMLOutputField.Builder builder = KiePMMLOutputField.builder(name, Collections.emptyList())
                .withResultFeature(resultFeature)
                .withTargetField(targetField)
                .withValue(outputField.getValue())
                .withDataType(dataType)
                .withRank(outputField.getRank())
                .withKiePMMLExpression(kiePMMLExpression);
        return builder.build();
    }
}
