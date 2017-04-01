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
import com.varmateo.yawg.atests.BakerCliScenario;
import static com.varmateo.testutils.DirPathAssert.assertThatDir;
import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 * Acceptance tests related with the "asciidoctor" baker type.
 */
public final class AsciidoctorBakerIT {


    /**
     *
     */
    @Test
    public void givenDocWithExternalPuml_whenBakingWithouTemplate_thenTargetHasSvg()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                AsciidoctorBakerIT.class,
                "givenDocWithExternalPuml_whenBakingWithoutTemplate_thenTargetHasSvg")
                .build();

        testPuml(scenario);
    }


    /**
     *
     */
    @Test
    public void givenDocWithExternalPuml_whenBakingWithTemplate_thenTargetHasSvg()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                AsciidoctorBakerIT.class,
                "givenDocWithExternalPuml_whenBakingWithTemplate_thenTargetHasSvg")
                .addTemplatesPath()
                .build();

        testPuml(scenario);
    }


    /**
     *
     */
    @Test
    public void givenDocWithEmbededPuml_whenBakingWithouTemplate_thenTargetHasSvg()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                AsciidoctorBakerIT.class,
                "givenDocWithEmbededPuml_whenBakingWithoutTemplate_thenTargetHasSvg")
                .build();

        testPuml(scenario);
    }


    /**
     *
     */
    @Test
    public void givenDocWithEmbededPuml_whenBakingWitTemplate_thenTargetHasSvg()
            throws IOException {

        // GIVEN
        BakerCliScenario scenario = BakerCliScenario.builder(
                AsciidoctorBakerIT.class,
                "givenDocWithEmbededPuml_whenBakingWithTemplate_thenTargetHasSvg")
                .addTemplatesPath()
                .build();

        testPuml(scenario);
    }


    /**
     *
     */
    private static void testPuml(final BakerCliScenario scenario)
            throws IOException {

        // WHEN
        BakerCliResult bakerResult = scenario.run();

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
