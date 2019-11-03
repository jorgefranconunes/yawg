/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;


/**
 * An exception signaling an abnormal occurence during a baking
 * related activity.
 */
public abstract class YawgException
    extends RuntimeException {


    /**
     * @param msg The exception message,
     */
    protected YawgException(final String msg) {

        super(msg);
    }


    /**
     * @param msg The exception message.
     *
     * @param cause The underlying cause of the abnormal occurence.
     */
    protected YawgException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }

}
