/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.commons.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;





/**
 * Utility methods for generating exceptions.
 */
public final class Exceptions
    extends Object {


    /**
     * No instances of this class are to be created.
     */
    private Exceptions() {
    }


    /**
     * Throws an exception of the given type. The exception message is
     * constructed according to <code>java.text.MessageFormat</code>
     * rules.
     *
     * @param <T> The type of the exception to be thrown.
     *
     * @param exceptionClass The Java class of the exception to the
     * thrown.
     *
     * @param msgFmt Format for the error message, with
     * <code>java.text.MessageForma</code> conventions.
     *
     * @param fmtArgs Formating arguments used to create the error
     * message according to <code>java.text.MessageForma</code>
     * conventions.
     */
    public static <T extends RuntimeException> void
        raise(final Class<T>  exceptionClass,
              final String    msgFmt,
              final Object... fmtArgs) {

        Throwable cause = null;

        raise(exceptionClass, cause, msgFmt, fmtArgs);
    }


    /**
     * Throws an exception of the given type and with the given
     * cause. The exception message is constructed according to
     * <code>java.text.MessageFormat</code> rules.
     *
     * @param <T> The type of the exception to be thrown.
     *
     * @param exceptionClass The Java class of the exception to the
     * thrown.
     *
     * @param cause The cause to be assigned to the exception that
     * will be thrown.
     *
     * @param msgFmt Format for the error message, with
     * <code>java.text.MessageForma</code> conventions.
     *
     * @param fmtArgs Formating arguments used to create the error
     * message according to <code>java.text.MessageForma</code>
     * conventions.
     */
    public static <T extends RuntimeException> void
        raise(final Class<T>  exceptionClass,
              final Throwable cause,
              final String    msgFmt,
              final Object... fmtArgs) {

        String msg = null;

        if ( (fmtArgs==null) || (fmtArgs.length==0) ) {
            msg = msgFmt;
        } else {
            msg = MessageFormat.format(msgFmt, fmtArgs);
        }

        RuntimeException exception = newException(exceptionClass, msg, cause);

        throw exception;
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    private static <T extends Exception> T
        newException(final Class<T>  exceptionClass,
                     final String    msg,
                     final Throwable cause) {

        Constructor<T> constructor = null;

        try {
            if ( cause == null ) {
                constructor =
                    exceptionClass.getConstructor(String.class);
            } else {
                constructor =
                    exceptionClass.getConstructor(String.class,Throwable.class);
            }
        } catch ( NoSuchMethodException e ) {
            String msgFmt   = "No appropriate constructor on class {0}";
            String errorMsg =
                MessageFormat.format(msgFmt, exceptionClass.getName());
            throw new IllegalArgumentException(errorMsg, e);
        }

        T result = null;

        try {
            if ( cause == null ) {
                result = constructor.newInstance(msg);
            } else {
                result = constructor.newInstance(msg, cause);
            }
        } catch ( Exception e ) {
            String msgFmt   = "Failed to create instance of class {0}";
            String errorMsg =
                MessageFormat.format(msgFmt, exceptionClass.getName());
            throw new IllegalArgumentException(errorMsg, e);
        }

        return result;
    }





    /**
     * Builds a string with the stack trace of the given
     * exception. The string is obtained directly from
     * <code>Throwable.printStackStrace(...)</code>.
     *
     * <p>Mainly usefull for logging</p>
     *
     * @param error The exception from which the stack trace will be
     * extracted.
     *
     * @return A string with the stack trace representation of the given
     * exception.
     */
    public static String getStackTrace(final Throwable error) {

        StringWriter buffer = new StringWriter();
        PrintWriter  writer = new PrintWriter(buffer);

        error.printStackTrace(writer);

        String result = buffer.toString();

        writer.close();

        return result;
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

