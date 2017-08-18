/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import static com.varmateo.testutils.DirPathAssert.assertThatDir;
import com.varmateo.yawg.atests.BakerCliResult;
import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 * Acceptance tests related with the "excludeHere" bake parameter.
 */
public final class ExcludeHereParamIT {


    /**
     *
     */
    @Test
    public void givenExcludeHereParam_whenBaking_thenFilesAreExcludedOnlyInDir()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                ExcludeHereParamIT.class,
                "givenExcludeHereParam_whenBaking_thenFilesAreExcludedOnlyInDir")
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContainsExpectedContent(scenario);
    }


}
