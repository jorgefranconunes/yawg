/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
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


/**
 * Wrapper for an unmodifiable map with utility methods for retrieving
 * values.
 */
public final class SimpleMap {


    private final Map<String, Object> _map;


    /**
     * Initializes this simple map with the given map.
     *
     * <p>As a matter of optimization we assume the given map will
     * never be modified. It is up to the caller to ensure no changes
     * are performed in the givan map</p>.
     *
     * @param map The map to be wrapped by this simple map.
     */
    public SimpleMap(final Map<String, Object> map) {

        _map = Objects.requireNonNull(map);
    }


    /**
     * Fetches a view on the contents of this simple map as an
     * unmodifiable map.
     *
     * @return A unmodifiable view of the contents of this simple map.
     */
    public Map<String, Object> asMap() {

        return Collections.unmodifiableMap(_map);
    }


    /**
     *
     */
    public <T> Optional<T> get(
            final String key,
            final Class<T> klass) {

        final T value = getWithType(key, klass);

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
        final Map<String, Object> value = (Map<String, Object>) getWithType(key, Map.class);

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
        final List<Object> value = (List<Object>) getWithType(key, List.class);

        return Optional.ofNullable(value)
                .map(v -> new SimpleList<T>(v, itemsClass));
    }


    /**
     *
     */
    private <T> T getWithType(
            final String key,
            final Class<T> klass) {

        final Object value = _map.get(key);

        if ( (value != null) && !klass.isInstance(value) ) {
            throw SimpleMapException.invalidValue(key, klass, value.getClass());
        }

        @SuppressWarnings("unchecked")
        final T result = (T) value;

        return result;
    }


    /**
     *
     */
    public Set<String> keySet() {

        return _map.keySet();
    }


}
