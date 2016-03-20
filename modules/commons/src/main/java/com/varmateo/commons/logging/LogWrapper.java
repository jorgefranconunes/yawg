/**************************************************************************
 *
 * Copyright (c) 2015 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.commons.logging;

import com.varmateo.commons.logging.Log;





/***************************************************************************
 *
 * A wrapper for an existing <code>Log</code>, intended to be
 * augmented by derived classes.
 *
 ***************************************************************************/

public abstract class LogWrapper
    extends Object
    implements Log {





    private Log _logger = null;





/***************************************************************************
 *
 * Specifies the wrapper logger to be used.
 *
 * @param logger The <code>Log</code> instance to whom all methods
 * delegate to.
 *
 ***************************************************************************/

    protected LogWrapper(final Log logger) {

        _logger = logger;
    }





/***************************************************************************
 *
 * {@inheritDoc}
 *
 ***************************************************************************/

    @Override
    public void warning(final String msg) {

        _logger.warning(msg);
    }





/***************************************************************************
 *
 * Log a WARNING message.
 *
 * @param msg The log message format.
 *
 * @param fmtArgs Formating arguments used when generating the actual
 * message that is logged.
 *
 ***************************************************************************/

    @Override
    public void warning(final String    msg,
                        final Object... fmtArgs) {

        _logger.warning(msg, fmtArgs);
    }





/***************************************************************************
 *
 * Log a WARNING message.
 *
 * @param error The exception associated with the log message.
 *
 * @param msg The log message format.
 *
 * @param fmtArgs Formating arguments used when generating the actual
 * message that is logged.
 *
 ***************************************************************************/

    @Override
    public void warning(final Throwable error,
                        final String    msg,
                        final Object... fmtArgs) {

        _logger.warning(error, msg, fmtArgs);
    }





/***************************************************************************
 *
 * Log a INFO message.
 *
 * @param msg The message to be logged.
 *
 ***************************************************************************/

    @Override
    public void info(final String msg) {

        _logger.info(msg);
    }





/***************************************************************************
 *
 * Log a INFO message.
 *
 * @param msg The log message format.
 *
 * @param fmtArgs Formating arguments used when generating the actual
 * message that is logged.
 *
 ***************************************************************************/

    @Override
    public void info(final String    msg,
                     final Object... fmtArgs) {

        _logger.info(msg, fmtArgs);
    }





/***************************************************************************
 *
 * Log a DEBUG message.
 *
 * @param msg The message to be logged.
 *
 ***************************************************************************/

    @Override
    public void debug(final String msg) {

        _logger.debug(msg);
    }





/***************************************************************************
 *
 * Log a DEBUG message.
 *
 * @param msg The log message format.
 *
 * @param fmtArgs Formating arguments used when generating the actual
 * message that is logged.
 *
 ***************************************************************************/

    @Override
    public void debug(final String    msg,
                      final Object... fmtArgs) {

        _logger.debug(msg, fmtArgs);
    }


}





/***************************************************************************
 *
 *
 *
 ***************************************************************************/

