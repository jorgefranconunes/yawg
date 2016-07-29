/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.testutils;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;


/**
 *
 */
public final class TestUtilsTest
        extends Object {


    /**
     *
     */
    @Test
    public void checkExistingProperty() {

        String value = TestUtils.getSystemProperty("java.version");

        assertNotNull(value);
    }


    /**
     *
     */
    @Test
    public void checkNonExistingProperty() {

        boolean isOk = false;

        try {
            TestUtils.getSystemProperty(
                    "this.property.does.not.exist.for.sure");

            // Uh, oh... We should not be able to get here...
            isOk = false;
        } catch ( IllegalStateException e ) {
            // Cool. Everything went as expected.
            isOk = true;
        }

        assertTrue(isOk);
    }


    /**
     *
     */
    @Test
    public void checkInputsDir() {

        String expectedBasePath =
                TestUtils.getSystemProperty("TestUtils.inputTestFilesDir");
        Path expectedPath =
                Paths.get(expectedBasePath,
                          "com",
                          "varmateo",
                          "testutils",
                          "TestUtils");
        Path actualPath =
                TestUtils.getInputsDir(TestUtils.class);

        assertEquals(expectedPath, actualPath);
    }


    /**
     *
     */
    @Test
    public void checkAssertThrowsRightException()  {

        MyException expectedException = new MyException();
        MyException actualException =
                TestUtils.assertThrows(
                        MyException.class,
                        () -> { throw expectedException; });

        assertSame(expectedException, actualException);
    }


    /**
     *
     */
    @Test(expected = TestUtils.ExceptionMissingException.class)
    public void checkAssertThrowsNoException() {

        TestUtils.assertThrows(
                MyException.class,
                () -> { /* We do nothing. */ });
    }


    /**
     *
     */
    @Test(expected = TestUtils.UnexpectedExceptionTypeException.class)
    public void checkAssertThrowsWrongException() {

        TestUtils.assertThrows(
                MyException.class,
                () -> { throw new NumberFormatException(); });
    }


    /**
     *
     */
    private static final class MyException
            extends Exception {
    }


}
