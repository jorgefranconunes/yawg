/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

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
public final class PageVars {


    private final Map<String,Object> _map;


    /**
     *
     */
    private PageVars(final Builder builder) {

        _map = Collections.unmodifiableMap(new HashMap<>(builder._map));
    }


    /**
     * Initializes an empty set of variables.
     */
    public PageVars() {

        _map = Collections.emptyMap();
    }


    /**
     * Retrieves the value of one of the variables.
     *
     * @param key The name of the variable whose value is to be
     * returned.
     *
     * @return An <code>Optional</code> containing the value of the
     * given variable, or nan empty <code>Optional</code> if the
     * variable does not exist.
     */
    public Optional<Object> get(final String key) {

        Object value = _map.get(key);

        return Optional.ofNullable(value);
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
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        return new Builder();
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

        return new Builder(data);
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

        return new Builder(data);
    }


    /**
     * A builder of <code>PageVars</code> instances.
     */
    public static final class Builder {


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
        public Builder addPageVars(final PageVars that) {

            _map.putAll(that._map);

            return this;
        }


        /**
         *
         */
        public PageVars build() {

            return new PageVars(this);
        }


    }


}
