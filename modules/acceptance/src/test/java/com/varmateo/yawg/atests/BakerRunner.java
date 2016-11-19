/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.util.ArrayList;
import java.util.List;

import com.varmateo.yawg.commons.util.Lists;
import com.varmateo.yawg.cli.BakerCli;


/**
 *
 */
public final class BakerRunner
        extends Object {


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
    private BakerRunner(final Builder builder) {

        _args = Lists.toArray(builder._args, String.class);
    }


    /**
     *
     */
    public int run() {

        BakerCli bakerCli =
                BakerCli.builder()
                .addArgs(_args)
                .build();
        int exitStatus = bakerCli.run();

        return exitStatus;
    }


    /**
     *
     */
    public static final class Builder
            extends Object {


        private List<String> _args;


        /**
         *
         */
        private Builder() {

            _args = new ArrayList<>();
        }


        /**
         *
         */
        public Builder addArg(final String arg) {

            _args.add(arg);

            return this;
        }


        /**
         *
         */
        public Builder addArgs(final String... args) {

            for ( String arg : args ) {
                _args.add(arg);
            }

            return this;
        }


        /**
         *
         */
        public BakerRunner build() {

            BakerRunner result = new BakerRunner(this);

            return result;
        }


    }


}
