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
import com.varmateo.yawg.atests.BakerRunner;
import com.varmateo.yawg.atests.BakerRunnerResult;
import static com.varmateo.yawg.atests.BakerRunnerResultAssert.assertThat;


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
        Path sourceDir = TestUtils.getPath(
                TemplateParamIT.class,
                "sourceDirWithNoTemplateParam");
        Path targetDir = TestUtils.newTempDir(TemplateParamIT.class);
        Path emptyDir = TestUtils.newTempDir(TemplateParamIT.class);
        BakerRunner.Builder bakerRunnerBuilder =
                BakerRunner.builder()
                .addSourcePath(sourceDir)
                .addTargetPath(targetDir)
                .addTemplatesPath(emptyDir);

        // WHEN
        BakerRunnerResult bakerResult = bakerRunnerBuilder.run();

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
        Path baseDir = TestUtils.getPath(
                TemplateParamIT.class,
                "sourceDirWithDefaultTemplate");
        Path sourceDir = baseDir.resolve("content");
        Path targetDir = TestUtils.newTempDir(TemplateParamIT.class);
        Path templatesDir = baseDir.resolve("templates");
        BakerRunner.Builder bakerRunnerBuilder =
                BakerRunner.builder()
                .addSourcePath(sourceDir)
                .addTargetPath(targetDir)
                .addTemplatesPath(templatesDir);

        // WHEN
        BakerRunnerResult bakerResult = bakerRunnerBuilder.run();

        // THEN
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
