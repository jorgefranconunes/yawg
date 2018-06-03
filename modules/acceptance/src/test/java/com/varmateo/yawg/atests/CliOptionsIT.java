/**************************************************************************
 *
 * Copyright (c) 2016-2018 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import org.junit.Rule;
import org.junit.Test;

import com.varmateo.testutils.LogStartAndEndRule;
import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.atests.BakerCliRunner;
import com.varmateo.yawg.atests.BakerCliResult;

import static org.assertj.core.api.Assertions.assertThat;

import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 *
 */
public final class CliOptionsIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();


    /**
     *
     */
    @Test
    public void noArgs() {

        BakerCliResult bakerResult = BakerCliRunner.builder().run();

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

        BakerCliResult bakerResult =
                BakerCliRunner.builder()
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

        BakerCliResult bakerResult =
                BakerCliRunner.builder()
                .addArg(option)
                .run();

        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .outputLine(1)
                .contains(YawgInfo.VERSION);
    }


}
