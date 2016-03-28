/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.commons.cli;

import com.varmateo.commons.util.Exceptions;


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
    public CliException(final String msg,
                        final Throwable cause) {

        super(msg, cause);
    }


    /**
     * Throws a newly created <code>CliException</code>
     */
    public static void raise(final String    msgFmt,
                             final Object... fmtArgs)
        throws CliException {

        Exceptions.raiseChecked(CliException.class, msgFmt, fmtArgs);
    }


    /**
     * Throws a newly created <code>CliException</code>
     */
    public static void raise(final Throwable cause,
                             final String    msgFmt,
                             final Object... fmtArgs)
        throws CliException {

        Exceptions.raiseChecked(CliException.class, msgFmt, fmtArgs);
    }


}

