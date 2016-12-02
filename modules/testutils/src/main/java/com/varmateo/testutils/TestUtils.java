/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.testutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;


/**
 * Utility functions intended for use in unit tests.
 */
public final class TestUtils {


    // Name of system property with the path of the base directory containting input files
    // to be used in testing.
    private static final String PROP_INPUTS_DIR_PREFIX =
            TestUtils.class.getSimpleName() + ".inputTestFilesDir";

    // Name of system property with the path of the directory to be used for creating
    // temporary files during testing.
    private static final String PROP_TMP_DIR_PREFIX =
            TestUtils.class.getSimpleName() + ".tmpTestFilesDir";


    /**
     * No instances of this class are to be created.
     */
    private TestUtils() {
    }


    /**
     * Retrieves the value of a given system property. If the system
     * property is not set, then it will blow up.
     *
     * @param key The name of the system property whose value is to be
     * returned.
     *
     * @return The value of the given system property.
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
     * given testsuite class.
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
     *
     * @return The path for the directory continaining the test input
     * files for the given testsuite class.
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
     * Retrieves the directory to be used as temporary directory for
     * the given testsuite class.
     *
     * <p>The temporary directory will be created under the path
     * specified by the <code>TestUtils.tmpTestFilesDir</code> system
     * property. The base name of the temporary directory is generated
     * automatically.</p>
     *
     * <p>The returned path will be different every time this method
     * is called. This method is intended to be called from within
     * test cases. Each test case should call this method to have its
     * down dedicated temporary directory.</p>
     *
     * <p>When this method returns, the temporary directory is sure to
     * exist.</p>
     *
     * @param testSuiteClass The Java class of the testsuite for
     * which we are to return its dedicated temporary directory.
     *
     * @return The path to a newly created temporary directory.
     *
     * @throws IOException If there were any problems creating the
     * temporary directory.
     */
    public static Path getTmpDir(final Class<?> testSuiteClass)
            throws IOException {

        String tmpDirRoot =
                getSystemProperty(PROP_TMP_DIR_PREFIX);
        String[] pathComponents =
            testSuiteClass.getName().split("\\.");
        Path tmpDirParent =
                Paths.get(tmpDirRoot, pathComponents);

        Files.createDirectories(tmpDirParent);

        Path tmpDir =
                Files.createTempDirectory(tmpDirParent, "tmp");

        return tmpDir;
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
     * @param testSuiteClass 
     *
     * @param pathName A path relative to the inputs directory for the
     * given testsuite class.
     *
     * @return The path for the desired test file of the testsuite.
     */
    public static Path getPath(
            final Class<?> testSuiteClass,
            final String   pathName) {

        Path inputsDir = getInputsDir(testSuiteClass);
        Path inputPath = inputsDir.resolve(pathName);

        return inputPath;
    }


    /**
     * Checks that the given task does throw the expected exception.
     *
     * <p>The task is executed. If it throws an exception of the given
     * type, then this method just returns the exception object.</p>
     *
     * <p>If the task throws no exception, a
     * <code>TestUtils.ExceptionMissingException</code> will be
     * thrown.</p>
     *
     * <p>If the task throws an exception of a type not compatible
     * with the given type, then an
     * <code>TestUtils.UnexpectedExceptionTypeException</code> will be
     * thrown.
     *
     * @param <T> The type of exception the task is expected to throw.
     *
     * @param expectedExceptionClass The class of the exception the task
     * is expected to throw. This can be wither a checked or an unchecked
     * exception.
     *
     * @param task The runnable that will be excuted,
     *
     * @return The exception thrown during the execution of the given
     * task.
     *
     * @throws ExceptionMissingException When the task throws no
     * exception.
     *
     * @throws UnexpectedExceptionTypeException When the task throws
     * an exception, but it is of a type not compatible with
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
                UnexpectedExceptionTypeException.raise(
                        expectedExceptionClass,
                        actualException);
            }
        }

        if ( result == null ) {
            ExceptionMissingException.raise(expectedExceptionClass);
        }

        return result;
    }


    /**
     * Signals that the wrong type of exception was thrown, when
     * another exception type was expected.
     */
    public static final class UnexpectedExceptionTypeException
            extends IllegalStateException {


        /**
         *
         */
        private UnexpectedExceptionTypeException(
                final String message,
                final Throwable cause) {
            super(message, cause);
        }


        /**
         * Creates a new exception and throws it.
         *
         * @param expectedExceptionType The type of exception that was
         * being expected.
         *
         * @param actualException The actual exception instance that
         * was caught.
         */
        public static void raise(
                final Class<? extends Throwable> expectedExceptionType,
                final Throwable actualException) {

            String msg =
                    MessageFormat.format(
                            "Expected exception {0} but got {1}",
                            expectedExceptionType.getName(),
                            actualException.getClass().getName());

            throw new UnexpectedExceptionTypeException(msg, actualException);
        }


    }


    /**
     * Signals that an exception should have been thrown, but none was.
     */
    public static final class ExceptionMissingException
            extends IllegalStateException {


        /**
         *
         */
        private ExceptionMissingException(final String message) {
            super(message);
        }


        /**
         * @param expectedExceptionType The type of exception that was
         * being exected.
         */
        public static void raise(
                final Class<? extends Throwable> expectedExceptionType) {

            String msg =
                    MessageFormat.format(
                            "Failed to throw expected exception {0}",
                            expectedExceptionType.getName());

            throw new ExceptionMissingException(msg);
        }


    }


    /**
     * A runnable simile that throws a checked exception. Intended for
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
