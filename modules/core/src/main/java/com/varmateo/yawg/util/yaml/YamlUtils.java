/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util.yaml;

import java.util.List;
import java.util.Map;

import com.varmateo.yawg.YawgException;


/**
 *
 */
/* package private */ final class YamlUtils
        extends Object {


    /**
     * No instances of this class are to be created.
     */
    private YamlUtils() {
    }


    /**
     *
     */
    public static <T> T getWithType(
            final Map<String,Object> map,
            final String key,
            final Class<T> klass)
            throws YawgException {

        Object value = map.get(key);

        if ( (value!=null) && !klass.isInstance(value) ) {
            YawgException.raise(
                    "Invalid {2} value in {1} field \"{0}\"",
                    key,
                    klass.getSimpleName(),
                    value.getClass().getSimpleName());
        }

        T result = (T)value;

        return result;
    }


    /**
     *
     */
    public static <T> T getWithType(
            final List<Object> list,
            final int index,
            final Class<?> klass)
            throws YawgException {

        Object value = list.get(index);

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


}
