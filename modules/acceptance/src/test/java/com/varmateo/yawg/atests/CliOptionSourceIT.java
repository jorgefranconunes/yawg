/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.varmateo.testutils.TestUtils;
import static com.varmateo.testutils.DirPathAssert.assertThatDir;
import com.varmateo.yawg.atests.BakerRunner;
import com.varmateo.yawg.atests.BakerRunnerResult;
import static com.varmateo.yawg.atests.BakerRunnerResultAssert.assertThat;


/**
 * Acceptance tests related with the --source command line option.
 */
public final class CliOptionSourceIT {


    /**
     *
     */
    @Test
    public void sourceWithNoTarget()
            throws IOException {

        Path sourcePath = TestUtils.newTempDir(CliOptionSourceIT.class);
        BakerRunnerResult bakerResult =
                BakerRunner.builder()
                .addSourcePath(sourcePath)
                .run();

        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputLine(1)
                .contains("missing mandatory option")
                .contains("--target");
    }


    /**
     *
     */
    @Test
    public void sourceNonExisting()
            throws IOException {

        Path sourcePath = Paths.get("this-directory-does-not-exist-for-sure");
        Path targetPath = TestUtils.newTempDir(CliOptionSourceIT.class);
        BakerRunnerResult bakerResult =
                BakerRunner.builder()
                .addSourcePath(sourcePath)
                .addTargetPath(targetPath)
                .run();

        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputLineFromEnd(1)
                .contains("NoSuchFileException");
    }


    /**
     *
     */
    @Test
    public void sourceMissingValue()
            throws IOException {

        BakerRunnerResult bakerResult =
                BakerRunner.builder()
                .addArg("--source")
                .run();

        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputLineFromEnd(1)
                .contains("argument for option --source is missing");
    }


    /**
     *
     */
    @Test
    public void givenSourceDirIsEmpty_whenBake_thenTargetDirIsEmpty()
            throws IOException {

        // GIVEN
        Path sourceDirParent = TestUtils.newTempDir(CliOptionSourceIT.class);
        Path sourceDir = sourceDirParent.resolve("emptySourceDir");
        Files.createDirectory(sourceDir);

        // WHEN
        Path targetDir = TestUtils.newTempDir(CliOptionSourceIT.class);
        BakerRunnerResult bakerResult =
                BakerRunner.builder()
                .addSourcePath(sourceDir)
                .addTargetPath(targetDir)
                .run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess();
        assertThatDir(targetDir)
                .isEmptyDirectory();
    }


    /**
     *
     */
    @Test
    public void givenSourceDirHasEntries_whenBake_thenTargetDirHasSameEntries()
            throws IOException {

        // GIVEN
        Path sourceDir = TestUtils.getPath(
                CliOptionSourceIT.class,
                "sourceDirWithEntries");

        // WHEN
        Path targetDir = TestUtils.newTempDir(CliOptionSourceIT.class);
        BakerRunnerResult bakerResult =
                BakerRunner.builder()
                .addSourcePath(sourceDir)
                .addTargetPath(targetDir)
                .run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess();
        assertThatDir(targetDir)
                .entryNames()
                .containsExactlyInAnyOrder(
                        "file01.txt",
                        "file02.txt");
    }


}
