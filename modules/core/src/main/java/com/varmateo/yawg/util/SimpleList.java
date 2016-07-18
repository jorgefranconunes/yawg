/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.util.SimpleMap;


/**
 *
 */
public final class SimpleList<T>
        extends Object {


    private final List<Object> _list;
    private final Class<T> _itemsClass;


    /**
     *
     */
    public SimpleList(
            final List<Object> list,
            final Class<T> itemsClass) {

        _list = Objects.requireNonNull(list);
        _itemsClass = Objects.requireNonNull(itemsClass);
    }


    /**
     *
     */
    public T get(final int index) {

        T result = getWithType(index, _itemsClass);

        return result;
    }


    /**
     *
     */
    public SimpleMap getMap(final int index) {

        Map<String,Object> map = getWithType(index, Map.class);
        SimpleMap result = new SimpleMap(map);

        return result;
    }


    /**
     *
     */
    public <T> SimpleList<T> getList(
            final int index,
            final Class<T> itemsClass) {

        List<Object> list = getWithType(index, List.class);
        SimpleList<T> result = new SimpleList<T>(list, itemsClass);

        return result;
    }


    /**
     *
     */
    public <T> T getWithType(
            final int index,
            final Class<T> klass)
            throws YawgException {

        Object value = _list.get(index);

        if ( (value==null) || !klass.isInstance(value) ) {
            YawgException.raise(
                    "Invalid {2} value in {1} position {0}",
                    index,
                    klass.getSimpleName(),
                    (value==null) ? "NULL" : value.getClass().getSimpleName());
        }

        T result = (T)value;

        return result;
    }


    /**
     *
     */
    public int size() {

        int result = _list.size();

        return result;
    }


}
