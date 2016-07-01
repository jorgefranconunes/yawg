/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Set of variables made available to the template when generating the
 * final bake result.
 *
 * <p>The meaning of this variables is template specific.</p>
 */
public final class TemplateVars
        extends Object {


    private final Map<String,Object> _map;


    /**
     * @param map Immutable mapping.
     */
    /* package private */ TemplateVars(final Map<String,Object> map) {

        _map = map;
    }


    /**
     *
     */
    public TemplateVars() {

        _map = Collections.emptyMap();
    }


    /**
     *
     */
    public TemplateVars mergeOnTopOf(final TemplateVars that) {

        Map<String,Object> newMap = new HashMap<>(that._map);
        newMap.putAll(_map);

        TemplateVars result = new TemplateVars(newMap);

        return result;
    }


    /**
     *
     */
    public Optional<Object> get(final String key) {

        Object value = _map.get(key);
        Optional<Object> result = Optional.ofNullable(value);

        return result;
    }


}
