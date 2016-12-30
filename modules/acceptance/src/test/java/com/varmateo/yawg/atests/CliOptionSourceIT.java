/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.varmateo.testutils.TestUtils;
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

        Path sourcePath = TestUtils.getTmpDir(CliOptionSourceIT.class);
        BakerRunnerResult bakerResult =
                BakerRunner.builder()
                .addSourcePath(sourcePath)
                .run();

        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputLine(1)
                .contains("missing mandatory option");
    }


    /**
     *
     */
    @Test
    public void sourceNonExisting()
            throws IOException {

        Path sourcePath = Paths.get("this-directory-does-not-exist-for-sure");
        Path targetPath = TestUtils.getTmpDir(CliOptionSourceIT.class);
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


}
