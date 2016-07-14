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
    private TemplateVars(final Builder builder) {

        _map = Collections.unmodifiableMap(new HashMap<>(builder._map));
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


    /**
     * A builder of <code>TemplateVars</code> instances.
     */
    public static final class Builder
            extends Object {


        private Map<String,Object> _map;


        /**
         *
         */
        public Builder() {

            _map = new HashMap<>();
        }


        /**
         *
         */
        public Builder(final TemplateVars vars) {

            _map = new HashMap<>(vars._map);
        }


        /**
         *
         */
        public Builder addVar(
                final String varName,
                final Object varValue) {

            _map.put(varName, varValue);

            return this;
        }


        /**
         *
         */
        public TemplateVars build() {

            TemplateVars result = new TemplateVars(this);

            return result;
        }


    }


}
