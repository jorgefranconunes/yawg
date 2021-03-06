/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;

import io.vavr.collection.Stream;


/**
 *
 */
public final class BakerCliResult {


    private final Path _sourcePath;
    private final Path _targetPath;
    private final byte[] _output;
    private final int _exitStatus;


    /**
     *
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    private BakerCliResult(final Builder builder) {

        _sourcePath = builder._sourcePath;
        _targetPath = builder._targetPath;
        _output = builder._output;
        _exitStatus = builder._exitStatus;
    }


    /**
     *
     */
    public Path sourcePath() {

        return Objects.requireNonNull(_sourcePath);
    }


    /**
     *
     */
    public Path targetPath() {

        return Objects.requireNonNull(_targetPath);
    }


    /**
     *
     */
    public String outputAsString() {

        return new String(_output, StandardCharsets.UTF_8);
    }


    /**
     *
     */
    public Stream<String> outputLines() {

        java.util.stream.Stream<String> lines =
                new BufferedReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(_output),
                                StandardCharsets.UTF_8))
                .lines();

        return Stream.ofAll(
                new Iterable<String>() {
                    @Override
                    public Iterator<String> iterator() {
                        return lines.iterator();
                    }
                });
    }


    /**
     *
     */
    public String outputLine(final int index) {

        return outputLines().get(index);
    }


    /**
     *
     */
    public String outputLineFromEnd(final int index) {

        return outputLines().reverse().get(index);
    }


    /**
     *
     */
    public int exitStatus() {

        return _exitStatus;
    }


    /**
     *
     */
    public static final class Builder {

        private Path _sourcePath;
        private Path _targetPath;
        private byte[] _output;
        private int _exitStatus;


        /**
         *
         */
        private Builder() {

            _sourcePath = null;
            _targetPath = null;
            _output = new byte[0];
            _exitStatus = 0;
        }


        /**
         *
         */
        public Builder sourcePath(final Path sourcePath) {

            _sourcePath = sourcePath;

            return this;
        }


        /**
         *
         */
        public Builder targetPath(final Path targetPath) {

            _targetPath = targetPath;

            return this;
        }


        /**
         *
         */
        public Builder output(final byte[] output) {

            _output = output;

            return this;
        }


        /**
         *
         */
        public Builder exitStatus(final int exitStatus) {

            _exitStatus = exitStatus;

            return this;
        }


        /**
         *
         */
        public BakerCliResult build() {

            BakerCliResult result = new BakerCliResult(this);

            return result;
        }


    }


}
