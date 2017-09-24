/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import javaslang.collection.List;
import javaslang.collection.Stream;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;

import com.varmateo.yawg.atests.BakerCliResult;


/**
 *
 */
public final class BakerCliResultAssert
        extends AbstractAssert<BakerCliResultAssert, BakerCliResult> {


    /**
     *
     */
    public static BakerCliResultAssert assertThat(
            final BakerCliResult actual) {

        return new BakerCliResultAssert(actual);
    }


    /**
     *
     */
    public BakerCliResultAssert(final BakerCliResult actual) {

        super(actual, BakerCliResultAssert.class);
    }


    /**
     *
     */
    public BakerCliResultAssert hasExitStatusSuccess() {

        isNotNull();

        if ( this.actual.getExitStatus() != 0 ) {
            failWithMessage(
                    "Expected exit status to be zero but was %d - %s",
                    this.actual.getExitStatus(),
                    this.actual.getOutputAsString());
        }

        return this;
    }


    /**
     *
     */
    public BakerCliResultAssert hasExitStatusFailure() {

        isNotNull();

        if ( this.actual.getExitStatus() == 0 ) {
            failWithMessage("Expected exit status to be non-zero but was zero");
        }

        return this;
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> outputAsString() {

        isNotNull();

        return Assertions.assertThat(this.actual.outputAsString());
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> outputLine(final int index) {

        isNotNull();

        return Assertions.assertThat(this.actual.outputLine(index));
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> outputLineFromEnd(
            final int index) {

        isNotNull();

        return Assertions.assertThat(this.actual.outputLineFromEnd(index));
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> targetContentAsString(
            final String relPath) {

        Path path = this.actual.getTargetPath().resolve(relPath);
        byte[] rawContent = readBytes(path);
        String contentAsString = new String(rawContent, StandardCharsets.UTF_8);

        return Assertions.assertThat(contentAsString);
    }


    /**
     *
     */
    private byte[] readBytes(final Path path) {

        byte[] result = null;

        try {
            result = Files.readAllBytes(path);
        } catch ( IOException e ) {
            failWithMessage(
                    "Failed to read file '%s' - '%s' - '%s'",
                    path,
                    e.getClass().getName(),
                    e.getMessage());
        }

        return result;
    }


    /**
     *
     */
    public BakerCliResultAssert targetDirContainsExactly(
            final String... fileNames) {

        isNotNull();

        List<Path> expectedFiles = List.of(fileNames).map(Paths::get);
        Path targetPath = this.actual.getTargetPath();
        List<Path> targetFiles = lsR(targetPath);

        Assertions.assertThat(targetFiles).isEqualTo(expectedFiles);

        return this;
    }


    /**
     *
     */
    public BakerCliResultAssert targetDirContains(
            final String... fileNames) {

        isNotNull();

        Path targetDir = this.actual.getTargetPath();

        List.of(fileNames)
                .map(Paths::get)
                .map(path -> targetDir.resolve(path))
                .forEach(path -> Assertions.assertThat(path).exists());

        return this;
    }


    /**
     *
     */
    public BakerCliResultAssert targetDirContainsExpectedContent(
            final BakerCliScenario scenario,
            final String... fileNames) {

        isNotNull();

        Path expectedResultsPath = scenario.getExpectedResultsPath();
        Path targetPath = this.actual.getTargetPath();

        for ( String fileName : fileNames ) {
            Path expectedFile = expectedResultsPath.resolve(fileName);
            Path actualFile = targetPath.resolve(fileName);
            Assertions.assertThat(actualFile)
                    .usingCharset(StandardCharsets.UTF_8)
                    .hasSameContentAs(expectedFile);
        }

        return this;
    }


    /**
     *
     */
    public BakerCliResultAssert targetDirContainsExpectedContent(
            final BakerCliScenario scenario) {

        isNotNull();

        Path expectedResultsPath = scenario.getExpectedResultsPath();
        Path targetPath = this.actual.getTargetPath();
        List<Path> expectedFiles = lsR(expectedResultsPath);
        List<Path> targetFiles = lsR(targetPath);

        Assertions.assertThat(expectedFiles).isEqualTo(targetFiles);

        expectedFiles.forEach(path -> {
                Path expectedFile = expectedResultsPath.resolve(path);
                Path actualFile = targetPath.resolve(path);
                Assertions.assertThat(actualFile)
                        .usingCharset(StandardCharsets.UTF_8)
                        .hasSameContentAs(expectedFile);
            });

        return this;
    }


    /**
     * Fetch sorted recursive list of files below the given directory.
     */
    private static List<Path> lsR(final Path dirPath) {

        return lsRStream(dirPath)
                .filter(Files::isRegularFile)
                .map(path -> dirPath.relativize(path))
                .sorted()
                .toList();
    }


    /**
     * This looks very ugly...
     */
    private static Stream<Path> lsRStream(final Path dirPath) {

        final java.util.stream.Stream<Path> javaStream;

        try {
            javaStream = Files.walk(dirPath);
        } catch ( IOException e ) {
            String msg =
                    String.format("Failed walkding directory \"%s\"", dirPath);
            throw new AssertionError(msg, e);
        }

        return Stream.ofAll(() -> javaStream.iterator());
    }


}
