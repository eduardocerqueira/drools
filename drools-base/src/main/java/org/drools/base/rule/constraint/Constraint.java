package org.drools.base.rule.constraint;

import java.io.Externalizable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.drools.base.rule.Pattern;
import org.drools.base.rule.RuleComponent;
import org.drools.base.rule.Declaration;
import org.drools.base.base.ObjectType;
import org.drools.core.util.bitmask.AllSetButLastBitMask;
import org.drools.core.util.bitmask.BitMask;
import org.drools.base.RuleBase;
import org.drools.base.RuleBuildContext;

//import static org.drools.core.reteoo.PropertySpecificUtil.allSetButTraitBitMask;

public interface Constraint
    extends
        RuleComponent,
    Externalizable,
    Cloneable {

    /**
     * Returns all the declarations required by the given
     * constraint implementation.
     *
     * @return
     */
    Declaration[] getRequiredDeclarations();

    /**
     * When a rule contains multiple logical branches, i.e., makes 
     * use of 'OR' CE, it is required to clone patterns and declarations
     * for each logical branch. Since this is done at ReteOO build
     * type, when constraints were already created, eventually
     * some constraints need to update their references to the
     * declarations.
     *
     * @param oldDecl
     * @param newDecl
     */
    void replaceDeclaration(Declaration oldDecl,
                            Declaration newDecl);

    /**
     * Clones the constraint
     * @return
     */
    Constraint clone();

    /**
     * Returns the type of the constraint, either ALPHA, BETA or UNKNOWN
     *
     * @return
     */
    ConstraintType getType();
    
    /**
     * Returns true in case this constraint is a temporal constraint
     * 
     * @return
     */
    boolean isTemporal();

    default BitMask getListenedPropertyMask(ObjectType objectType, List<String> settableProperties ) {
        return getListenedPropertyMask(Optional.empty(), objectType, settableProperties);
    }

    /**
     * Returns property reactivity BitMask of this constraint.
     *
     * @param pattern which this constraint belongs to. if pattern is empty, bind variables are considered to be declared in the same pattern. It should be fine for alpha constraints
     * @param objectType
     * @param settableProperties
     * @return property reactivity BitMask
     */
    default BitMask getListenedPropertyMask(Optional<Pattern> pattern, ObjectType objectType, List<String> settableProperties ) {
        return AllSetButLastBitMask.get();
    }

    default boolean equals(Object object, RuleBase kbase) {
        return this.equals( object );
    }

    default void registerEvaluationContext(RuleBuildContext ruleBuildContext) { }

    default void mergeEvaluationContext(Constraint other) { }

    default Collection<String> getPackageNames() {
        return Collections.emptyList();
    }
    default void addPackageNames(Collection<String> otherPkgs) { }

    /**
     * An enum for Constraint Types
     */
    public static enum ConstraintType {

        UNKNOWN("UNKNOWN"),
        ALPHA("ALPHA"),
        BETA("BETA"),
        XPATH("XPATH");

        private String desc;

        private ConstraintType( String desc ) {
            this.desc = desc;
        }

        public String toString() {
            return "ConstraintType::"+this.desc;
        }
    }
    
}
