package org.kie.api.fluent;

import org.slf4j.LoggerFactory;

/**
 * Holds an utility method to retrieve <code>ProcessBuilderFactory</code> default instance.<br>
 * Default instance returned by <code>get</code> method can be modified by setting property "org.jbpm.processBuilder.factoryClass"<br>
 * Value should be full class name of a <code>ProcessBuilderFactory</code> implementation that contains a default public constructor
 */
public class ProcessBuilderFactories {

    private ProcessBuilderFactories() {}

    private static ProcessBuilderFactory instance;

    public static synchronized ProcessBuilderFactory get() {
        if (instance == null) {
            try {
                instance =
                        Class.forName(System.getProperty("org.jbpm.processBuilder.factoryClass",
                                                         "org.jbpm.ruleflow.core.RuleFlowProcessFactoryBuilder"))
                             .asSubclass(ProcessBuilderFactory.class)
                             .newInstance();
            } catch (ReflectiveOperationException e) {
                LoggerFactory.getLogger(ProcessBuilderFactories.class).error("Unable to instance ProcessBuilderFactory", e);
                throw new IllegalStateException(e);
            }
        }
        return instance;
    }
}
