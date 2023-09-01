package org.drools.verifier.core.checks.base;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.drools.verifier.core.cache.inspectors.RuleInspector;
import org.drools.verifier.core.maps.MultiSet;

public class PairCheckStorage {

    private final MultiSet<RuleInspector, PairCheckBundle> pairChecks = new MultiSet<>();
    private final Map<RuleInspector, MultiSet<RuleInspector, PairCheckBundle>> pairChecksByOtherRowInspector = new HashMap<>();

    public void add(final PairCheckBundle pairCheckList) {

        pairChecks.put(pairCheckList.getRuleInspector(),
                       pairCheckList);

        addByOther(pairCheckList);
    }

    private void addByOther(final PairCheckBundle pairCheckList) {
        final MultiSet<RuleInspector, PairCheckBundle> multiSet = getByOther(pairCheckList.getOther());
        final Collection<PairCheckBundle> collection = multiSet.get(pairCheckList.getRuleInspector());

        if (collection == null) {
            multiSet.put(pairCheckList.getRuleInspector(),
                         pairCheckList);
        } else {
            collection.add(pairCheckList);
        }
    }

    private MultiSet<RuleInspector, PairCheckBundle> getByOther(final RuleInspector other) {
        final MultiSet<RuleInspector, PairCheckBundle> multiSet = pairChecksByOtherRowInspector.get(other);

        if (multiSet == null) {
            final MultiSet<RuleInspector, PairCheckBundle> result = new MultiSet<>();
            pairChecksByOtherRowInspector.put(other,
                                              result);
            return result;
        } else {
            return multiSet;
        }
    }

    public Collection<PairCheckBundle> remove(final RuleInspector ruleInspector) {
        final HashSet<PairCheckBundle> result = new HashSet<>();

        final Collection<PairCheckBundle> removedPairCheckLists = pairChecks.remove(ruleInspector);

        if (removedPairCheckLists != null) {
            result.addAll(removedPairCheckLists);
        }

        result.addAll(removeByOther(ruleInspector));

        return result;
    }

    private List<PairCheckBundle> removeByOther(final RuleInspector ruleInspector) {
        final MultiSet<RuleInspector, PairCheckBundle> removedMap = pairChecksByOtherRowInspector.remove(ruleInspector);

        if (removedMap != null) {
            for (final RuleInspector inspector : removedMap.keys()) {
                final Collection<PairCheckBundle> collection = removedMap.get(inspector);
                pairChecks.get(inspector).removeAll(collection);
                getByOther(inspector).remove(ruleInspector);
            }
            return removedMap.allValues();
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Collection<PairCheckBundle> get(final RuleInspector ruleInspector) {
        final Collection<PairCheckBundle> pairCheckLists = this.pairChecks.get(ruleInspector);
        final MultiSet<RuleInspector, PairCheckBundle> multiSet = getByOther(ruleInspector);

        final HashSet<PairCheckBundle> result = new HashSet<>();
        if (pairCheckLists != null) {
            result.addAll(pairCheckLists);
        }
        result.addAll(multiSet.allValues());
        return result;
    }

    public void remove(final Collection<PairCheckBundle> checks) {
        for (final PairCheckBundle check : checks) {
            get(check.getOther()).remove(check);
        }
    }
}
