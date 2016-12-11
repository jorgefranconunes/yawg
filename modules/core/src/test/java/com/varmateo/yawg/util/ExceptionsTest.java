/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.varmateo.yawg.util.Exceptions;


/**
 *
 */
public final class ExceptionsTest
 {


    /**
     *
     */
    @Test
    public void withFmtArgs() {

        assertThatThrownBy(
                () -> Exceptions.raise(ExceptionOk.class, "All is {0}", "well"))
                .isInstanceOf(ExceptionOk.class)
                .hasMessage("All is well")
                .hasNoCause();
    }


    /**
     *
     */
    @Test
    public void withNoFmtArgs() {

        assertThatThrownBy(
                () -> Exceptions.raise(ExceptionOk.class, "All is {0}"))
                .isInstanceOf(ExceptionOk.class)
                .hasMessage("All is {0}")
                .hasNoCause();
    }


    /**
     *
     */
    @Test
    public void withCause() {

        Exception cause = new RuntimeException("The Cause");

        assertThatThrownBy(
                () -> Exceptions.raise(
                        ExceptionOk.class,
                        cause,
                        "All is {0}", "well"))
                .isInstanceOf(ExceptionOk.class)
                .hasMessage("All is well")
                .hasCause(cause);
    }


    /**
     *
     */
    @Test
    public void noConstructorWithMessage() {

        assertThatThrownBy(
                () -> Exceptions.raise(
                        ExceptionNoConstructorWithMessage.class,
                        "Fail"))
                .isInstanceOf(IllegalArgumentException.class);
    }


    /**
     *
     */
    private static final class ExceptionOk
            extends RuntimeException {


        /**
         *
         */
        public ExceptionOk(final String msg) {
            super(msg);
        }


        /**
         *
         */
        public ExceptionOk(
                final String msg,
                final Throwable cause) {
            super(msg, cause);
        }


    }


    /**
     *
     */
    private static final class ExceptionNoConstructorWithMessage
            extends RuntimeException {


        /**
         *
         */
        public ExceptionNoConstructorWithMessage() {
            // We do nothing.
        }


    }


}
