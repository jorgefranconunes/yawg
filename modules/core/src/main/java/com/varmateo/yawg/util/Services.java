/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;

import com.varmateo.yawg.commons.util.Lists;


/**
 * Utility functions to support common use cases in the Java
 * service-provider loading facility.
 */
public final class Services
        extends Object {

    /**
     * No instances of this class are to be created.
     */
    private Services() {
        // Nothing to do.
    }


    /**
     * Retrieves all available services of the given class.
     *
     * @param klass The class of the services we are searching for.
     *
     * @return All available services of the given class.
     */
    public static <T> Collection<T> getAll(final Class<T> klass) {

        ServiceLoader<T> loader = ServiceLoader.load(klass);
        Iterator<T> allServices = loader.iterator();
        Collection<T> result = Lists.newList(allServices);

        return result;
    }


}
