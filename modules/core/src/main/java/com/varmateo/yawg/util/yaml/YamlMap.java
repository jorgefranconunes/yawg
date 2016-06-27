/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util.yaml;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.varmateo.yawg.util.yaml.YamlList;
import com.varmateo.yawg.util.yaml.YamlUtils;


/**
 *
 */
public final class YamlMap
        extends Object {


    private final Map<String,Object> _map;


    /**
     *
     */
    /* package private */ YamlMap(final Map<String,Object> map) {

        _map = Objects.requireNonNull(map);
    }


    /**
     *
     */
    public Map<String,Object> asMap() {

        Map<String,Object> result = Collections.unmodifiableMap(_map);

        return result;
    }


    /**
     *
     */
    public String getString(final String key) {

        String result = YamlUtils.getWithType(_map, key, String.class);

        return result;
    }


    /**
     *
     */
    public YamlMap getMap(final String key) {

        Map<String,Object> map = YamlUtils.getWithType(_map, key, Map.class);
        YamlMap result = (map==null) ? null : new YamlMap(map);

        return result;
    }


    /**
     *
     */
    public <T> YamlList<T> getList(
            final String key,
            final Class<T> itemsClass) {

        List<Object> list = YamlUtils.getWithType(_map, key, List.class);
        YamlList<T> result =
                (list==null) ? null : new YamlList<>(list, itemsClass);

        return result;
    }


    /**
     *
     */
    public Set<String> keySet() {

        return _map.keySet();
    }


}
