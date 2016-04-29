/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.util.Exceptions;


/**
 *
 */
public final class ExceptionsTest
        extends Object {


    /**
     *
     */
    @Test
    public void withFmtArgs() {

        ExceptionOk exception = null;

        try {
            Exceptions.raise(
                    ExceptionOk.class,
                    "All is {0}",
                    "well");
            fail("No exception was thrown");
        } catch ( ExceptionOk e ) {
            exception = e;
        }

        assertEquals("All is well", exception.getMessage());
        assertNull(exception.getCause());
    }


    /**
     *
     */
    @Test
    public void withNoFmtArgs() {

        ExceptionOk exception = null;

        try {
            Exceptions.raise(
                    ExceptionOk.class,
                    "All is {0}");
            fail("No exception was thrown");
        } catch ( ExceptionOk e ) {
            exception = e;
        }

        assertEquals("All is {0}", exception.getMessage());
        assertNull(exception.getCause());
    }


    /**
     *
     */
    @Test
    public void withCause() {

        ExceptionOk exception = null;
        Exception cause = new RuntimeException("The Cause");

        try {
            Exceptions.raise(
                    ExceptionOk.class,
                    cause,
                    "All is {0}",
                    "well");
            fail("No exception was thrown");
        } catch ( ExceptionOk e ) {
            exception = e;
        }

        assertEquals("All is well", exception.getMessage());
        assertSame(cause, exception.getCause());
    }


    /**
     *
     */
    @Test
    public void noConstructorWithMessage() {

        TestUtils.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Exceptions.raise(
                            ExceptionNoConstructorWithMessage.class,
                            "Fail");
                });
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