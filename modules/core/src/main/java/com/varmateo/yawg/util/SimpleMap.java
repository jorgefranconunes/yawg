/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.util.Exceptions;
import com.varmateo.yawg.util.SimpleList;


/**
 * Wrapper for an unmodifiable map with utility methods for retrieving
 * values.
 */
public final class SimpleMap {


    private final Map<String,Object> _map;


    /**
     * Initializes this simple map with the given map.
     *
     * <p>As a matter of optimization we assume the given map will
     * never be modified. It is up to the caller to ensure no changes
     * are performed in the givan map</p>.
     *
     * @param map The map to be wrapped by this simple map.
     */
    public SimpleMap(final Map<String,Object> map) {

        _map = Objects.requireNonNull(map);
    }


    /**
     * Fetches a view on the contents of this simple map as an
     * unmodifiable map.
     *
     * @return A unmodifiable view of the contents of this simple map.
     */
    public Map<String,Object> asMap() {

        return Collections.unmodifiableMap(_map);
    }


    /**
     *
     */
    public <T> Optional<T> get(
            final String key,
            final Class<T> klass) {

        T value = getWithType(key, klass);

        return Optional.ofNullable(value);
    }


    /**
     *
     */
    public Optional<String> getString(final String key) {

        return get(key, String.class);
    }


    /**
     *
     */
    public Optional<SimpleMap> getMap(final String key) {

        @SuppressWarnings("unchecked")
        Map<String,Object> value =
                (Map<String,Object>)getWithType(key, Map.class);

        return Optional.ofNullable(value)
                .map(SimpleMap::new);
    }


    /**
     *
     */
    public <T> Optional<SimpleList<T>> getList(
            final String key,
            final Class<T> itemsClass) {

        @SuppressWarnings("unchecked")
        List<Object> value = (List<Object>)getWithType(key, List.class);

        return Optional.ofNullable(value)
                .map(v -> new SimpleList<T>(v, itemsClass));
    }


    /**
     *
     */
    private <T> T getWithType(
            final String key,
            final Class<T> klass)
            throws YawgException {

        Object value = _map.get(key);

        if ( (value!=null) && !klass.isInstance(value) ) {
            Exceptions.raise(
                    "Invalid {2} value in {1} field \"{0}\"",
                    key,
                    klass.getSimpleName(),
                    value.getClass().getSimpleName());
        }

        @SuppressWarnings("unchecked")
        T result = (T)value;

        return result;
    }


    /**
     *
     */
    public Set<String> keySet() {

        return _map.keySet();
    }


}
