/**************************************************************************
 *
 * Copyright (c) 2017-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * A builder of <code>PageVars</code> instances.
 */
public final class PageVarsBuilder {


    private final Map<String, Object> _map;


    /**
     *
     */
    private PageVarsBuilder(final Map<String, Object> map) {

        _map = new HashMap<>(map);
    }


    /**
     * Creates a new empty builder.
     *
     * @return A newly created <code>PageVarsBuilder</code> instance.
     */
    public static PageVarsBuilder create() {

        return new PageVarsBuilder(Collections.emptyMap());
    }


    /**
     * Creates a new builder initialized with the data from the given
     * map.
     *
     * @param data Used for initializing the builder state.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static PageVarsBuilder create(final Map<String, Object> data) {

        return new PageVarsBuilder(data);
    }


    /**
     * Creates a new builder initialized with the data from the given
     * <code>PageVars</code>.
     *
     * @param pageVars Used for initializing the builder state.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static PageVarsBuilder create(final PageVars pageVars) {

        return new PageVarsBuilder(pageVars.asMap());
    }


    /**
     * Adds or updates a page variable variable.
     */
    public PageVarsBuilder addVar(
            final String varName,
            final Object varValue) {

        _map.put(varName, varValue);
        return this;
    }


    /**
     * Adds to this builder all the variables contained in the
     * given <code>PageVars</code>.
     */
    public PageVarsBuilder addPageVars(final PageVars pageVars) {

        _map.putAll(pageVars.asMap());
        return this;
    }


    /**
     * Creates a new <code>PageVars</code> instance that containing
     * the data currently in this builder.
     *
     * @return A new <code>PageVars</code> instance.
     */
    public PageVars build() {

        return new PageVarsImpl(_map);
    }


    /**
     *
     */
    private static final class PageVarsImpl
            implements PageVars {


        private final Map<String, Object> _map;


        /**
         *
         */
        /* default */ PageVarsImpl() {

            _map = Collections.emptyMap();
        }


        /**
         *
         */
        /* default */ PageVarsImpl(final Map<String, Object> data) {

            _map = Collections.unmodifiableMap(data);
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Optional<Object> get(final String key) {

            final Object value = _map.get(key);
            return Optional.ofNullable(value);
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> asMap() {

            return _map;
        }

    }


}
