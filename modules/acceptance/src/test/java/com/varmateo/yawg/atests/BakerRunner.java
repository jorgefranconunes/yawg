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
public final class BakerRunner {


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
    public static BakerRunner empty() {

        BakerRunner result = BakerRunner.builder().build();

        return result;
    }


    /**
     *
     */
    private BakerRunner(final Builder builder) {

        _args = builder._args.toJavaArray(String.class);
    }


    /**
     *
     */
    public BakerRunnerResult run() {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BakerCli bakerCli =
                BakerCli.builder()
                .addArgs(_args)
                .setOutput(output)
                .build();
        int exitStatus = bakerCli.run();
        BakerRunnerResult result =
                BakerRunnerResult.builder()
                .setOutput(output.toByteArray())
                .setExitStatus(exitStatus)
                .build();

        return result;
    }


    /**
     *
     */
    public static final class Builder {


        private Seq<String> _args;


        /**
         *
         */
        private Builder() {

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
        public Builder addSourcePath(final Path sourcePath) {

            addArgs("--source", sourcePath.toString());

            return this;
        }


        /**
         *
         */
        public Builder addTargetPath(final Path targetPath) {

            addArgs("--target", targetPath.toString());

            return this;
        }


        /**
         *
         */
        public BakerRunner build() {

            BakerRunner result = new BakerRunner(this);

            return result;
        }


        /**
         *
         */
        public BakerRunnerResult run() {

            BakerRunner baker = new BakerRunner(this);
            BakerRunnerResult result = baker.run();

            return result;
        }


    }


}
