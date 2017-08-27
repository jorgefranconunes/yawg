/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.logging;

import java.util.function.Supplier;


/**
 * Extends the logger interface with additional utility methods.
 */
public final class LogWithUtils
        implements Log {


    private final Log _myLog;


    /**
     *
     */
    private LogWithUtils(final Log myLog) {

        _myLog = myLog;
    }


    /**
     *
     */
    public static LogWithUtils from(final Log myLog) {

        return new LogWithUtils(myLog);
    }


    /**
     *
     */
    public static LogWithUtils createFor(final Object owner) {

        Log myLog  = LogFactory.createFor(owner);

        return LogWithUtils.from(myLog);
    }


    /**
     *
     */
    public void logDelay(
            final String   subject,
            final Runnable runnable) {

        Supplier<Void> supplier = () -> {
            runnable.run();
            return null;
        };
        logDelay(subject, supplier);
    }


    /**
     * Performs the given action and logs how long it took to
     * complete.
     *
     * @param subject Short action description used when logging just
     * before the action is started and immediatly after the action
     * completes.
     *
     * @param action The action to be executed.
     *
     * @return The same value returned by the given action.
     *
     * @param <T> The return type of the given action.
     */
    public <T> T logDelay(
            final String      subject,
            final Supplier<T> action) {

        _myLog.info("Starting {0}...", subject);

        long startTime = System.currentTimeMillis();
        T result = performAction(subject, action);
        long delay = System.currentTimeMillis() - startTime;

        _myLog.info("Done {0} (delay {1}ms)", subject, delay);

        return result;
    }


    /**
     *
     */
    private <T> T performAction(final String subject,
                                final Supplier<T> action) {

        T result = null;
        RuntimeException error = null;

        try {
            result = action.get();
        } catch ( RuntimeException e ) {
            _myLog.warning("Failed {0} - {1} - {2}",
                           subject,
                           e.getClass().getSimpleName(),
                           e.getMessage());
            error = e;
        }

        if ( error != null ) {
            throw error;
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(final String msg) {

        _myLog.warning(msg);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(final String    msg,
                        final Object... fmtArgs) {

        _myLog.warning(msg, fmtArgs);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(final Throwable error,
                        final String    msg,
                        final Object... fmtArgs) {

        _myLog.warning(error, msg, fmtArgs);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final String msg) {

        _myLog.info(msg);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final String    msg,
                     final Object... fmtArgs) {

        _myLog.info(msg, fmtArgs);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final String msg) {

        _myLog.debug(msg);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final String    msg,
                      final Object... fmtArgs) {

        _myLog.debug(msg, fmtArgs);
    }


}





/***************************************************************************
 *
 *
 *
 ***************************************************************************/

