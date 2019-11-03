/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
public final class SimpleMapException
        extends YawgException {


    private SimpleMapException(final String msg) {

        super(msg);
    }


    /**
     *
     */
    public static SimpleMapException invalidValue(
            final String key,
            final Class<?> expectedValueType,
            final Class<?> actualValueType) {

        final String msg = String.format(
                "Unexpected %s value in %s field \"%s\"",
                actualValueType.getSimpleName(),
                expectedValueType.getSimpleName(),
                key);

        return new SimpleMapException(msg);
    }

}
