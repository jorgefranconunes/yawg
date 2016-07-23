/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;


/**
 *
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
     *
     */
    public static <T> Collection<T> getAll(final Class<T> klass) {

        ServiceLoader<T> loader = ServiceLoader.load(klass);
        Iterator<T> allServices = loader.iterator();
        Collection<T> result = Lists.newList(allServices);

        return result;
    }


}
