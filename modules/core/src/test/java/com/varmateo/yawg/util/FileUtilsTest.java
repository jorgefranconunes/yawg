/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.yawg.util.FileUtils;


/**
 *
 */
public final class FileUtilsTest
        extends Object {


    /**
     *
     */
    @Test
    public void withExtension() {

        String expectedBasename = "hello";
        String fileName = expectedBasename + ".txt";
        Path path = Paths.get(fileName);
        String actualBasename = FileUtils.basename(path);

        assertEquals(expectedBasename, actualBasename);
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

        assertEquals(expectedBasename, actualBasename);
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

        assertEquals(expectedBasename, actualBasename);
    }


}