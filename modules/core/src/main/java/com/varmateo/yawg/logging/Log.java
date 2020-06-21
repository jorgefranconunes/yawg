/**************************************************************************
 *
 * Copyright (c) 2015-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.logging;


/**
 * A simple logger.
 */

public interface Log {


    /**
     * Log a WARNING message.
     *
     * @param msg The message to be logged.
     */
    void warning(String msg);


    /**
     * Log a WARNING message.
     *
     * @param msg The log message format.
     *
     * @param fmtArgs Formating arguments used when generating the
     * actual message that is logged.
     */
    void warning(
            String    msg,
            Object... fmtArgs);


    /**
     * Log a WARNING message.
     *
     * @param error The exception associated with the log message.
     *
     * @param msg The log message format.
     *
     * @param fmtArgs Formating arguments used when generating the
     * actual message that is logged.
     */
    void warning(
            Throwable error,
            String    msg,
            Object... fmtArgs);


    /**
     * Log a INFO message.
     *
     * @param msg The message to be logged.
     */
    void info(String msg);


    /**
     * Log a INFO message.
     *
     * @param msg The log message format.
     *
     * @param fmtArgs Formating arguments used when generating the
     * actual message that is logged.
     */
    void info(
            String    msg,
            Object... fmtArgs);


    /**
     * Log a DEBUG message.
     *
     * @param msg The message to be logged.
     */
    void debug(String msg);


    /**
     * Log a DEBUG message.
     *
     * @param msg The log message format.
     *
     * @param fmtArgs Formating arguments used when generating the
     * actual message that is logged.
     */
    void debug(
            String    msg,
            Object... fmtArgs);

}
