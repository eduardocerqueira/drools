package org.drools.model.impl;

import org.drools.model.Variable;

import static org.drools.model.impl.NamesGenerator.generateName;

public abstract class VariableImpl<T> implements Variable<T>, ModelComponent {
    public static final String GENERATED_VARIABLE_PREFIX = "GENERATED_";

    private final Class<T> type;
    private final String name;

    public VariableImpl(Class<T> type) {
        this(type, generateName("var"));
    }

    public VariableImpl(Class<T> type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Variable " + name + " of type " + type;
    }

    @Override
    public boolean isEqualTo( ModelComponent o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        Variable var = ( Variable ) o;
        return type.getName().equals( var.getType().getName() );
    }
}
