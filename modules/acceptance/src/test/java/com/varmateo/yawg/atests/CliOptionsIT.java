/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.varmateo.yawg.YawgInfo;
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

        BakerRunner baker = BakerRunner.empty();
        BakerRunnerResult bakerResult = baker.run();

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

        BakerRunner baker =
                BakerRunner.builder()
                .addArgs("--this-is-an-unknown-option")
                .build();
        BakerRunnerResult bakerResult = baker.run();

        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputLine(1)
                .contains("unknown option");
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

        BakerRunner baker =
                BakerRunner.builder()
                .addArg(option)
                .build();
        BakerRunnerResult bakerResult = baker.run();

        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .outputLine(1)
                .contains(YawgInfo.VERSION);
    }


}
