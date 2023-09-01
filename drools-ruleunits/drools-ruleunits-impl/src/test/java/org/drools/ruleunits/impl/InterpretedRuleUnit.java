package org.drools.ruleunits.impl;

import org.drools.compiler.kie.builder.impl.BuildContext;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieModuleKieProject;
import org.drools.compiler.kproject.models.KieBaseModelImpl;
import org.drools.core.common.ReteEvaluator;
import org.drools.core.impl.InternalRuleBase;
import org.drools.ruleunits.api.RuleUnitData;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.conf.RuleConfig;
import org.drools.ruleunits.impl.factory.AbstractRuleUnit;
import org.drools.ruleunits.impl.sessions.RuleUnitExecutorImpl;
import org.kie.api.builder.Message;

import static org.drools.compiler.kproject.models.KieBaseModelImpl.defaultKieBaseModel;
import static org.drools.ruleunits.impl.RuleUnitProviderImpl.createRuleUnitKieModule;
import static org.drools.ruleunits.impl.RuleUnitProviderImpl.createRuleUnitKieProject;

/**
 * A fully-runtime, reflective implementation of a rule unit, useful for testing
 */
public class InterpretedRuleUnit<T extends RuleUnitData> extends AbstractRuleUnit<T> {

    public static <T extends RuleUnitData> RuleUnitInstance<T> instance(T ruleUnitData) {
        InterpretedRuleUnit<T> interpretedRuleUnit = new InterpretedRuleUnit<>((Class<T>) ruleUnitData.getClass());
        return interpretedRuleUnit.createInstance(ruleUnitData);
    }

    public static <T extends RuleUnitData> RuleUnitInstance<T> instance(T ruleUnitData, RuleConfig ruleConfig) {
        InterpretedRuleUnit<T> interpretedRuleUnit = new InterpretedRuleUnit<>((Class<T>) ruleUnitData.getClass());
        return interpretedRuleUnit.createInstance(ruleUnitData, ruleConfig);
    }

    private InterpretedRuleUnit(Class<T> ruleUnitDataClass) {
        super(ruleUnitDataClass);
    }

    @Override
    public RuleUnitInstance<T> internalCreateInstance(T data, RuleConfig ruleConfig) {
        InternalRuleBase ruleBase = createRuleBase(data);
        ReteEvaluator reteEvaluator = new RuleUnitExecutorImpl(ruleBase);
        return new InterpretedRuleUnitInstance<>(this, data, reteEvaluator, ruleConfig);
    }

    private InternalRuleBase createRuleBase(T data) {
        InternalKieModule kieModule = createRuleUnitKieModule(data.getClass(), false);
        KieModuleKieProject kieProject = createRuleUnitKieProject(kieModule, false);

        BuildContext buildContext = new BuildContext();
        InternalRuleBase kBase = kieModule.createKieBase((KieBaseModelImpl) defaultKieBaseModel(), kieProject, buildContext, null);
        if (kBase == null) {
            // build error, throw runtime exception
            throw new RuntimeException("Error while creating KieBase" + buildContext.getMessages().filterMessages(Message.Level.ERROR));
        }
        return kBase;
    }
}
