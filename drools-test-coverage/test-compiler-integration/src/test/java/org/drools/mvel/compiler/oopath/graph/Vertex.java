package org.drools.mvel.compiler.oopath.graph;

import org.drools.core.phreak.AbstractReactiveObject;
import org.drools.core.phreak.ReactiveList;

import java.util.List;

public class Vertex<T> extends AbstractReactiveObject {

    private static int ID_GENERATOR = 1;

    private final int id;
    private final T it;

    private final List<Edge> inEs = new ReactiveList<Edge>();
    private final List<Edge> outEs = new ReactiveList<Edge>();

    private final List<Vertex<?>> inVs = new ReactiveList<Vertex<?>>();
    private final List<Vertex<?>> outVs = new ReactiveList<Vertex<?>>();

    public Vertex( T content ) {
        this(ID_GENERATOR++, content);
    }

    public Vertex( int id, T it ) {
        this.id = id;
        this.it = it;
    }

    public int getId() {
        return id;
    }

    public T getIt() {
        return it;
    }

    public List<Edge> getInEs() {
        return inEs;
    }

    public List<Vertex<?>> getInVs() {
        return inVs;
    }

    public List<Edge> getOutEs() {
        return outEs;
    }

    public List<Vertex<?>> getOutVs() {
        return outVs;
    }

    public void addInEdge(Edge edge) {
        inEs.add( edge );
        edge.setOutV( this );
    }

    public void addOutEdge(Edge edge) {
        outEs.add( edge );
        edge.setInV( this );
    }

    public Edge connectTo(Vertex<?> other) {
        Edge edge = new Edge();
        this.addOutEdge( edge );
        other.addInEdge( edge );

        outVs.add(other);
        other.inVs.add(this);

        return edge;
    }
}
