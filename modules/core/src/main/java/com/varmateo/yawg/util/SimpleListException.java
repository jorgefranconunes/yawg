/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import com.varmateo.yawg.api.YawgException;


/*
 *
 */
public final class SimpleListException
        extends YawgException {


    private SimpleListException(final String msg) {

        super(msg);
    }


    /**
     *
     */
    public static SimpleListException invalidValue(
            final int index,
            final Class<?> expectedValueType,
            final Object actualValue) {

        final String actualType =
                (actualValue == null) ? "NULL" : actualValue.getClass().getSimpleName();

        final String msg = String.format(
                "Invalid %s value in %s position %d",
                actualType,
                expectedValueType.getSimpleName(),
                index);

        return new SimpleListException(msg);
    }
}
