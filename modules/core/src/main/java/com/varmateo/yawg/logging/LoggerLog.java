/**************************************************************************
 *
 * Copyright (c) 2015-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


/**
 * A <code>Log</code> implementation using Java <code>Logger</code>
 * instances. Intended for private use of package
 * com.varmateo.yawg.logging.
 */
/* default */ final class LoggerLog
        implements Log {


    private final Logger _logger;


    /**
     *
     */
    /* default */ LoggerLog(final Logger logger) {

        _logger = logger;
    }


    /**
     * Retrieves the underlying Java <code>Logger</code> that is being
     * used as output.
     *
     * @return The Java <code>Logger</code> underlying this
     * <code>LoggerLog</code>.
     */
    public Logger getLogger() {

        return _logger;
    }


    /**
     * @return The current log level.
     */
    public Level getLevel() {

        return (_logger == null) ? Level.OFF : _logger.getLevel();
    }


    /**
     * Log a WARNING message.
     *
     * @param msg The message to be logged.
     */
    @Override
    public void warning(final String msg) {

        log(Level.WARNING, msg, null);
    }


    /**
     * Log a WARNING message.
     *
     * @param msg The log message format.
     *
     * @param fmtArgs Formating arguments used when generating the
     * actual message that is logged.
     */
    @Override
    public void warning(
            final String    msg,
            final Object... fmtArgs) {

        log(Level.WARNING, msg, fmtArgs);
    }


    /**
     * Log a WARNING message.
     *
     * @param error The exception associated with the log message.
     *
     * @param msg The log message format.
     *
     * @param fmtArgs Formating arguments used when generating the actual
     * message that is logged.
     */
    @Override
    public void warning(
            final Throwable error,
            final String    msg,
            final Object... fmtArgs) {

        log(Level.WARNING, error, msg, fmtArgs);
    }


    /**
     * Log a INFO message.
     *
     * @param msg The message to be logged.
     */
    @Override
    public void info(final String msg) {

        log(Level.INFO, msg, null);
    }


    /**
     * Log a INFO message.
     *
     * @param msg The log message format.
     *
     * @param fmtArgs Formating arguments used when generating the
     * actual message that is logged.
     */
    @Override
    public void info(
            final String    msg,
            final Object... fmtArgs) {

        log(Level.INFO, msg, fmtArgs);
    }


    /**
     * Log a DEBUG message.
     *
     * @param msg The message to be logged.
     */
    @Override
    public void debug(final String msg) {

        log(Level.FINE, msg, null);
    }


    /**
     * Log a DEBUG message.
     *
     * @param msg The log message format.
     *
     * @param fmtArgs Formating arguments used when generating the
     * actual message that is logged.
     */
    @Override
    public void debug(
            final String    msg,
            final Object... fmtArgs) {

        log(Level.FINE, msg, fmtArgs);
    }


    /**
     *
     */
    private void log(
            final Level    level,
            final String   msg,
            final Object[] fmtArgs) {

        if ( _logger != null ) {
            _logger.log(level, msg, fmtArgs);
        }
    }


    /**
     *
     */
    private void log(
            final Level     level,
            final Throwable error,
            final String    msg,
            final Object[]  fmtArgs) {

        if ( _logger != null ) {
            final LogRecord logRecord = new LogRecord(level, msg);

            logRecord.setThrown(error);
            logRecord.setParameters(fmtArgs);
            _logger.log(logRecord);
        }
    }


}
