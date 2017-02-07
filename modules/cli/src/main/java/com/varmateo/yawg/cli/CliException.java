/**************************************************************************
 *
 * Copyright (c) 2015-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import com.varmateo.yawg.util.Exceptions;


/**
 * An exception signaling an occurrence intended to be communicated to
 * the end user.
 */
public final class CliException
        extends Exception {


    /**
     *
     */
    public CliException(final String msg) {

        super(msg);
    }


    /**
     *
     */
    public CliException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     * Throws a newly created <code>CliException</code>.
     */
    public static void raise(
            final String    msgFmt,
            final Object... fmtArgs)
            throws CliException {

        Exceptions.raiseChecked(CliException.class, msgFmt, fmtArgs);
    }


    /**
     * Throws a newly created <code>CliException</code>.
     */
    public static void raise(
            final Throwable cause,
            final String    msgFmt,
            final Object... fmtArgs)
            throws CliException {

        Exceptions.raiseChecked(CliException.class, msgFmt, fmtArgs);
    }


}
