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
import com.varmateo.yawg.atests.BakerCliRunner;
import com.varmateo.yawg.atests.BakerCliResult;
import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


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
        BakerCliResult bakerResult =
                BakerCliRunner.builder()
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
        BakerCliResult bakerResult =
                BakerCliRunner.builder()
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

        BakerCliResult bakerResult =
                BakerCliRunner.builder()
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
        BakerCliResult bakerResult =
                BakerCliRunner.builder()
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
        BakerCliScenario scenario = BakerCliScenario.builder(
                CliOptionSourceIT.class,
                "givenSourceDirHasEntries_whenBake_thenTargetDirHasSameEntries")
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContainsExactly(
                        "file01.txt",
                        "file02.txt");
    }


}
