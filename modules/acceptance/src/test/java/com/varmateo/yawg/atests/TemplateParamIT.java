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
 * Acceptance tests related with the "template" bake parameter.
 */
public final class TemplateParamIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();


    /**
     *
     */
    @Test
    public void givenEmptyTemplatesDir_whenBaking_thenErrorDueToMissingDefaultTemplate()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                TemplateParamIT.class,
                "givenEmptyTemplatesDir_whenBaking_thenErrorDueToMissingDefaultTemplate")
                .addTemplatesPath()
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputAsString()
                .contains("TemplateNotFoundException");
    }


    /**
     *
     */
    @Test
    public void givenTemplatesDirWithoutTemplateParam_whenBaking_thenDefaultTemplateIsUsed()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                TemplateParamIT.class,
                "givenTemplatesDirWithoutTemplateParam_whenBaking_thenDefaultTemplateIsUsed")
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
