/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.util.Exceptions;
import com.varmateo.yawg.util.SimpleMap;


/**
 * Simple list intended for use in deserialization.
 *
 * @param <T> The type of elements in the list.
 */
public final class SimpleList<T>
        implements Iterable<T> {


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
    public Iterator<T> iterator() {

        return new SimpleIterator<>(this);
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
            Exceptions.raise(
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


    /**
     *
     */
    private static final class SimpleIterator<T>
            implements Iterator<T> {


        private final SimpleList<T> _simpleList;
        private int _index;


        /**
         *
         */
        SimpleIterator(final SimpleList<T> simpleList) {

            _simpleList = simpleList;
            _index = 0;
        }


        /**
         *
         */
        @Override
        public boolean hasNext() {

            return _index < _simpleList.size();
        }


        /**
         *
         */
        @Override
        public T next() {

            if ( _index >= _simpleList.size() ) {
                throw new NoSuchElementException();
            }
            T result = _simpleList.get(_index);
            ++_index;

            return result;
        }


    }


}
