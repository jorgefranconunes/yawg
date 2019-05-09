/**************************************************************************
 *
 * Copyright (c) 2018 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.logging;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.vavr.Function0;
import io.vavr.control.Try;

import com.varmateo.yawg.logging.Log;


/**
 * Utility functions related with logging.
 */
public final class Logs {


    /**
     * No instance of this class are to be created.
     */
    private Logs() {
        // Nothing to do.
    }


    /**
     *
     */
    public static <T> T logDuration(
            final Log log,
            final String subject,
            final Supplier<T> action) {

        Supplier<Try<T>> liftedAction = Function0.liftTry(action);
        boolean isVerbose = false;
        Try<T> result = logTryDuration(log, subject, liftedAction, isVerbose);

        return result.get();
    }


    /**
     *
     */
    public static Runnable decorateWithLogDuration(
            final Log log,
            final String description,
            final Runnable function) {

        return () -> logDuration(
                log,
                description,
                () -> {
                    function.run();
                    return null;
                });
    }


    /**
     *
     */
    public static <T, U> BiConsumer<T, U> decorateWithLogDuration(
            final Log log,
            final BiFunction<T, U, String> description,
            final BiConsumer<T, U> function) {

        return (t, u) -> logDuration(
                log,
                description.apply(t, u),
                () -> {
                    function.accept(t, u);
                    return null;
                });
    }


    /**
     *
     */
    private static <T> Try<T> logTryDuration(
            final Log log,
            final String subject,
            final Supplier<Try<T>> action,
            final boolean isVerbose) {

        log.info("Starting {0}...", subject);

        long startTime = System.currentTimeMillis();
        Try<T> result = Try.of(action::get).flatMap(x -> x);
        long delay = System.currentTimeMillis() - startTime;

        if (result.isSuccess()) {
            log.info("Done {0} ({1} ms)", subject, delay);
        } else {
            final Throwable cause = result.getCause();

            if (isVerbose) {
                // Log with stack trace.
                log.warning(cause, "Failed {0} ({1} ms)", subject, delay);
            } else {
                // Log without stack trace.
                log.warning(
                        "Failed {0} ({1} ms) - {2} - {3}",
                        subject, delay,
                        cause.getClass().getName(), cause.getMessage());
            }
        }

        return result;
    }

}
