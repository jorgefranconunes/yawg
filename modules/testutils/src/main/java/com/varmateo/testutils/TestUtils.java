/**************************************************************************
 *
 * Copyright (c) 2016-2018 Yawg project contributors.
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


    // Name of system property with the path of the base directory
    // containting input files to be used in testing.
    private static final String PROP_INPUTS_DIR_PREFIX =
            TestUtils.class.getSimpleName() + ".inputTestFilesDir";

    // Name of system property with the path of the directory to be
    // used for creating temporary files during testing.
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
    public static Path newTempDir(final Class<?> testSuiteClass)
            throws IOException {

        String tmpDirRoot = getSystemProperty(PROP_TMP_DIR_PREFIX);
        String[] pathComponents = testSuiteClass.getName().split("\\.");
        Path tmpDirParent = Paths.get(tmpDirRoot, pathComponents);

        Files.createDirectories(tmpDirParent);

        return Files.createTempDirectory(tmpDirParent, "tmp");
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

        return inputsDir.resolve(pathName);
    }


}
