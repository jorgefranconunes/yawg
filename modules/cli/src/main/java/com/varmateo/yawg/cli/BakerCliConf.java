/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.OutputStream;

import javaslang.collection.List;
import javaslang.collection.Seq;


/**
 * Set of parameters requires to create a <code>BakerCli</code>
 * instance.
 */
public final class BakerCliConf {


    private static final String DEFAULT_ARGV0 = "yawg";


    public final String argv0;
    public final String[] args;
    public final OutputStream output;


    /**
     *
     */
    private BakerCliConf(final Builder builder) {

        argv0 = builder._argv0;
        args = builder._args.toJavaArray(String.class);
        output = builder._output;
    }


    /**
     * Creates a newly initialized builder for creating a
     * <code>BakerCliConf</code> instance.
     *
     * @return A new builder.
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
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
        public Builder setArgv0(final String argv0) {

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
        public Builder setOutput(final OutputStream output) {

            _output = output;

            return this;
        }


        /**
         *
         */
        public BakerCliConf build() {

            BakerCliConf result = new BakerCliConf(this);

            return result;
        }


    }


}
