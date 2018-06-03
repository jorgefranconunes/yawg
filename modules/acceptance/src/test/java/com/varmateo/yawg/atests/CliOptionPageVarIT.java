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
 * Acceptance tests related with the "--page-var" comand line option.
 */
public final class CliOptionPageVarIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();


    /**
     *
     */
    @Test
    public void givenPageVarOption_whenBaking_thenVarIsVisibleInTemplate()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                CliOptionPageVarIT.class,
                "givenPageVarOption_whenBaking_thenVarIsVisibleInTemplate")
                .addTemplatesPath()
                .addArgs("--page-var", "demoVar=Just testing...")
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
    public void givenTwoPageVarOptions_whenBaking_thenVarsAreVisibleInTemplate()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                CliOptionPageVarIT.class,
                "givenTwoPageVarOptions_whenBaking_thenVarsAreVisibleInTemplate")
                .addTemplatesPath()
                .addArgs("--page-var", "oneDemoVar=Just testing...")
                .addArgs("--page-var", "anotherDemoVar=And more testing.")
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
    public void givenPageVarOption_whenBakingAsciidocWithTemplate_thenVarIsVisibleInContent()
            throws IOException {

        final String PAGE_VAR_VALUE = "Just testing";

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                CliOptionPageVarIT.class,
                "givenPageVarOption_whenBakingAsciidocWithTemplate_thenVarIsVisibleInContent")
                .addTemplatesPath()
                .addArgs("--page-var", "oneDemoVar="+PAGE_VAR_VALUE)
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContainsExactly(
                        "file01.html");
        assertThat(bakerResult)
                .targetContentAsString("file01.html")
                .contains(PAGE_VAR_VALUE);
    }


    /**
     *
     */
    @Test
    public void givenPageVarOption_whenBakingAsciidocWithNoTemplate_thenVarIsVisibleInContent()
            throws IOException {

        final String PAGE_VAR_VALUE = "The world is my oyster";

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                CliOptionPageVarIT.class,
                "givenPageVarOption_whenBakingAsciidocWithNoTemplate_thenVarIsVisibleInContent")
                .addArgs("--page-var", "oneDemoVar="+PAGE_VAR_VALUE)
                .build();

        // WHEN
        BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContainsExactly(
                        "file01.html");
        assertThat(bakerResult)
                .targetContentAsString("file01.html")
                .contains(PAGE_VAR_VALUE);
    }


}
