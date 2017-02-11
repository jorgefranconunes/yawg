/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.varmateo.testutils.TestUtils;
import static com.varmateo.testutils.DirPathAssert.assertThatDir;
import com.varmateo.yawg.atests.BakerCliRunner;
import com.varmateo.yawg.atests.BakerCliResult;
import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 * Acceptance tests related with the "template" bake parameter.
 */
public final class TemplateParamIT {


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
