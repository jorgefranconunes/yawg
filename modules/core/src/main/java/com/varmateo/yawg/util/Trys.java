/**************************************************************************
 *
 * Copyright (c) 2019-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.vavr.control.Try;


/**
 * Utility functions related with {@code io.vavr.control.Try}.
 */
public final class Trys {


    /**
     * No instances of this class are to be created.
     */
    private Trys() {
        // Nothing to do.
    }


    /**
     *
     */
    public static Try<Void> success() {

        return Try.success(null);
    }


    /**
     *
     */
    public static <T> Try<T> flatten(final Try<Try<T>> x) {

        if ( x.isSuccess() ) {
            return x.get();
        } else {
            return Try.failure(x.getCause());
        }
    }


    /**
     * Applies the given function to the values contained in the given
     * results.
     *
     * If any of the results is not a success, then the function is
     * not called at all, and a failure is returned. The returned
     * failure is either the failure contained in the first, or in the
     * second result.
     */
    public static <T, U, R> Try<R> apply(
            final BiFunction<T, U, Try<R>> f,
            final Try<T> x,
            final Try<U> y) {

        if ( x.isSuccess() ) {
            if ( y.isSuccess() ) {
                return flatten(Try.of(() -> f.apply(x.get(), y.get())));
            } else {
                return Try.failure(y.getCause());
            }
        } else {
            return Try.failure(x.getCause());
        }
    }

    /**
     * Applies the given function to the values returned by the given
     * suppliers.
     *
     * If any of the results is not a success, then the function is
     * not called at all, and a failure is returned. If the first
     * result is not a success, then the second supplier will not be
     * called. The returned failure is either the failure contained in
     * the first, or in the second result.
     */
    public static <T, U, R> Try<R> apply(
            final BiFunction<T, U, Try<R>> f,
            final Supplier<Try<T>> x,
            final Supplier<Try<U>> y) {

        final Try<T> xResult = flatten(Try.of(() -> x.get()));

        if ( xResult.isSuccess() ) {
            final Try<U> yResult = flatten(Try.of(() -> y.get()));

            if ( yResult.isSuccess() ) {
                return flatten(Try.of(() -> f.apply(xResult.get(), yResult.get())));
            } else {
                return Try.failure(yResult.getCause());
            }
        } else {
            return Try.failure(xResult.getCause());
        }
    }

}
