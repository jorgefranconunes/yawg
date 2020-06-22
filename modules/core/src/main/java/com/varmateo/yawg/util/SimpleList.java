/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.varmateo.yawg.api.YawgException;


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
    @Override
    public Iterator<T> iterator() {

        return new SimpleIterator<>(this);
    }


    /**
     *
     */
    public T get(final int index) {

        return getWithType(index, _itemsClass);
    }


    /**
     *
     */
    public SimpleMap getMap(final int index) {

        @SuppressWarnings("unchecked")
        final Map<String, Object> value = (Map<String, Object>) getWithType(index, Map.class);

        return new SimpleMap(value);
    }


    /**
     *
     */
    public <T> SimpleList<T> getList(
            final int index,
            final Class<T> itemsClass) {

        @SuppressWarnings("unchecked")
        final List<Object> value = (List<Object>) getWithType(index, List.class);

        return new SimpleList<T>(value, itemsClass);
    }


    /**
     *
     */
    public <T> T getWithType(
            final int index,
            final Class<T> klass) {

        final Object value = _list.get(index);

        if ( (value == null) || !klass.isInstance(value) ) {
            throw SimpleListException.invalidValue(index, klass, value);
        }

        @SuppressWarnings("unchecked")
        final T result = (T) value;

        return result;
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
    private static final class SimpleIterator<T>
            implements Iterator<T> {


        private final SimpleList<T> _simpleList;
        private int _index;


        /**
         *
         */
        /* default */ SimpleIterator(final SimpleList<T> simpleList) {

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
            final T result = _simpleList.get(_index);
            ++_index;

            return result;
        }


    }


}
