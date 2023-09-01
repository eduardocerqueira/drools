package org.drools.verifier.core.checks.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.drools.verifier.core.cache.inspectors.RuleInspector;
import org.drools.verifier.core.checks.DetectConflictingRowsCheck;
import org.drools.verifier.core.checks.DetectDeficientRowsCheck;
import org.drools.verifier.core.checks.DetectEmptyRowCheck;
import org.drools.verifier.core.checks.DetectImpossibleMatchCheck;
import org.drools.verifier.core.checks.DetectMissingActionCheck;
import org.drools.verifier.core.checks.DetectMissingConditionCheck;
import org.drools.verifier.core.checks.DetectMultipleValuesForOneActionCheck;
import org.drools.verifier.core.checks.DetectRedundantActionFactFieldCheck;
import org.drools.verifier.core.checks.DetectRedundantActionValueCheck;
import org.drools.verifier.core.checks.DetectRedundantConditionsCheck;
import org.drools.verifier.core.checks.DetectRedundantRowsCheck;
import org.drools.verifier.core.checks.SingleHitCheck;
import org.drools.verifier.core.configuration.AnalyzerConfiguration;
import org.drools.verifier.core.configuration.CheckConfiguration;
import org.drools.verifier.core.util.PortablePreconditions;

/**
 * Creates checks. Uses a white list to make sure the only the checks the user wants are used.
 */
public class CheckFactory {

    private final CheckConfiguration checkConfiguration;
    private final AnalyzerConfiguration configuration;

    public CheckFactory(final AnalyzerConfiguration configuration) {
        this.configuration = PortablePreconditions.checkNotNull("configuration",
                                                                configuration);
        checkConfiguration = PortablePreconditions.checkNotNull("checkWhiteList",
                                                                configuration.getCheckConfiguration());
    }

    protected Set<Check> makeSingleChecks(final RuleInspector ruleInspector) {
        return new HashSet<>(filter(new DetectImpossibleMatchCheck(ruleInspector,
                                                                   configuration),
                                    new DetectMultipleValuesForOneActionCheck(ruleInspector,
                                                                              configuration),
                                    new DetectEmptyRowCheck(ruleInspector,
                                                            configuration),
                                    new DetectMissingActionCheck(ruleInspector,
                                                                 configuration),
                                    new DetectMissingConditionCheck(ruleInspector,
                                                                    configuration),
                                    new DetectDeficientRowsCheck(ruleInspector,
                                                                 configuration),
                                    new DetectRedundantActionFactFieldCheck(ruleInspector,
                                                                            configuration),
                                    new DetectRedundantActionValueCheck(ruleInspector,
                                                                        configuration),
                                    new DetectRedundantConditionsCheck(ruleInspector,
                                                                       configuration)));
    }

    protected Optional<PairCheckBundle> makePairRowCheck(final RuleInspector ruleInspector,
                                                         final RuleInspector other) {

        final List<Check> filteredSet = filter(new DetectConflictingRowsCheck(ruleInspector,
                                                                              other,
                                                                              configuration),
                                               new DetectRedundantRowsCheck(ruleInspector,
                                                                            other,
                                                                            configuration),
                                               new SingleHitCheck(ruleInspector,
                                                                  other,
                                                                  configuration));

        if (filteredSet.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new PairCheckBundle(ruleInspector,
                                                   other,
                                                   filteredSet));
        }
    }

    private List<Check> filter(final Check... checks) {
        final ArrayList<Check> checkHashSet = new ArrayList<>();

        for (final Check check : checks) {
            if (check.isActive(checkConfiguration)) {
                checkHashSet.add(check);
            }
        }

        return checkHashSet;
    }
}
