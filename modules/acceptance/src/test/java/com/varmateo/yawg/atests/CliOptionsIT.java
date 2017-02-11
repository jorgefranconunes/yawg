/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.atests.BakerRunner;
import com.varmateo.yawg.atests.BakerRunnerResult;
import static com.varmateo.yawg.atests.BakerRunnerResultAssert.assertThat;


/**
 *
 */
public final class CliOptionsIT {


    /**
     *
     */
    @Test
    public void noArgs() {

        BakerRunnerResult bakerResult = BakerRunner.builder().run();

        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .outputLine(1)
                .contains(YawgInfo.VERSION);
    }


    /**
     *
     */
    @Test
    public void unknownOption() {

        BakerRunnerResult bakerResult =
                BakerRunner.builder()
                .addArgs("--this-is-an-unknown-option")
                .run();

        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputLine(1)
                .contains("unknown option")
                .contains("--this-is-an-unknown-option");
    }


    /**
     *
     */
    @Test
    public void helpOption() {

        helpOptionTest("--version");
        helpOptionTest("-v");
    }


    /**
     *
     */
    private void helpOptionTest(final String option) {

        BakerRunnerResult bakerResult =
                BakerRunner.builder()
                .addArg(option)
                .run();

        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .outputLine(1)
                .contains(YawgInfo.VERSION);
    }


}
