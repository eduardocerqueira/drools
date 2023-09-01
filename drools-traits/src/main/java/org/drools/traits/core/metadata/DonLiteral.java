package org.drools.traits.core.metadata;

import java.net.URI;

import org.drools.traits.core.factmodel.AbstractTraitFactory;
import org.drools.traits.core.factmodel.LogicalTypeInconsistencyException;
import org.drools.base.factmodel.traits.Thing;
import org.drools.base.factmodel.traits.TraitableBean;
import org.drools.traits.core.util.StandaloneTraitFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DonLiteral<K, T extends Metadatable> extends AbstractWMTask<T> implements Don<K,T> {
    private static final Logger LOG = LoggerFactory.getLogger(DonLiteral.class);
    protected K core;
    private URI key;
    protected AbstractTraitFactory factory;
    protected ModifyLiteral setter;
    protected With[] with;

    protected abstract MetaClass<T> getMetaClassInfo();

    public DonLiteral( K target, With... with ) {
        this.core = target;
        this.with = with;
    }

    public K getCore() {
        return core;
    }

    void setCore( K target ) {
        this.core = target;
    }

    @Override
    public KIND kind() {
        return KIND.DON;
    }

    @Override
    public Object getTargetId() {
        return MetadataContainer.getIdentifier( core );
    }

    public T call() {
        if ( getCore() == null ) {
            return null;
        }
        if ( factory != null && factory instanceof StandaloneTraitFactory) {
            StandaloneTraitFactory stf = (StandaloneTraitFactory) factory;
            try {
                T x = (T) stf.don( getCore() instanceof TraitableBean ? ((TraitableBean) getCore()) : stf.makeTraitable( getCore(), getCore().getClass() ),
                                    getTrait() != null ? getTrait() : Thing.class );
                if ( setter != null ) {
                    setter.call( x );
                }
                return x;
            } catch ( LogicalTypeInconsistencyException e ) {
                LOG.error("Exception", e);
            }
        } else {
            throw new UnsupportedOperationException(  );
        }
        return null;
    }

    public Modify getInitArgs() {
        return setter;
    }

    @Override
    public URI getUri() {
        if ( key == null ) {
            key = createURI();
        }
        return key;
    }

    @Override
    public Object getId() {
        return getUri();
    }

    protected URI createURI() {
        StringBuilder sb = new StringBuilder();
        sb.append( MetadataContainer.getIdentifier( core ) );

        sb.append( "?don=" ).append( getTrait().getName() );

        return URI.create( sb.toString() );
    }

    public static URI createURI( String coreId, Class type ) {
        StringBuilder sb = new StringBuilder();
        sb.append( coreId );

        sb.append( "?don=" ).append( type.getName() );

        return URI.create( sb.toString() );
    }

    @Override
    public Don<K,T> setTraitFactory( AbstractTraitFactory factory ) {
        this.factory = factory;
        return this;
    }

    @Override
    public ModifyLiteral<T> getSetters() {
        return setter;
    }

    @Override
    public Object getTarget() {
        return core;
    }
}
