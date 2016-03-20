/**************************************************************************
 *
 * Copyright (c) 2015 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.commons.cli;

import java.text.MessageFormat;





/**************************************************************************
 *
 * An exception signaling an occurrence intended to be communicated to
 * the end user.
 *
 **************************************************************************/

public final class CliException
    extends Exception {





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    private CliException(final String msg) {

        super(msg);
    }





/**************************************************************************
 *
 * Throws a newly created <code>CliException</code>
 *
 **************************************************************************/

    public static void raise(final String    msgFmt,
                             final Object... fmtArgs)
        throws CliException {

        String msg = null;

        if ( (fmtArgs==null) || (fmtArgs.length==0) ) {
            msg = msgFmt;
        } else {
            msg = MessageFormat.format(msgFmt, fmtArgs);
        }

        throw new CliException(msg);
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

