/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.atests.BakerCliRunner;
import com.varmateo.yawg.atests.BakerCliResult;
import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 *
 */
public final class CliOptionsIT {


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
