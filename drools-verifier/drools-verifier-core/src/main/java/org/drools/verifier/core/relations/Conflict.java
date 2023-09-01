package org.drools.verifier.core.relations;

import org.drools.verifier.core.index.keys.UUIDKey;
import org.drools.verifier.core.maps.InspectorList;
import org.drools.verifier.core.maps.util.HasUUID;
import org.drools.verifier.core.util.PortablePreconditions;

public class Conflict
        extends Relation<Conflict> {

    public static Conflict EMPTY = new Conflict();

    private final HasUUID conflictedItem;
    private final HasUUID conflictingItem;

    public Conflict(final HasUUID conflictedItem,
                    final HasUUID conflictingItem) {
        super(null);
        this.conflictedItem = PortablePreconditions.checkNotNull("conflictedItem",
                                                                 conflictedItem);
        this.conflictingItem = PortablePreconditions.checkNotNull("conflictingItem",
                                                                  conflictingItem);
    }

    public Conflict(final InspectorList conflictedItem,
                    final InspectorList conflictingItem,
                    final Conflict origin) {
        super(PortablePreconditions.checkNotNull("origin",
                                                 origin));
        this.conflictedItem = PortablePreconditions.checkNotNull("conflictedItem",
                                                                 conflictedItem);
        this.conflictingItem = PortablePreconditions.checkNotNull("conflictingItem",
                                                                  conflictingItem);
    }

    private Conflict() {
        super(null);
        this.conflictedItem = null;
        this.conflictingItem = null;
    }

    @Override
    public boolean foundIssue() {
        return conflictingItem != null;
    }

    public HasUUID getConflictedItem() {
        return conflictedItem;
    }

    public HasUUID getConflictingItem() {
        return conflictingItem;
    }

    @Override
    public UUIDKey otherUUID() {
        return conflictingItem.getUuidKey();
    }

    @Override
    public boolean doesRelationStillExist() {
        if (origin != null
                && stillContainsConflictedItem(getOrigin().getConflictedItem())
                && stillContainsConflictingItem(getOrigin().getConflictingItem())) {

            return ConflictResolver.isConflicting(getOrigin().getConflictedItem(),
                                                  getOrigin().getConflictingItem()).foundIssue();
        } else {
            return false;
        }
    }

    private boolean stillContainsConflictedItem(final HasUUID item) {
        if (this.conflictedItem instanceof InspectorList) {
            return ((InspectorList) this.conflictedItem).contains(item);
        } else {
            if (parent != null) {
                return parent.stillContainsConflictedItem(item);
            } else {
                return false;
            }
        }
    }

    private boolean stillContainsConflictingItem(final HasUUID item) {
        if (this.conflictingItem instanceof InspectorList) {
            return ((InspectorList) this.conflictingItem).contains(item);
        } else {
            if (parent != null) {
                return parent.stillContainsConflictingItem(item);
            } else {
                return false;
            }
        }
    }
}
