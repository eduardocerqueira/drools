package org.kie.dmn.model.v1_4;

import java.util.ArrayList;
import java.util.List;

import org.kie.dmn.model.api.Context;
import org.kie.dmn.model.api.ContextEntry;

public class TContext extends TExpression implements Context {

    protected List<ContextEntry> contextEntry;

    @Override
    public List<ContextEntry> getContextEntry() {
        if (contextEntry == null) {
            contextEntry = new ArrayList<>();
        }
        return this.contextEntry;
    }

}
