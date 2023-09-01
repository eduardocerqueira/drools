package org.drools.traits.core.factmodel;

import java.io.Serializable;
import java.util.Map;

import org.drools.base.factmodel.traits.CoreWrapper;
import org.drools.base.factmodel.traits.Thing;
import org.drools.base.factmodel.traits.TraitFieldTMS;
import org.drools.base.factmodel.traits.Traitable;
import org.drools.base.factmodel.traits.TraitableBean;

import static org.drools.base.factmodel.traits.TraitConstants.FIELDTMS_FIELD_NAME;
import static org.drools.base.factmodel.traits.TraitConstants.TRAITSET_FIELD_NAME;

@Traitable
public interface TraitableMap extends TraitableBean<Map, CoreWrapper<Map>>, Serializable, Map<String,Object>, CoreWrapper<Map> {

	@Override
	default Map<String, Object> _getDynamicProperties() {
		return this;
	}

	@Override
	default void _setDynamicProperties(Map<String, Object> map) {

	}


	default TraitFieldTMS _getFieldTMS() {
    	TraitFieldTMS tms = ( TraitFieldTMS ) _getDynamicProperties().get( FIELDTMS_FIELD_NAME );
        if ( tms == null ) {
        	tms = new TraitFieldTMSImpl();
            _getDynamicProperties().put( FIELDTMS_FIELD_NAME, tms );
        }
        return tms;
    }

    default void _setFieldTMS(TraitFieldTMS __$$field_Tms$$) {
        _getDynamicProperties().put( FIELDTMS_FIELD_NAME, __$$field_Tms$$ );
    }

    default void _setTraitMap(Map map) {
        _getDynamicProperties().put( TRAITSET_FIELD_NAME, map );
    }

    default Map<String, Thing<Map>> _getTraitMap() {
        return ( Map<String, Thing<Map>> ) _getDynamicProperties().get( TRAITSET_FIELD_NAME );
    }

    default void init(Map core) {
//        __$$dynamic_properties_map$$ = core;
    }

    default Map getCore() {
        return _getDynamicProperties();
    }

}

