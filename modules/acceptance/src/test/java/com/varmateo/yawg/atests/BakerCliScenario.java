/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.IOException;
import java.nio.file.Path;

import com.varmateo.testutils.TestUtils;


/**
 *
 */
public final class BakerCliScenario {


    private final BakerCliRunner _bakerRunner;
    private final Path _expectedResultsPath;


    /**
     *
     */
    private BakerCliScenario(final Builder builder) {

        _bakerRunner = builder._bakerRunnerBuilder.build();
        _expectedResultsPath = builder._expectedResultsPath;
    }


    /**
     *
     */
    public static Builder builder(
            final Class<?> testsuiteClass,
            final String testName)
            throws IOException {

        return new Builder(testsuiteClass, testName);
    }


    /**
     *
     */
    public Path getExpectedResultsPath() {

        return _expectedResultsPath;
    }


    /**
     *
     */
    public BakerCliResult run() {

        return _bakerRunner.run();
    }


    /**
     *
     */
    public static final class Builder {


        private final Path _testsuiteDir;
        private final BakerCliRunner.Builder _bakerRunnerBuilder;
        private final Path _expectedResultsPath;


        /**
         *
         */
        private Builder(
            final Class<?> testsuiteClass,
            final String testName)
                throws IOException {

            Path testsuiteDir = TestUtils.getPath(testsuiteClass, testName);
            Path sourcePath = testsuiteDir.resolve("content");
            Path targetPath = TestUtils.newTempDir(testsuiteClass);
            BakerCliRunner.Builder bakerRunnerBuilder =
                    BakerCliRunner.builder()
                    .addSourcePath(sourcePath)
                    .addTargetPath(targetPath);

            _testsuiteDir = testsuiteDir;
            _bakerRunnerBuilder = bakerRunnerBuilder;
            _expectedResultsPath = testsuiteDir.resolve("expected");
        }


        /**
         *
         */
        public Builder addTemplatesPath() {

            Path templatesPath = _testsuiteDir.resolve("templates");
            _bakerRunnerBuilder.addTemplatesPath(templatesPath);

            return this;
        }


        /**
         *
         */
        public Builder addAssetsPath() {

            Path assetsPath = _testsuiteDir.resolve("assets");
            _bakerRunnerBuilder.addAssetsPath(assetsPath);

            return this;
        }


        /**
         *
         */
        public Builder addArgs(final String... args) {

            _bakerRunnerBuilder.addArgs(args);

            return this;
        }


        /**
         *
         */
        public BakerCliScenario build() {

            return new BakerCliScenario(this);
        }

    }

}
