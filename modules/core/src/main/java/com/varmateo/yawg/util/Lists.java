/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
