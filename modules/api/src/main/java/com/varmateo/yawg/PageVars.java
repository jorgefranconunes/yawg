/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
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
public final class PageVars
        extends Object {


    private final Map<String,Object> _map;


    /**
     * @param map Immutable mapping.
     */
    private PageVars(final Map<String,Object> map) {

        _map = map;
    }


    /**
     *
     */
    private PageVars(final Builder builder) {

        _map = Collections.unmodifiableMap(new HashMap<>(builder._map));
    }


    /**
     *
     */
    public PageVars() {

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
     * <code>PageVars</code>.
     *
     * @param data Used for initializing the builder state.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder(final Map<String,Object> data) {

        Builder result = new Builder(data);

        return result;
    }


    /**
     * Creates a new builder initialized with the data from the given
     * <code>PageVars</code>.
     *
     * @param data Used for initializing the builder state.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder(final PageVars data) {

        Builder result = new Builder(data);

        return result;
    }


    /**
     *
     */
    public PageVars mergeOnTopOf(final PageVars that) {

        Map<String,Object> newMap = new HashMap<>(that._map);
        newMap.putAll(_map);

        PageVars result = new PageVars(newMap);

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
     * A builder of <code>PageVars</code> instances.
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
        private Builder(final Map<String,Object> map) {

            _map = new HashMap<>(map);
        }


        /**
         *
         */
        private Builder(final PageVars vars) {

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
        public PageVars build() {

            PageVars result = new PageVars(this);

            return result;
        }


    }


}