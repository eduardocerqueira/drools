package org.drools.mvel.builder;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.drools.compiler.compiler.AnalysisResult;
import org.drools.compiler.compiler.BoundIdentifiers;

/**
 * An analysis result implementation for the MVEL dialect
 */
public class MVELAnalysisResult
    implements
    AnalysisResult {

    private BoundIdentifiers        boundIdentifiers      = null;
    private Set<String>             identifiers           = Collections.emptySet();
    private Set<String>             localVariables        = Collections.emptySet();
    private Set<String>             notBoundedIdentifiers = Collections.emptySet();

    private Map<String, Class< ? >> mvelVariables;

    private boolean                 typesafe              = true;

    private Class<?>                returnType;

    public BoundIdentifiers getBoundIdentifiers() {
        return boundIdentifiers;
    }

    public void setBoundIdentifiers(BoundIdentifiers boundIdentifiers) {
        this.boundIdentifiers = boundIdentifiers;

    }

    public Set<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set<String> identifiers) {
        this.identifiers = identifiers;
    }

    public Set<String> getLocalVariables() {
        return this.localVariables;
    }

    public Set<String> getNotBoundedIdentifiers() {
        return notBoundedIdentifiers;
    }

    public void setNotBoundedIdentifiers(Set<String> notBoundedIdentifiers) {
        this.notBoundedIdentifiers = notBoundedIdentifiers;
    }

    public Map<String, Class< ? >> getMvelVariables() {
        return mvelVariables;
    }

    public void setMvelVariables(Map<String, Class< ? >> mvelVariables) {
        this.mvelVariables = mvelVariables;
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public boolean isTypesafe() {
        return typesafe;
    }

    public void setTypesafe(boolean typesafe) {
        this.typesafe = typesafe;
    }

    @Override
    public String toString() {
        return "MVELAnalysisResult [boundIdentifiers=" + boundIdentifiers +
               ",\n identifiers=" + identifiers +
               ",\n localVariables=" + localVariables +
               ",\n notBoundedIdentifiers=" + notBoundedIdentifiers +
               ",\n mvelVariables=" + mvelVariables +
               ",\n returnType=" + returnType + "]";
    }

}
