/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
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
import com.varmateo.yawg.atests.BakerRunner;
import com.varmateo.yawg.atests.BakerRunnerResult;
import static com.varmateo.yawg.atests.BakerRunnerResultAssert.assertThat;


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
        Path sourceDir = TestUtils.getPath(
                HtmlBakerIT.class,
                "sourceDirWithNoTemplate");
        Path targetDir = TestUtils.newTempDir(CliOptionSourceIT.class);
        BakerRunner.Builder bakerRunnerBuilder =
                BakerRunner.builder()
                .addSourcePath(sourceDir)
                .addTargetPath(targetDir);

        // WHEN
        BakerRunnerResult bakerResult = bakerRunnerBuilder.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess();
        assertThatDir(targetDir)
                .entryNames()
                .containsExactlyInAnyOrder("file01.html");

        Path sourceHtmlFile = sourceDir.resolve("file01.html");
        Path targetHtmlFile = targetDir.resolve("file01.html");
        assertThat(targetHtmlFile)
                .usingCharset(StandardCharsets.UTF_8)
                .hasSameContentAs(sourceHtmlFile);
    }


    /**
     *
     */
    @Test
    public void givenTemplate_whenBakingHtmlSource_thenTargetIsTemplated()
            throws IOException {

        // GIVEN
        Path baseDir = TestUtils.getPath(
                HtmlBakerIT.class,
                "sourceDirWithTemplate");
        Path sourceDir = baseDir.resolve("content");
        Path targetDir = TestUtils.newTempDir(CliOptionSourceIT.class);
        Path templatesDir = baseDir.resolve("templates");
        BakerRunner.Builder bakerRunnerBuilder =
                BakerRunner.builder()
                .addSourcePath(sourceDir)
                .addTargetPath(targetDir)
                .addTemplatesPath(templatesDir);

        // WHEN
        BakerRunnerResult bakerResult = bakerRunnerBuilder.run();

        // THEN
        assertThat(bakerResult)
                .hasExitStatusSuccess();
        assertThatDir(targetDir)
                .entryNames()
                .containsExactlyInAnyOrder("file01.html");

        Path expectedHtmlFile = baseDir.resolve("file01-expected.html");
        Path targetHtmlFile = targetDir.resolve("file01.html");
        assertThat(targetHtmlFile)
                .usingCharset(StandardCharsets.UTF_8)
                .hasSameContentAs(expectedHtmlFile);
    }


}
