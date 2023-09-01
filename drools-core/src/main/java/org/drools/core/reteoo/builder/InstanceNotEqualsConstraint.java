package org.drools.core.reteoo.builder;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.base.base.ValueResolver;
import org.drools.base.reteoo.BaseTuple;
import org.drools.base.rule.ContextEntry;
import org.drools.base.rule.Declaration;
import org.drools.base.rule.Pattern;
import org.drools.base.rule.constraint.BetaNodeFieldConstraint;
import org.kie.api.runtime.rule.FactHandle;

public class InstanceNotEqualsConstraint
    implements
    BetaNodeFieldConstraint {

    private static final long          serialVersionUID = 510l;

    private Declaration[] declarations     = new Declaration[0];

    private Pattern                     otherPattern;

    public InstanceNotEqualsConstraint() {

    }

    public InstanceNotEqualsConstraint(final Pattern otherPattern) {
        this.otherPattern = otherPattern;

    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        declarations    = (Declaration[])in.readObject();
        otherPattern    = (Pattern)in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(declarations);
        out.writeObject(otherPattern);
    }

    public Declaration[] getRequiredDeclarations() {
        return declarations;
    }

    public void replaceDeclaration(Declaration oldDecl,
                                   Declaration newDecl) {
    }

    public Pattern getOtherPattern() {
        return this.otherPattern;
    }

    public boolean isTemporal() {
        return false;
    }

    public ContextEntry createContextEntry() {
        return new InstanceNotEqualsConstraintContextEntry( this.otherPattern );
    }

    public boolean isAllowed(final ContextEntry entry) {
        final InstanceNotEqualsConstraintContextEntry context = (InstanceNotEqualsConstraintContextEntry) entry;
        return context.left != context.right;
    }

    public boolean isAllowedCachedLeft(final ContextEntry context,
                                       final FactHandle handle) {
        return ((InstanceNotEqualsConstraintContextEntry) context).left != handle.getObject();
    }

    public boolean isAllowedCachedRight(final BaseTuple tuple,
                                        final ContextEntry context) {
        return tuple.getObject( this.otherPattern.getTupleIndex()) != ((InstanceNotEqualsConstraintContextEntry) context).right;
    }

    public String toString() {
        return "[InstanceEqualsConstraint otherPattern=" + this.otherPattern + " ]";
    }

    public int hashCode() {
        return this.otherPattern.hashCode();
    }

    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }

        if (!(object instanceof InstanceNotEqualsConstraint)) {
            return false;
        }

        final InstanceNotEqualsConstraint other = (InstanceNotEqualsConstraint) object;
        return this.otherPattern.equals( other.otherPattern );
    }

    public InstanceNotEqualsConstraint clone() {
        return new InstanceNotEqualsConstraint( this.otherPattern );
    }

    public ConstraintType getType() {
        return ConstraintType.BETA;
    }

    public static class InstanceNotEqualsConstraintContextEntry
        implements
        ContextEntry {

        private static final long serialVersionUID = 510l;
        public Object             left;
        public Object             right;

        private Pattern            pattern;
        private ContextEntry      entry;

        public InstanceNotEqualsConstraintContextEntry() {
        }

        public InstanceNotEqualsConstraintContextEntry(final Pattern pattern) {
            this.pattern = pattern;
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            left    = in.readObject();
            right   = in.readObject();
            pattern = (Pattern)in.readObject();
            entry   = (ContextEntry)in.readObject();
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(left);
            out.writeObject(right);
            out.writeObject(pattern);
            out.writeObject(entry);
        }

        public ContextEntry getNext() {
            return this.entry;
        }

        public void setNext(final ContextEntry entry) {
            this.entry = entry;
        }

        public void updateFromTuple(final ValueResolver valueResolver,
                                    final BaseTuple tuple) {
            this.left = tuple.getObject( this.pattern.getTupleIndex());
        }

        public void updateFromFactHandle(final ValueResolver valueResolver,
                                         final FactHandle handle) {
            this.right = handle.getObject();
        }

        public void resetTuple() {
            this.left = null;
        }

        public void resetFactHandle() {
            this.right = null;
        }
    }

    public BetaNodeFieldConstraint cloneIfInUse() {
        return this;
    }
}
