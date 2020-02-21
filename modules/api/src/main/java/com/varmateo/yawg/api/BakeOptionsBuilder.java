/**************************************************************************
 *
 * Copyright (c) 2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Builder of {@code BakeOptions} instances.
 */
public final class BakeOptionsBuilder {


    private Path _sourceDir;
    private Path _targetDir;
    private Map<String, Object> _externalPageVars;


    private BakeOptionsBuilder(
            final Path sourceDir,
            final Path targetDir,
            final Map<String, Object> externalPageVars) {

        _sourceDir = sourceDir;
        _targetDir = targetDir;
        _externalPageVars = externalPageVars;
    }


    /**
     * Creates a new {@code BakeOptionsBuilder} initialized with no
     * values.
     */
    public static BakeOptionsBuilder create() {
        return new BakeOptionsBuilder(
                null,
                null,
                new HashMap<>());
    }


    /**
     * Creates a new {@code BakeOptionsBuilder} initialized with the
     * given data.
     */
    public static BakeOptionsBuilder create(final BakeOptions initialData) {
        return new BakeOptionsBuilder(
                initialData.sourceDir(),
                initialData.targetDir(),
                new HashMap<>(initialData.externalPageVars()));
    }


    /**
     * Sets the value to be returned by {@link BakeOptions#sourceDir()}.
     */
    public BakeOptionsBuilder sourceDir(final Path sourceDir) {
        _sourceDir = sourceDir;
        return this;
    }


    /**
     * Sets the value to be returned by {@link BakeOptions#targetDir()}.
     */
    public BakeOptionsBuilder targetDir(final Path targetDir) {
        _targetDir = targetDir;
        return this;
    }


    /**
     * Copies all of the mappings from map to the map to be returned
     * from {@link BakeOptions#externalPageVars()}.
     */
    public BakeOptionsBuilder putAllExternalPageVars(
            final Map<String, ? extends Object> externalPageVars) {
        _externalPageVars.putAll(externalPageVars);
        return this;
    }


    /**
     * Returns a newly-created {@link BakeOptions} based on the
     * contents of this {@code BakeOptionsBuilder}.
     */
    public BakeOptions build() {
        return new BakeOptionsImpl(
                _sourceDir,
                _targetDir,
                _externalPageVars);
    }


    /**
     *
     */
    private static final class BakeOptionsImpl
            implements BakeOptions {

        private final Path _sourceDir;
        private final Path _targetDir;
        private final Map<String, Object> _externalPageVars;


        BakeOptionsImpl(
                final Path sourceDir,
                final Path targetDir,
                final Map<String, Object> externalPageVars) {

            _sourceDir = Objects.requireNonNull(sourceDir);
            _targetDir = Objects.requireNonNull(targetDir);
            _externalPageVars = Collections.unmodifiableMap(new HashMap<>(externalPageVars));
        }


        @Override
        public Path sourceDir() {
            return _sourceDir;
        }


        @Override
        public Path targetDir() {
            return _targetDir;
        }


        @Override
        public Map<String, Object> externalPageVars() {
            return _externalPageVars;
        }
    }
}
