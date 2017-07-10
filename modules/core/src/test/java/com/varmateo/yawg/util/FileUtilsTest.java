/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.varmateo.yawg.util.FileUtils;


/**
 *
 */
public final class FileUtilsTest
 {


    /**
     *
     */
    @Test
    public void withExtension() {

        String expectedBasename = "hello";
        String fileName = expectedBasename + ".txt";
        Path path = Paths.get(fileName);
        String actualBasename = FileUtils.basename(path);

        assertThat(actualBasename).isEqualTo(expectedBasename);
    }


    /**
     *
     */
    @Test
    public void withoutExtension() {

        String expectedBasename = "hello";
        String fileName = expectedBasename;
        Path path = Paths.get(fileName);
        String actualBasename = FileUtils.basename(path);

        assertThat(actualBasename).isEqualTo(expectedBasename);
    }


    /**
     *
     */
    @Test
    public void withEmptyExtension() {

        String expectedBasename = "hello";
        String fileName = expectedBasename + ".";
        Path path = Paths.get(fileName);
        String actualBasename = FileUtils.basename(path);

        assertThat(actualBasename).isEqualTo(expectedBasename);
    }


}
