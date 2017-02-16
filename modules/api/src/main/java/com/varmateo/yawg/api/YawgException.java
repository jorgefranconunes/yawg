/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;


/**
 * An exception signaling an abnormal occurence during a baking
 * related activity.
 */
public final class YawgException
    extends RuntimeException {


    /**
     * @param msg The exception message,
     */
    public YawgException(final String msg) {

        super(msg);
    }


    /**
     * @param msg The exception message.
     *
     * @param cause The underlying cause of the abnormal occurence.
     */
    public YawgException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


}

