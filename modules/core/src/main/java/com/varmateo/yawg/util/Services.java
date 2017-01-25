/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.Iterator;
import java.util.ServiceLoader;

import javaslang.collection.List;
import javaslang.collection.Seq;


/**
 * Utility functions to support common use cases in the Java
 * service-provider loading facility.
 */
public final class Services {

    /**
     * No instances of this class are to be created.
     */
    private Services() {
        // Nothing to do.
    }


    /**
     * Retrieves all available services of the given class.
     *
     * @param <T> The type os services to retrieve.
     *
     * @param klass The class of the services we are searching for.
     *
     * @return All available services of the given class.
     */
    public static <T> Seq<T> getAll(final Class<T> klass) {

        ServiceLoader<T> loader = ServiceLoader.load(klass);
        Iterator<T> allServices = loader.iterator();
        Iterable<T> iterable = new Iterable<T>() {
                @Override
                public Iterator<T> iterator() {
                    return allServices;
                }
            };
        Seq<T> result = List.ofAll(iterable);

        return result;
    }


}
