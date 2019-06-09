/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;

import io.vavr.collection.List;
import io.vavr.collection.Seq;

import com.varmateo.yawg.cli.BakerCli;
import com.varmateo.yawg.cli.BakerCliRunOptions;


/**
 *
 */
public final class BakerCliRunner {


    private final Path _sourcePath;
    private final Path _targetPath;
    private final String[] _args;


    /**
     *
     */
    private BakerCliRunner(final Builder builder) {

        _sourcePath = builder._sourcePath;
        _targetPath = builder._targetPath;
        _args = builder._args.toJavaArray(String.class);
    }


    /**
     *
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    public static BakerCliRunner empty() {

        return BakerCliRunner.builder().build();
    }


    /**
     *
     */
    public BakerCliResult run() {

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final BakerCliRunOptions options = BakerCliRunOptions.builder()
                .addArgs(_args)
                .output(output)
                .build();
        final BakerCli bakerCli = BakerCli.create();
        final int exitStatus = bakerCli.run(options);

        return BakerCliResult.builder()
                .sourcePath(_sourcePath)
                .targetPath(_targetPath)
                .output(output.toByteArray())
                .exitStatus(exitStatus)
                .build();
    }


    /**
     *
     */
    public static final class Builder {


        private Path _sourcePath;
        private Path _targetPath;
        private Seq<String> _args;


        /**
         *
         */
        private Builder() {

            _sourcePath = null;
            _targetPath = null;
            _args = List.of();
        }


        /**
         *
         */
        public Builder addArg(final String arg) {

            _args = _args.append(arg);

            return this;
        }


        /**
         *
         */
        public Builder addArgs(final String... args) {

            _args = _args.appendAll(List.of(args));

            return this;
        }


        /**
         *
         */
        public Builder addSourcePath(final Path path) {

            _sourcePath = path;
            addArgs("--source", path.toString());

            return this;
        }


        /**
         *
         */
        public Builder addTargetPath(final Path path) {

            _targetPath = path;
            addArgs("--target", path.toString());

            return this;
        }


        /**
         *
         */
        public Builder addTemplatesPath(final Path path) {

            addArgs("--templates", path.toString());

            return this;
        }


        /**
         *
         */
        public Builder addAssetsPath(final Path path) {

            addArgs("--assets", path.toString());

            return this;
        }


        /**
         *
         */
        public BakerCliRunner build() {

            return new BakerCliRunner(this);
        }


        /**
         *
         */
        public BakerCliResult run() {

            final BakerCliRunner baker = new BakerCliRunner(this);

            return baker.run();
        }


    }


}
