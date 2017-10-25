/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import com.varmateo.yawg.api.SiteBakerConf;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * A builder of <code>SiteBakerConf</code> instances.
 */
public final class SiteBakerConfBuilder {


    private Optional<Path> _assetsDir = Optional.empty();
    private Path _sourceDir = null;
    private Path _targetDir = null;
    private Optional<Path> _templatesDir = Optional.empty();
    private Map<String,Object> _externalPageVars = new HashMap<>();


    /**
     * 
     */
    private SiteBakerConfBuilder() {
        // Nothing to do.
    }


    /**
     * Creates a new empty <code>SiteBakerConfBuilder</code> instance.
     */
    public static SiteBakerConfBuilder create() {

        return new SiteBakerConfBuilder();
    }


    /**
     *
     */
    public SiteBakerConfBuilder setAssetsDir(final Path assetsDir) {

        _assetsDir = Optional.ofNullable(assetsDir);
        return this;
    }


    /**
     *
     */
    public SiteBakerConfBuilder setSourceDir(final Path sourceDir) {

        _sourceDir = Objects.requireNonNull(sourceDir);
        return this;
    }


    /**
     *
     */
    public SiteBakerConfBuilder setTargetDir(final Path targetDir) {

        _targetDir = Objects.requireNonNull(targetDir);
        return this;
    }


    /**
     *
     */
    public SiteBakerConfBuilder setTemplatesDir(final Path templatesDir) {

        _templatesDir = Optional.ofNullable(templatesDir);
        return this;
    }


    /**
     * Adds page variables to be used by templates.
     *
     * @param externalPageVars Map where keys are the variable names,
     * and the values are the corresponding variable values.
     *
     * @return A reference to the same builder object.
     */
    public SiteBakerConfBuilder addExternalPageVars(
            final Map<String,Object> externalPageVars) {

        _externalPageVars.putAll(externalPageVars);
        return this;
    }


    /**
     * Creates a new <code>SiteBakerConf</code> instance initialized
     * with the current values in this builder.
     *
     * @return A new <code>SiteBakerConf</code> instance.
     */
    public SiteBakerConf build() {

        return new SiteBakerConfImpl(this);
    }


    /**
     *
     */
    private static final class SiteBakerConfImpl
            implements SiteBakerConf {


        private final Optional<Path> _assetsDir;
        private final Path _sourceDir;
        private final Path _targetDir;
        private final Optional<Path> _templatesDir;
        private final Map<String,Object> _externalPageVars;


        /**
         *
         */
        SiteBakerConfImpl(final SiteBakerConfBuilder builder) {

            _assetsDir = builder._assetsDir;
            _sourceDir = Objects.requireNonNull(builder._sourceDir);
            _targetDir = Objects.requireNonNull(builder._targetDir);
            _templatesDir = builder._templatesDir;
            _externalPageVars = Collections.unmodifiableMap(
                    new HashMap<>(builder._externalPageVars));
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Optional<Path> getAssetsDir() {
            return _assetsDir;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Path getSourceDir() {
            return _sourceDir;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Path getTargetDir() {
            return _targetDir;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Optional<Path> getTemplatesDir() {
            return _templatesDir;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String,Object> getExternalPageVars() {
            return _externalPageVars;
        }


    }


}
