/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.util.NoSuchElementException;


/**
 *
 */
public abstract class Result<T> {

    private Result() {
        // Nothing to do.
    }


    /**
     *
     */
    public static <T> Result<T> success(final T value) {
        return new Success<>(value);
    }


    /**
     *
     */
    public static <T> Result<T> failure(final YawgException cause) {
        return new Failure<>(cause);
    }


    /**
     *
     */
    public abstract boolean isSuccess();


    /**
     *
     */
    public abstract T get();


    /**
     *
     */
    public abstract YawgException failureCause();


    /**
     *
     */
    public static final class Success<T>
            extends Result<T> {

        private final T _value;


        private Success(final T value) {
            _value = value;
        }


        @Override
        public boolean isSuccess() {
            return true;
        }


        @Override
        public T get() {
            return _value;
        }


        @Override
        public YawgException failureCause() {
            throw new NoSuchElementException();
        }
    }


    /**
     *
     */
    public static final class Failure<T>
            extends Result<T> {

        private final YawgException _cause;


        private Failure(final YawgException cause) {
            _cause = cause;
        }


        @Override
        public boolean isSuccess() {
            return false;
        }


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
