/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;


/**
 *
 */
public final class BakerRunnerResult {


    private final byte[] _output;
    private final int _exitStatus;


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
    private BakerRunnerResult(final Builder builder) {

        _output = builder._output;
        _exitStatus = builder._exitStatus;
    }


    /**
     *
     */
    public Stream<String> outputLines() {

        Stream<String> lines =
                new BufferedReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(_output),
                                StandardCharsets.UTF_8))
                .lines();

        return lines;
    }


    /**
     *
     */
    public String outputLine(final int index) {

        String result =
                outputLines()
                .skip(index)
                .findFirst()
                .get();

        return result;
    }


    /**
     *
     */
    public int getExitStatus() {

        return _exitStatus;
    }


    /**
     *
     */
    public static final class Builder {

        private byte[] _output;
        private int _exitStatus;


        /**
         *
         */
        private Builder() {

            _output = new byte[0];
            _exitStatus = 0;
        }


        /**
         *
         */
        public Builder setOutput(final byte[] output) {

            _output = output;

            return this;
        }


        /**
         *
         */
        public Builder setExitStatus(final int exitStatus) {

            _exitStatus = exitStatus;

            return this;
        }


        /**
         *
         */
        public BakerRunnerResult build() {

            BakerRunnerResult result = new BakerRunnerResult(this);

            return result;
        }


    }


}
