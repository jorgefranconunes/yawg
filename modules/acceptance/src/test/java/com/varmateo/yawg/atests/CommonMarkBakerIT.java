/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import com.varmateo.testutils.LogStartAndEndRule;
import com.varmateo.yawg.atests.BakerCliResult;
import com.varmateo.yawg.atests.BakerCliScenario;

import static org.assertj.core.api.Assertions.assertThat;

import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 * Acceptance tests related with the "markdown" baker type.
 */
public final class CommonMarkBakerIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();


    /**
     *
     */
    @Test
    public void givenDoc_whenBakingWithoutTemplate_thenTargetHasRightContent()
            throws IOException {

        // GIVEN
        final BakerCliScenario scenario = BakerCliScenario.builder(
                CommonMarkBakerIT.class,
                "givenDoc_whenBakingWithoutTemplate_thenTargetHasRightContent")
                .build();

        // WHEN
        final BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContains("file01.html");

        assertThat(bakerResult)
                .targetContentAsString("file01.html")
                .contains("<p>Hello, world!</p>");
    }


    /**
     *
     */
    @Test
    public void givenDoc_whenBakingWithTemplate_thenTargetHasRightContent()
            throws IOException {

        // GIVEN
        final BakerCliScenario scenario = BakerCliScenario.builder(
                CommonMarkBakerIT.class,
                "givenDoc_whenBakingWithTemplate_thenTargetHasRightContent")
                .addTemplatesPath()
                .build();

        // WHEN
        final BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContains("file01.html");

        assertThat(bakerResult)
                .targetContentAsString("file01.html")
                .contains("<p>Hello, world!</p>");
    }


}
