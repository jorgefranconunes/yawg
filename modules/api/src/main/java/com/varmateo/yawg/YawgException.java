/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;


/**
 * An exception signaling an abnormal occurence during a baking
 * related activity.
 */
public final class YawgException
    extends RuntimeException {


    /**
     *
     */
    public YawgException(final String msg) {

        super(msg);
    }


    /**
     *
     */
    public YawgException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


}

