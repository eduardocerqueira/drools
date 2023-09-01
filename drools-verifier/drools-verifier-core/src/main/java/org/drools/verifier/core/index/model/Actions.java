package org.drools.verifier.core.index.model;

import java.util.Collection;

import org.drools.verifier.core.index.matchers.ExactMatcher;
import org.drools.verifier.core.index.matchers.KeyMatcher;
import org.drools.verifier.core.index.matchers.Matcher;
import org.drools.verifier.core.index.query.MapBy;
import org.drools.verifier.core.index.query.Where;
import org.drools.verifier.core.index.select.Listen;
import org.drools.verifier.core.index.select.Select;
import org.drools.verifier.core.maps.KeyTreeMap;

public class Actions {

    private final KeyTreeMap<Action> map = new KeyTreeMap<>(Action.keyDefinitions());

    public Actions() {

    }

    public Actions(final Collection<Action> actions) {
        for (final Action action : actions) {
            add(action);
        }
    }

    public void add(final Action action) {

        map.put(action);
    }

    public void merge(final Actions actions) {
        map.merge(actions.map);
    }

    public Where<ActionSelect, ActionListen> where(final Matcher matcher) {
        return new Where<ActionSelect, ActionListen>() {
            @Override
            public ActionSelect select() {
                return new ActionSelect(matcher);
            }

            @Override
            public ActionListen listen() {
                return new ActionListen(matcher);
            }
        };
    }

    public <KeyType> MapBy<KeyType, Action> mapBy(final KeyMatcher matcher) {
        return new MapBy<>(map.get(matcher.getKeyDefinition()));
    }

    public void remove(final Column column) {
        final ExactMatcher matcher = Action.columnUUID().is(column.getUuidKey());
        for (final Action action : where(matcher).select().all()) {
            action.getUuidKey().retract();
        }
    }

    public class ActionSelect
            extends Select<Action> {

        public ActionSelect(final Matcher matcher) {
            super(map.get(matcher.getKeyDefinition()),
                  matcher);
        }
    }

    public class ActionListen
            extends Listen<Action> {

        public ActionListen(final Matcher matcher) {
            super(map.get(matcher.getKeyDefinition()),
                  matcher);
        }
    }
}
