/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.util.SimpleList;


/**
 * Wrapper for an unmodifiable map with utility methods for retrieving
 * values.
 */
public final class SimpleMap
        extends Object {


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

        Map<String,Object> result = Collections.unmodifiableMap(_map);

        return result;
    }


    /**
     *
     */
    public <T> Optional<T> get(
            final String key,
            final Class<T> klass) {

        T value = getWithType(key, klass);
        Optional<T> result =
                (value==null)
                ? Optional.empty()
                : Optional.of(value);

        return result;
    }


    /**
     *
     */
    public Optional<String> getString(final String key) {

        Optional<String> result = get(key, String.class);

        return result;
    }


    /**
     *
     */
    public Optional<SimpleMap> getMap(final String key) {

        Map<String,Object> map = getWithType(key, Map.class);
        Optional<SimpleMap> result =
                (map==null)
                ? Optional.empty()
                : Optional.of(new SimpleMap(map));

        return result;
    }


    /**
     *
     */
    public <T> Optional<SimpleList<T>> getList(
            final String key,
            final Class<T> itemsClass) {

        List<Object> list = getWithType(key, List.class);
        Optional<SimpleList<T>> result =
                (list==null)
                ? Optional.empty()
                : Optional.of(new SimpleList<T>(list, itemsClass));

        return result;
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
            YawgException.raise(
                    "Invalid {2} value in {1} field \"{0}\"",
                    key,
                    klass.getSimpleName(),
                    value.getClass().getSimpleName());
        }

        T result = (T)value;

        return result;
    }


    /**
     *
     */
    public Set<String> keySet() {

        Set<String> result =_map.keySet();

        return result;
    }


}
