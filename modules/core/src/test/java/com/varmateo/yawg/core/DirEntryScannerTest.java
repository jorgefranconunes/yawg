/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Path;

import javaslang.collection.List;
import javaslang.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.DirEntryScanner;


/**
 *
 */
public final class DirEntryScannerTest {


    /**
     *
     */
    @Test
    public void allFiles()
            throws IOException {

        DirBakerConf conf = DirBakerConf.empty();
        Path dirPath = TestUtils.getInputsDir(DirEntryScanner.class);
        DirEntryScanner scanner = new DirEntryScanner(conf);
        Seq<Path> actualEntries = scanner.getDirEntries(dirPath);
        Seq<String> expectedEntries =
                List.of(
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
                .setFilesToExclude("*.txt", "*.html")
                .build();
        Path dirPath = TestUtils.getInputsDir(DirEntryScanner.class);
        DirEntryScanner scanner = new DirEntryScanner(conf);
        Seq<Path> actualEntries = scanner.getDirEntries(dirPath);
        Seq<String> expectedEntries =
                List.of(
                        "file02.adoc",
                        "file04.adoc");

        assertFilenameEquals(expectedEntries, actualEntries);
    }


    /**
     *
     */
    private void assertFilenameEquals(
            final Seq<String> expectedNames,
            final Seq<Path> actualEntries) {

        Seq<String> actualNames = actualEntries
                .map(Path::getFileName)
                .map(Object::toString);

        assertThat(actualNames).isEqualTo(expectedNames);
    }


}