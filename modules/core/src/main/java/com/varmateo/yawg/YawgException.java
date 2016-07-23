/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import com.varmateo.yawg.util.Exceptions;


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


    /**
     * Throws a newly created <code>YawgException</code>.
     */
    public static void raise(
            final String msgFmt,
            final Object... fmtArgs)
            throws YawgException {

        Exceptions.raise(YawgException.class, msgFmt, fmtArgs);
    }


    /**
     * Throws a newly created <code>YawgException</code>.
     */
    public static void raise(
            final Throwable cause,
            final String msgFmt,
            final Object... fmtArgs)
            throws YawgException {

        Exceptions.raise(YawgException.class, cause, msgFmt, fmtArgs);
    }


}

