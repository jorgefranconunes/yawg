/**************************************************************************
 *
 * Copyright (c) 2019-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.util.NoSuchElementException;


/**
 * Represents the success or failure of an operation. A success has an
 * associated value. A failure has an associated cause.
 *
 * @param <T> The type of the value in a success result.
 */
public abstract class Result<T> {

    private Result() {
        // Nothing to do.
    }


    /**
     * Creates a successful result.
     *
     * @param value The value to be associated with the returned success result.
     */
    public static <T> Result<T> success(final T value) {
        return new Success<>(value);
    }


    /**
     * Created a failed result.
     *
     * @param cause The failure cause to be associated with the
     * resturned failure result.
     */
    public static <T> Result<T> failure(final YawgException cause) {
        return new Failure<>(cause);
    }


    /**
     * Checks if this result is a success.
     *
     * @return True if this result is a success, false otheriwse.
     */
    public abstract boolean isSuccess();


    /**
     * Fetches the value associated with a success result.
     *
     * <p>This method can only be called for success results. Calling
     * it for a failure result will result in a {@code
     * NoSuchElementException} being thrown.</p>
     */
    public abstract T get();


    /**
     * Fetches the cause associated with a failure result.
     *
     * <p>This method can only be called for failure results. Calling
     * it for success result will result in a {@code
     * NoSuchElementException} being thrown.</p>
     */
    public abstract YawgException failureCause();


    /**
     * A success result.
     */
    public static final class Success<T>
            extends Result<T> {

        private final T _value;


        /* default */ Success(final T value) {
            _value = value;
        }


        /**
         * Always true.
         */
        @Override
        public boolean isSuccess() {
            return true;
        }


        @Override
        public T get() {
            return _value;
        }


        /**
         * Always throws {@code NoSuchElementException}.
         */
        @Override
        public YawgException failureCause() {
            throw new NoSuchElementException();
        }
    }


    /**
     * A failure result.
     */
    public static final class Failure<T>
            extends Result<T> {

        private final YawgException _cause;


        /* default */ Failure(final YawgException cause) {
            _cause = cause;
        }


        /**
         * Always false.
         */
        @Override
        public boolean isSuccess() {
            return false;
        }


        /**
         * Always throws {@code NoSuchElementException}.
         */
        @Override
        public T get() {
            throw new NoSuchElementException();
        }


        @Override
        public YawgException failureCause() {
            return _cause;
        }
    }
}
