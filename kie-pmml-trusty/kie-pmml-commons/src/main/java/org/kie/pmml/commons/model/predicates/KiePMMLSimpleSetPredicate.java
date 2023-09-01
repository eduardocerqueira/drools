package org.kie.pmml.commons.model.predicates;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import org.kie.pmml.api.exceptions.KiePMMLException;
import org.kie.pmml.commons.model.KiePMMLExtension;
import org.kie.pmml.api.enums.ARRAY_TYPE;
import org.kie.pmml.api.enums.IN_NOTIN;
import org.kie.pmml.api.utils.ConverterTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see <a href=http://dmg.org/pmml/v4-4/TreeModel.html#xsdElement_SimpleSetPredicate>SimpleSetPredicate</a>
 */
public class KiePMMLSimpleSetPredicate extends KiePMMLPredicate {

    private static final Logger logger = LoggerFactory.getLogger(KiePMMLSimpleSetPredicate.class);

    private final ARRAY_TYPE arrayType;
    private final IN_NOTIN inNotIn;
    protected List<Object> values;

    protected KiePMMLSimpleSetPredicate(final String name, final List<KiePMMLExtension> extensions, final ARRAY_TYPE arrayType, final IN_NOTIN inNotIn) {
        super(name, extensions);
        this.arrayType = arrayType;
        this.inNotIn = inNotIn;
    }

    /**
     * Builder to auto-generate the <b>id</b>
     * @return
     */
    public static Builder builder(final String name, final List<KiePMMLExtension> extensions, final ARRAY_TYPE arrayType, final IN_NOTIN inNotIn) {
        return new Builder(name, extensions, arrayType, inNotIn);
    }

    @Override
    public boolean evaluate(Map<String, Object> values) {
        boolean toReturn = false;
        if (values.containsKey(name)) {
            logger.debug("found matching parameter, evaluating... ");
            toReturn = evaluation(values.get(name));
        }
        return toReturn;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Object> getValues() {
        return values;
    }

    public ARRAY_TYPE getArrayType() {
        return arrayType;
    }

    public IN_NOTIN getInNotIn() {
        return inNotIn;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", KiePMMLSimpleSetPredicate.class.getSimpleName() + "[", "]")
                .add("values=" + values)
                .add("inNotIn=" + inNotIn)
                .add("name='" + name + "'")
                .add("extensions=" + extensions)
                .add("id='" + id + "'")
                .add("parentId='" + parentId + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KiePMMLSimpleSetPredicate that = (KiePMMLSimpleSetPredicate) o;
        return Objects.equals(values, that.values) &&
                inNotIn == that.inNotIn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, inNotIn);
    }

    protected boolean evaluation(Object rawValue) {
        String stringValue = (String) ConverterTypeUtil.convert(String.class, rawValue);
        Object convertedValue = arrayType.getValue(stringValue);
        switch (inNotIn) {
            case IN:
                return values.contains(convertedValue);
            case NOT_IN:
                return !values.contains(convertedValue);
            default:
                throw new KiePMMLException("Unknown IN_NOTIN" + inNotIn);
        }
    }

    public static class Builder extends KiePMMLPredicate.Builder<KiePMMLSimpleSetPredicate> {

        private Builder(final String name, final List<KiePMMLExtension> extensions, final ARRAY_TYPE arrayType, final IN_NOTIN inNotIn) {
            super("SimpleSetPredicate-", () -> new KiePMMLSimpleSetPredicate(name, extensions, arrayType, inNotIn));
        }

        public KiePMMLSimpleSetPredicate.Builder withValues(List<Object> values) {
            toBuild.values = Collections.unmodifiableList(values);
            return this;
        }


    }
}
