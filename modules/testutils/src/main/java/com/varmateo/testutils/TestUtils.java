/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.testutils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

import org.junit.Assert;





/**************************************************************************
 *
 * Utility functions intended for use in unit tests.
 *
 **************************************************************************/

public final class TestUtils
    extends Object {





/**************************************************************************
 *
 * No instances of this class are to be created.
 *
 **************************************************************************/

    private TestUtils() {
    }





/**************************************************************************
 *
 * Checks that the given task does throw the expected exception.
 *
 * <p>The task is executed. If it throws an exception of the given
 * type, then this method just returns the exception object.</p>
 *
 * <p>If the task throws no exception, then JUnits
 * <code>Assert.fail()</code> will be called, causing the JUnit test
 * invoking this method to fail</p>
 *
 * <p>If the task throws an exception of a type not compatible with
 * the given type, then an
 * <code>java.lang.IllegalStateException</code> will be thrown.
 *
 * @param <T> The type of exception the task is expected to throw.
 *
 * @param expectedExceptionClass The class of the exception the task
 * is expected to throw. This can be wither a checked or an unchecked
 * exception.
 *
 * @param task The runnable that will be excuted,
 *
 * @return The exception thrown during the execution of the given task.
 *
 * @throws IllegalStateException When the task throws an exception,
 * but it is of a type not compatible with
 * <code>expectedExceptionClass</code>.
 *
 **************************************************************************/

    public static <T extends Throwable> T
        assertThrows(final Class<T> expectedExceptionClass,
                     final TestTask task) {

        T result = null;

        try {
            task.run();
        } catch ( Throwable actualException ) {
            if ( expectedExceptionClass.isInstance(actualException) ) {
                result = (T)actualException;
            } else {
                // Uh, oh... The wrong exception was thrown. Let us
                // blow up with yet another one!
                String msgFmt = "Expected exception {0} but got {1}";
                Object[] fmtArgs = {
                    expectedExceptionClass.getName(),
                    actualException.getClass().getName()
                };
                String msg = MessageFormat.format(msgFmt, fmtArgs);

                throw new IllegalStateException(msg, actualException);
            }
        }

        if ( result == null ) {
            String msgFmt = "Failed to throw expected exception ";
            Object[] fmtArgs = { expectedExceptionClass.getName() };
            String msg = MessageFormat.format(msgFmt, fmtArgs);

            Assert.fail(msg);
        }

        return result;
    }





/**************************************************************************
 *
 * A runnable simile that throws a checked exception. Used for
 * simplifying the use of lambdas when calling <code>{@link
 * #assertThrows(Class, TestTask)}</code>.
 *
 **************************************************************************/

    @FunctionalInterface
    public static interface TestTask {





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

        void run() throws Exception;
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

