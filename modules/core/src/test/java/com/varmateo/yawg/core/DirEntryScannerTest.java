/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.DirEntryScanner;
import com.varmateo.yawg.util.Lists;


/**
 *
 */
public final class DirEntryScannerTest
        extends Object {


    /**
     *
     */
    @Test
    public void allFiles()
            throws IOException {

        DirBakerConf conf = DirBakerConf.builder().build();
        Path dirPath = TestUtils.getInputsDir(DirEntryScanner.class);
        DirEntryScanner scanner = new DirEntryScanner(conf);
        List<Path> actualEntries = scanner.getDirEntries(dirPath);
        List<String> expectedEntries =
                Arrays.asList(
                        "file01.txt",
                        "file02.adoc",
                        "file03.txt",
                        "file04.adoc",
                        "file05.html");

        assertFilenameEquals(expectedEntries, actualEntries);
    }


    /**
     *
     */
    @Test
    public void someFiles()
            throws IOException {

        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToIgnore("*.txt", "*.html")
                .build();
        Path dirPath = TestUtils.getInputsDir(DirEntryScanner.class);
        DirEntryScanner scanner = new DirEntryScanner(conf);
        List<Path> actualEntries = scanner.getDirEntries(dirPath);
        List<String> expectedEntries =
                Arrays.asList(
                        "file02.adoc",
                        "file04.adoc");

        assertFilenameEquals(expectedEntries, actualEntries);
    }


    /**
     *
     */
    private void assertFilenameEquals(
            final List<String> expectedNames,
            final List<Path> actualEntries) {

        List<String> actualNames =
                Lists.map(actualEntries, p -> p.getFileName().toString());

        assertEquals(actualNames, expectedNames);
    }


}
