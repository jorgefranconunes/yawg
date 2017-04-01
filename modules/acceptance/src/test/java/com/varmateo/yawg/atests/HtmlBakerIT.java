/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.varmateo.yawg.atests.BakerCliResult;
import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 * Acceptance tests related with the "html" baker type.
 */
public final class HtmlBakerIT {


    /**
     *
     */
    @Test
    public void givenNoTemplate_whenBakingHtmlSource_thenTargetIsCopy()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                HtmlBakerIT.class,
                "givenNoTemplate_whenBakingHtmlSource_thenTargetIsCopy")
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContainsExpectedContent(scenario);
    }


    /**
     *
     */
    @Test
    public void givenTemplate_whenBakingHtmlSource_thenTargetIsTemplated()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                HtmlBakerIT.class,
                "givenTemplate_whenBakingHtmlSource_thenTargetIsTemplated")
                .addTemplatesPath()
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContainsExpectedContent(scenario);
    }


}
