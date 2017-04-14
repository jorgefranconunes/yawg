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
import com.varmateo.yawg.atests.BakerCliRunner;
import com.varmateo.yawg.atests.BakerCliResult;
import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 * Acceptance tests related with the "template" bake parameter.
 */
public final class CliOptionAssetsIT {


    /**
     *
     */
    @Test
    public void givenAssetsDirIsMissing_whenBaking_thenBakeFails()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                CliOptionAssetsIT.class,
                "givenAssetsDirIsMissing_whenBaking_thenBakeFails")
                .addAssetsPath()
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusFailure()
                .outputLineFromEnd(1)
                .contains("Failed to copy assets");
    }


    /**
     *
     */
    @Test
    public void givenAssetsDir_whenBaking_thenAssetsAreCopiedToTarget()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                CliOptionAssetsIT.class,
                "givenAssetsDir_whenBaking_thenAssetsAreCopiedToTarget")
                .addAssetsPath()
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContainsExpectedContent(scenario);
    }


}
