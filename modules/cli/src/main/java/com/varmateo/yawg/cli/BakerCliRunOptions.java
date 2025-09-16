/**************************************************************************
 *
 * Copyright (c) 2017-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.OutputStream;

import io.vavr.collection.List;
import io.vavr.collection.Seq;


/**
 * Set of parameters required to create a {@code BakerCli} instance.
 */
public final class BakerCliRunOptions {


    private static final String DEFAULT_ARGV0 = "yawg";


    public final String argv0;
    public final String[] args;
    public final OutputStream output;


    /**
     *
     */
    private BakerCliRunOptions(final Builder builder) {

        argv0 = builder._argv0;
        args = builder._args.toJavaArray(String[]::new);
        output = builder._output;
    }


    /**
     * Creates a newly initialized builder for creating a
     * <code>BakerCliRunOptions</code> instance.
     *
     * @return A new builder.
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    public static final class Builder {


        private String _argv0;
        private Seq<String> _args;
        private OutputStream _output;


        /**
         *
         */
        private Builder() {

            _argv0 = DEFAULT_ARGV0;
            _args = List.of();
            _output = System.out;
        }


        /**
         * @param argv0 The name the utility was launched with on the
         * command line. It will be used in informative or error
         * messages.
         */
        public Builder argv0(final String argv0) {

            _argv0 = argv0;

            return this;
        }


        /**
         * @param args The command line arguments passed to the
         * utility.
         */
        public Builder addArgs(final String... args) {

            _args = _args.appendAll(List.of(args));

            return this;
        }


        /**
         *
         */
        public Builder output(final OutputStream output) {

            _output = output;

            return this;
        }


        /**
         *
         */
        public BakerCliRunOptions build() {

            return new BakerCliRunOptions(this);
        }


    }


}
