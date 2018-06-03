/**************************************************************************
 *
 * Copyright (c) 2017-2018 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import com.varmateo.testutils.LogStartAndEndRule;
import com.varmateo.yawg.atests.BakerCliResult;

import static org.assertj.core.api.Assertions.assertThat;

import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 * Acceptance tests related with the "extraDirsHere" baking parameter.
 */
public final class ExtraDirsHereParamIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();


    /**
     *
     */
    @Test
    public void givenExtraDirsHereParam_whenBaking_thenExtraContentsArePresent()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                ExtraDirsHereParamIT.class,
                "givenExtraDirsHereParam_whenBaking_thenExtraContentsArePresent")
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContainsExpectedContent(scenario);
    }


}
