/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.DirBaker;
import com.varmateo.yawg.DirBakerConf;
import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.util.Lists;


/**
 *
 */
public final class DirBakerTest
        extends Object {


    private static DirBakerConf _emptyConf;
    private static DirBakerConfDao _confDao;


    /**
     *
     */
    @BeforeClass
    public static void classSetUp() {

        _emptyConf = new DirBakerConf.Builder().build();
        _confDao = new DirBakerConfDao();
    }


    /**
     *
     */
    @Test
    public void bakeSomeWithConfFixed() {

        DirBakerConf conf =
                new DirBakerConf.Builder()
                .setFilesToIgnore(".*\\.txt")
                .build();

        Path sourceDir = TestUtils.getPath(DirBaker.class, "source01");
        Path targetDir = Paths.get(".");
        MockItemBaker fileBaker = new MockItemBaker();
        DirBaker baker = buildDirBaker(sourceDir, fileBaker);

        baker.bakeDirectory(conf, sourceDir, targetDir);

        List<Path> bakedFiles = fileBaker.getBakedFiles();
        List<String> expectedFiles =
                Arrays.asList(
                        "file02.adoc",
                        "file04.adoc");

        assertFileNameEquals(sourceDir, expectedFiles, bakedFiles);
    }


    /**
     *
     */
    @Test
    public void bakeSomeWithConfFromDir() {

        Path sourceDir = TestUtils.getPath(DirBaker.class, "source02");
        Path targetDir = Paths.get(".");
        MockItemBaker fileBaker = new MockItemBaker();
        DirBaker baker = buildDirBaker(sourceDir, fileBaker);

        baker.bakeDirectory(_emptyConf, sourceDir, targetDir);

        List<Path> bakedFiles = fileBaker.getBakedFiles();
        List<String> expectedFiles =
                Arrays.asList(
                        "file02.adoc",
                        "file04.adoc");

        assertFileNameEquals(sourceDir, expectedFiles, bakedFiles);
    }


    /**
     *
     */
    private DirBaker buildDirBaker(
            final Path sourceRootDir,
            final ItemBaker fileBaker) {

        Log log = LogFactory.createFor(DirBaker.class);
        DirBaker result =
                new DirBaker(
                        log,
                        sourceRootDir,
                        fileBaker,
                        Optional.empty(),
                        _confDao);
        return result;
    }


    /**
     *
     */
    private void assertFileNameEquals(
            final Path rootDir,
            final List<String> expectedRelFiles,
            final List<Path> actualFiles) {

        List<String> actualRelFiles =
                actualFiles.stream()
                .map(path -> rootDir.relativize(path))
                .map(Path::toString)
                .collect(Collectors.toList());

        assertEquals(expectedRelFiles, actualRelFiles);
    }


    /**
     * Does nothing apart from keeping a list of files submitted for
     * baking.
     */
    private static final class MockItemBaker
            extends Object
            implements ItemBaker {


        final List<Path> _bakedFiles = new ArrayList<>();


        /**
         *
         */
        @Override
        public String getShortName() {

            return "mock";
        }


        /**
         *
         */
        @Override
        public boolean isBakeable(final Path Path) {

            return true;
        }


        /**
         *
         */
        @Override
        public void bake(
                final Path sourcePath,
                final Optional<PageTemplate> template,
                final Path targetDir) {

            _bakedFiles.add(sourcePath);
        }


        /**
         *
         */
        public List<Path> getBakedFiles() {

            return _bakedFiles;
        }



    }


}