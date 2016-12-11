/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.testutils;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.varmateo.testutils.TestUtils;


/**
 *
 */
public final class TestUtilsTest
 {


    /**
     *
     */
    @Test
    public void checkExistingProperty() {

        String value = TestUtils.getSystemProperty("java.version");

        assertThat(value).isNotNull();
    }


    /**
     *
     */
    @Test
    public void checkNonExistingProperty() {

        assertThatThrownBy(
                () -> TestUtils.getSystemProperty(
                        "this.property.does.not.exist.for.sure"))
                .isInstanceOf(IllegalStateException.class);
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

        assertThat(actualPath).isEqualTo(expectedPath);
    }


}
