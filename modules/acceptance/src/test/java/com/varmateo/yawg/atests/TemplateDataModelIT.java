/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;

import com.varmateo.testutils.LogStartAndEndRule;
import com.varmateo.yawg.atests.BakerCliResult;
import com.varmateo.yawg.atests.BakerCliScenario;

import static org.assertj.core.api.Assertions.assertThat;

import static com.varmateo.yawg.atests.BakerCliResultAssert.assertThat;


/**
 * 
 */
public final class TemplateDataModelIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();


    /**
     *
     */
    @Test
    public void givenDocs_whenBakingOnce_thenBakeIdIsTheSameInBoth()
            throws IOException {

        // GIVEN
        final BakerCliScenario scenario = BakerCliScenario.builder(
                TemplateDataModelIT.class,
                "givenDocs_whenBakingOnce_thenBakeIdIsTheSameInBoth")
                .addTemplatesPath()
                .build();

        // WHEN
        final BakerCliResult bakerResult = scenario.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess()
                .targetDirContains(
                        "file01.html",
                        "file02.html");

        final Path target01 = bakerResult.targetPath().resolve("file01.html");
        final String line01 = Files.readAllLines(target01, StandardCharsets.UTF_8).get(0);

        final Path target02 = bakerResult.targetPath().resolve("file02.html");
        final String line02 = Files.readAllLines(target02, StandardCharsets.UTF_8).get(0);

        assertThat(line01)
                .isEqualTo(line02);
    }


    /**
     *
     */
    @Test
    public void givenDoc_whenBakingTwice_thenBakeIdIsDifferent()
            throws IOException {

        // GIVEN
        final BakerCliScenario scenario01 = BakerCliScenario.builder(
                TemplateDataModelIT.class,
                "givenDoc_whenBakingTwice_thenBakeIdIsDifferent")
                .addTemplatesPath()
                .build();
        final BakerCliScenario scenario02 = BakerCliScenario.builder(
                TemplateDataModelIT.class,
                "givenDoc_whenBakingTwice_thenBakeIdIsDifferent")
                .addTemplatesPath()
                .build();

        // WHEN
        final BakerCliResult bakerResult01 = scenario01.run();
        final BakerCliResult bakerResult02 = scenario02.run();

        // THEN
        assertThat(bakerResult01)
                .hasExitStatusSuccess()
                .targetDirContains("file01.html");
        assertThat(bakerResult02)
                .hasExitStatusSuccess()
                .targetDirContains("file01.html");

        final Path target01 = bakerResult01.targetPath().resolve("file01.html");
        final String line01 = Files.readAllLines(target01, StandardCharsets.UTF_8).get(0);
        assertThat(line01)
                .startsWith("Bake ID:");

        final Path target02 = bakerResult02.targetPath().resolve("file01.html");
        final String line02 = Files.readAllLines(target02, StandardCharsets.UTF_8).get(0);
        assertThat(line02)
                .startsWith("Bake ID:");

        assertThat(line01)
                .isNotEqualTo(line02);
    }


}
