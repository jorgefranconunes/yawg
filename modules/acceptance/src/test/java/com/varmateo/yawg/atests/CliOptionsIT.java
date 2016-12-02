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

        int actualExitStatus = bakerResult.getExitStatus();
        assertThat(0).isEqualTo(actualExitStatus);

        String actualFirstLine = bakerResult.outputLine(1);
        assertThat(actualFirstLine).contains(YawgInfo.VERSION);
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

        int actualExitStatus = bakerResult.getExitStatus();
        assertThat(0).isNotEqualTo(actualExitStatus);

        String actualFirstLine = bakerResult.outputLine(1);
        assertThat(actualFirstLine).contains("unknown option");
    }


}
