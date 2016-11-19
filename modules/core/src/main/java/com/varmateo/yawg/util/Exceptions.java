/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;

import com.varmateo.yawg.YawgException;





/**
 * Utility methods for generating exceptions.
 */
public final class Exceptions {


    /**
     * No instances of this class are to be created.
     */
    private Exceptions() {
    }


    /**
     *
     */
    public static void raise(
            final Throwable cause,
            final String    msgFmt,
            final Object... fmtArgs)
            throws YawgException {

        Exceptions.raise(YawgException.class, cause, msgFmt, fmtArgs);
    }


    /**
     *
     */
    public static void raise(
            final String    msgFmt,
            final Object... fmtArgs)
            throws YawgException {

        Exceptions.raise(YawgException.class, msgFmt, fmtArgs);
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

        RuntimeException exception =
            newException(exceptionClass, cause, msgFmt, fmtArgs);

        throw exception;
    }





    /**
     *
     * Throws a checked exception of the given type and with the given
     * cause. The exception message is constructed according to
     * <code>java.text.MessageFormat</code> rules.
     *
     * @param <T> The type of the checked exception to be thrown.
     *
     * @param exceptionClass The Java class of the exception to the
     * thrown.
     *
     * @param msgFmt Format for the error message, with
     * <code>java.text.MessageForma</code> conventions.
     *
     * @param fmtArgs Formating arguments used to create the error message
     * according to <code>java.text.MessageForma</code> conventions.
     *
     * @throws T The checked exception thrown by this method.
     */
    public static <T extends Exception> void
        raiseChecked(final Class<T>  exceptionClass,
                     final String    msgFmt,
                     final Object... fmtArgs)
        throws T {

        Throwable cause = null;

        raiseChecked(exceptionClass, cause, msgFmt, fmtArgs);
    }


    /**
     * Throws a checked exception of the given type and with the given
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
     *
     * @throws T A checked exception type.
     */
    public static <T extends Exception> void
        raiseChecked(final Class<T>  exceptionClass,
                     final Throwable cause,
                     final String    msgFmt,
                     final Object... fmtArgs)
        throws T {

        T exception = newException(exceptionClass, cause, msgFmt, fmtArgs);

        throw exception;
    }


    /**
     *
     */
    private static <T extends Exception> T
        newException(final Class<T>  exceptionClass,
                     final Throwable cause,
                     final String msgFmt,
                     final Object... fmtArgs) {

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
            String fmt   = "No appropriate constructor on class {0}";
            String errorMsg =
                MessageFormat.format(fmt, exceptionClass.getName());
            throw new IllegalArgumentException(errorMsg, e);
        }

        T result = null;
        String msg = null;

        if ( (fmtArgs==null) || (fmtArgs.length==0) ) {
            msg = msgFmt;
        } else {
            msg = MessageFormat.format(msgFmt, fmtArgs);
        }

        try {
            if ( cause == null ) {
                result = constructor.newInstance(msg);
            } else {
                result = constructor.newInstance(msg, cause);
            }
        } catch ( ReflectiveOperationException e ) {
            String fmt   = "Failed to create instance of class {0}";
            String errorMsg =
                MessageFormat.format(fmt, exceptionClass.getName());
            throw new IllegalArgumentException(errorMsg, e.getCause());
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
