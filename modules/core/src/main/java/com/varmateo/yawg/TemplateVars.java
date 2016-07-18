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
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
    }


    /**
     * Creates a new builder initialized with the data from the given
     * <code>TemplateVars</code>.
     *
     * @param data Used for initializing the builder state.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder(final TemplateVars data) {

        Builder result = new Builder(data);

        return result;
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
     * Fetches a view of the set of vars as an unmodifiable map.
     *
     * @return An unmodifiable map containing all the vars. Each entry
     * is one var.
     */
    public Map<String,Object> asMap() {

        return _map;
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
        private Builder() {

            _map = new HashMap<>();
        }


        /**
         *
         */
        private Builder(final TemplateVars vars) {

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
