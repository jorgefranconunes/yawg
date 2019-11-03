/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
public final class FileBakerException
        extends YawgException {


    private FileBakerException(final String msg) {

        super(msg);
    }


    /**
     *
     */
    public static FileBakerException unknownBakerType(final String bakerType) {

        final String msg = String.format("Unknown baker type \"%s\"", bakerType);

        return new FileBakerException(msg);
    }

}
