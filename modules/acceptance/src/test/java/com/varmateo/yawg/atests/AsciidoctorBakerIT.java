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
 * Acceptance tests related with the "asciidoctor" baker type.
 */
public final class AsciidoctorBakerIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();


    /**
     *
     */
    @Test
    public void givenDocWithExternalPuml_whenBakingWithouTemplate_thenTargetHasSvg()
            throws IOException {

        // GIVEN
        final BakerCliScenario scenario = BakerCliScenario.builder(
                AsciidoctorBakerIT.class,
                "givenDocWithExternalPuml_whenBakingWithoutTemplate_thenTargetHasSvg")
                .build();

        verifyPlantUml(scenario);
    }


    /**
     *
     */
    @Test
    public void givenDocWithExternalPuml_whenBakingWithTemplate_thenTargetHasSvg()
            throws IOException {

        // GIVEN
        final BakerCliScenario scenario = BakerCliScenario.builder(
                AsciidoctorBakerIT.class,
                "givenDocWithExternalPuml_whenBakingWithTemplate_thenTargetHasSvg")
                .addTemplatesPath()
                .build();

        verifyPlantUml(scenario);
    }


    /**
     *
     */
    @Test
    public void givenDocWithEmbededPuml_whenBakingWithouTemplate_thenTargetHasSvg()
            throws IOException {

        // GIVEN
        final BakerCliScenario scenario = BakerCliScenario.builder(
                AsciidoctorBakerIT.class,
                "givenDocWithEmbededPuml_whenBakingWithoutTemplate_thenTargetHasSvg")
                .build();

        verifyPlantUml(scenario);
    }


    /**
     *
     */
    @Test
    public void givenDocWithEmbededPuml_whenBakingWitTemplate_thenTargetHasSvg()
            throws IOException {

        // GIVEN
        final BakerCliScenario scenario = BakerCliScenario.builder(
                AsciidoctorBakerIT.class,
                "givenDocWithEmbededPuml_whenBakingWithTemplate_thenTargetHasSvg")
                .addTemplatesPath()
                .build();

        verifyPlantUml(scenario);
    }


    /**
     *
     */
    private static void verifyPlantUml(final BakerCliScenario scenario)
            throws IOException {

        // WHEN
        final BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContains(
                        "TestDiagram.svg",
                        "file01.html");
        assertThat(bakerResult)
                .targetContentAsString("file01.html")
                .contains("src=\"TestDiagram.svg\"");
        assertThat(bakerResult)
                .targetContentAsString("TestDiagram.svg")
                .contains("MyTestClass");
    }


}
