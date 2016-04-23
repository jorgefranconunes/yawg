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


/**
 * Utility functions intended for use in unit tests.
 */
public final class TestUtils
    extends Object {





    private static final String PROP_INPUTS_DIR_PREFIX =
            TestUtils.class.getSimpleName() + ".inputTestFilesDir";


    /**
     * No instances of this class are to be created.
     */
    private TestUtils() {
    }


    /**
     * Retrieves the value of a given system property. If the system
     * property is not set, then it will blow up.
     */
    public static String getSystemProperty(final String key) {

        String value = System.getProperty(key);

        if ( value == null ) {
            String msg =
                    MessageFormat.format(
                    "System property \"{0}\" is not defined",
                    key);
            throw new IllegalStateException(msg);
        }

        return value;
    }


    /**
     * Retrieves the directory containing the test input files for the
     * given testuite class.
     *
     * <p>By convention the path of directory with test input files is
     * obtained in the following way:</p>
     *
     * <ul>
     *
     * <li>The prefix component is obtained from the value of the
     * <code>TestUtils.inputTestFilesDir</code> system property. This
     * is usually set to <code>${basedir}/test/resources</code> in the
     * Maven project <code>pom.xml</code>.</li>
     *
     * <li>The remaining path components are obtained from the fully
     * qualified class name by replacing "." with the path separator
     * char.</li>
     *
     *</ul>
     *
     * @param testsuiteClass Class for which we will return in directory
     * with input files for the corresponding testsuite.
     */
    public static Path getInputsDir(final Class<?> testsuiteClass) {

        String inputsDirPrefix =
                getSystemProperty(PROP_INPUTS_DIR_PREFIX);
        String[] pathComponents =
            testsuiteClass.getName().split("\\.");
        Path inputsDirPath =
            Paths.get(inputsDirPrefix, pathComponents);

        return inputsDirPath;
    }


    /**
     * Retrives the path for a test file related with the given
     * testsuit class.
     *
     * <p>The <code>pathName</code> is assumed to be relative to the
     * inputs directory for the given testsuit class. See <code>{@link
     * #getInputsDir(Class)}</code> for a description on on how the
     * inputs directory is obtained.
     *
     * @param testsuiteClass 
     *
     * @param pathName A path relative to the inputs directory for the
     * given testsuite class.
     *
     * @return The path for the desired test file of the testsuite.
     */
    public static Path getPath(
            final Class<?> testsuiteClass,
            final String   pathName) {

        Path inputsDir = getInputsDir(testsuiteClass);
        Path inputPath = inputsDir.resolve(pathName);

        return inputPath;
    }


    /**
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
     */
    public static <T extends Throwable> T assertThrows(
            final Class<T> expectedExceptionClass,
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
                String msg =
                        MessageFormat.format(
                                "Expected exception {0} but got {1}",
                                expectedExceptionClass.getName(),
                                actualException.getClass().getName());
                throw new IllegalStateException(msg, actualException);
            }
        }

        if ( result == null ) {
            String msg =
                    MessageFormat.format(
                            "Failed to throw expected exception {0}",
                            expectedExceptionClass.getName());
            Assert.fail(msg);
        }

        return result;
    }


    /**
     * A runnable simile that throws a checked exception. Used for
     * simplifying the use of lambdas when calling <code>{@link
     * #assertThrows(Class, TestTask)}</code>.
     */
    @FunctionalInterface
    public interface TestTask {


        /**
         * @throws Exception Checked exception.
         */
        void run() throws Exception;
    }


}
