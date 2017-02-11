/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;

import javaslang.collection.List;
import javaslang.collection.Seq;

import com.varmateo.yawg.cli.BakerCli;


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
    public static Builder builder() {

        Builder builder = new Builder();

        return builder;
    }


    /**
     *
     */
    public static BakerCliRunner empty() {

        BakerCliRunner result = BakerCliRunner.builder().build();

        return result;
    }


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
    public BakerCliResult run() {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BakerCli bakerCli =
                BakerCli.builder()
                .addArgs(_args)
                .setOutput(output)
                .build();
        int exitStatus = bakerCli.run();
        BakerCliResult result =
                BakerCliResult.builder()
                .setSourcePath(_sourcePath)
                .setTargetPath(_targetPath)
                .setOutput(output.toByteArray())
                .setExitStatus(exitStatus)
                .build();

        return result;
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
        public BakerCliRunner build() {

            BakerCliRunner result = new BakerCliRunner(this);

            return result;
        }


        /**
         *
         */
        public BakerCliResult run() {

            BakerCliRunner baker = new BakerCliRunner(this);
            BakerCliResult result = baker.run();

            return result;
        }


    }


}
