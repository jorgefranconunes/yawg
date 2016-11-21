/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
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

        BakerRunner baker = BakerRunner.builder().build();
        BakerRunnerResult bakerResult = baker.run();

        int actualExitStatus = bakerResult.getExitStatus();
        assertEquals(0, actualExitStatus);

        String actualFirstLine = bakerResult.outputLine(1);
        assertThat(
                "Contains version string",
                actualFirstLine,
                containsString(YawgInfo.VERSION));
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
        assertNotEquals(0, actualExitStatus);

        String actualFirstLine = bakerResult.outputLine(1);
        assertThat(
                "Contains unknown option message",
                actualFirstLine,
                containsString("unknown option"));
    }


}
