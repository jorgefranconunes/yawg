/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.util.Exceptions;


/**
 * Utility methods for throwing <code>YawgException</code>.
 */
public final class YawgExceptions {


    /**
     * No instances of this class are to be created.
     */
    private YawgExceptions() {
        // Nothing to do.
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

