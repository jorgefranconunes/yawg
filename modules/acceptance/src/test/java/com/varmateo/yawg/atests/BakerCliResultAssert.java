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

        if ( actual.getExitStatus() != 0 ) {
            failWithMessage(
                    "Expected exit status to be zero but was %d - %s",
                    actual.getExitStatus(),
                    actual.getOutputAsString());
        }

        return this;
    }


    /**
     *
     */
    public BakerCliResultAssert hasExitStatusFailure() {

        isNotNull();

        if ( actual.getExitStatus() == 0 ) {
            failWithMessage("Expected exit status to be non-zero but was zero");
        }

        return this;
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> outputAsString() {

        isNotNull();

        return Assertions.assertThat(actual.outputAsString());
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> outputLine(final int index) {

        isNotNull();

        return Assertions.assertThat(actual.outputLine(index));
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> outputLineFromEnd(
            final int index) {

        isNotNull();

        return Assertions.assertThat(actual.outputLineFromEnd(index));
    }


    /**
     *
     */
    public BakerCliResultAssert targetDirContainsExactly(
            final String... fileNames) {

        isNotNull();

        List<Path> expectedFiles = List.of(fileNames).map(Paths::get);
        Path targetPath = actual.getTargetPath();
        List<Path> targetFiles = lsR(targetPath);

        Assertions.assertThat(expectedFiles).isEqualTo(targetFiles);

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
        Path targetPath = actual.getTargetPath();

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
        Path targetPath = actual.getTargetPath();
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
            throw new RuntimeException(e);
        }

        return Stream.ofAll(
                new Iterable<Path>() {
                    @Override
                    public Iterator<Path> iterator() {
                        return javaStream.iterator();
                    }
                });
    }


}
