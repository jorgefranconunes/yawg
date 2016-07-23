/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Utility functions for manipulating lists.
 */
public final class Lists
    extends Object {


    /**
     * No instances of this class are to be created.
     */
    private Lists() {
    }


    /**
     * Creates a new list where the elements are obtained by applying
     * the given function to each of the elements of the given
     * collection.
     *
     * <p>The order of the elements in the returned list is the same
     * order of the given input collection iterator.</p>
     *
     * @param <T> The type of the elements in the input collection.
     *
     * @param <R> The type of the elements in the returned list.
     *
     * @param inputCollection The elements to be transformed by the
     * given function.
     *
     * @param function The function to be applied to each element in
     * the given collection.
     *
     * @return A newly created list with the mapped elements.
     */
    public static <T,R> List<R> map(
            final Collection<T> inputCollection,
            final Function<T,R> function) {

        List<R> mappedList =
            inputCollection.stream()
            .map(function)
            .collect(Collectors.toList());

        return mappedList;
    }


    /**
     * Creates a new immutable list with the elements contained in the
     * given iterator.
     *
     * @param <T> The type of elements in the given iterator and on
     * the returned list.
     *
     * @param iterator The source of the elements that will be
     * contained in the returned list.
     *
     * @return An immutable list containing all the elements in the
     * given iterator, in the same order.
     */
    public static <T> List<T> newList(final Iterator<T> iterator) {

        List<T> newList = new ArrayList<T>();

        while ( iterator.hasNext() ) {
            T element = iterator.next();
            newList.add(element);
        }

        List<T> result = Collections.unmodifiableList(newList);

        return result;
    }


    /**
     * Builds a read-only copy of the given collection.
     *
     * <p>The order of the elements in the returned list is the same
     * order of the given collection iterator.</p>
     *
     * @param <T> The type of the elements in the given and returned
     * collections.
     *
     * @param inputCollection The collection to be copied.
     *
     * @return A new read only list containing the elements in the
     * given collection.
     */
    public static <T> List<T> readOnlyCopy(
            final Collection<T> inputCollection) {

        List<T> copy = new ArrayList<T>(inputCollection);
        List<T> result = Collections.unmodifiableList(copy);

        return result;
    }


}
