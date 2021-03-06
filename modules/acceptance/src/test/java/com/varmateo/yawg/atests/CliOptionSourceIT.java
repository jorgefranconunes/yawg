/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;

import com.varmateo.testutils.LogStartAndEndRule;
import com.varmateo.testutils.TestUtils;
import com.varmateo.yawg.atests.BakerCliRunner;
import com.varmateo.yawg.atests.BakerCliResult;

import static org.assertj.core.api.Assertions.assertThat;

import static com.varmateo.testutils.DirPathAssert.assertThatDir;
import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 * Acceptance tests related with the --source command line option.
 */
public final class CliOptionSourceIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();


    /**
     *
     */
    @Test
    public void sourceWithNoTarget()
            throws IOException {

        final Path sourcePath = TestUtils.newTempDir(CliOptionSourceIT.class);
        final BakerCliResult bakerResult = BakerCliRunner.builder()
                .addSourcePath(sourcePath)
                .run();

        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputLine(1)
                .containsIgnoringCase("missing mandatory option")
                .contains("--target");
    }


    /**
     *
     */
    @Test
    public void sourceNonExisting()
            throws IOException {

        final Path sourcePath = Paths.get("this-directory-does-not-exist-for-sure");
        final Path targetPath = TestUtils.newTempDir(CliOptionSourceIT.class);
        final BakerCliResult bakerResult = BakerCliRunner.builder()
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

        final BakerCliResult bakerResult = BakerCliRunner.builder()
                .addArg("--source")
                .run();

        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputLineFromEnd(1)
                .containsIgnoringCase("argument for option --source is missing");
    }


    /**
     *
     */
    @Test
    public void givenSourceDirIsEmpty_whenBake_thenTargetDirIsEmpty()
            throws IOException {

        // GIVEN
        final Path sourceDirParent = TestUtils.newTempDir(CliOptionSourceIT.class);
        final Path sourceDir = sourceDirParent.resolve("emptySourceDir");
        Files.createDirectory(sourceDir);

        // WHEN
        final Path targetDir = TestUtils.newTempDir(CliOptionSourceIT.class);
        final BakerCliResult bakerResult = BakerCliRunner.builder()
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
        final BakerCliScenario scenario = BakerCliScenario.builder(
                CliOptionSourceIT.class,
                "givenSourceDirHasEntries_whenBake_thenTargetDirHasSameEntries")
                .build();

        // WHEN
        final BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContainsExactly(
                        "file01.txt",
                        "file02.txt");
    }


}
