/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util.yaml;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.varmateo.yawg.util.yaml.YamlMap;


/**
 *
 */
public final class YamlList<T>
        extends Object {


    private final List<Object> _list;
    private final Class<T> _itemsClass;


    /**
     *
     */
    /* package private */ YamlList(
            final List<Object> list,
            final Class<T> itemsClass) {

        _list = Objects.requireNonNull(list);
        _itemsClass = Objects.requireNonNull(itemsClass);
    }


    /**
     *
     */
    public int size() {

        return _list.size();
    }


    /**
     *
     */
    public String getString(final int index) {

        String result = YamlUtils.getWithType(_list, index, _itemsClass);

        return result;
    }


    /**
     *
     */
    public YamlMap getMap(final int index) {

        Map<String,Object> map = YamlUtils.getWithType(_list, index, Map.class);
        YamlMap result = new YamlMap(map);

        return result;
    }


    /**
     *
     */
    public <T> YamlList<T> getList(
            final int index,
            final Class<T> itemsClass) {

        List<Object> list = YamlUtils.getWithType(_list, index, List.class);
        YamlList result = new YamlList(list, itemsClass);

        return result;
    }


}
