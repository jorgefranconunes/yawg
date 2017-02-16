/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import com.varmateo.yawg.api.PageVars;


/**
 * Set of configuration parameters for baking a site with a
 * <code>SiteBaker</code> instance.
 *
 * <p>Objects of this class are immutable.</p>
 */
public final class SiteBakerConf {


    private final Optional<Path> _assetsDir;
    private final Path _sourceDir;
    private final Path _targetDir;
    private final Optional<Path> _templatesDir;
    private final PageVars _externalPageVars;


    /**
     *
     */
    private SiteBakerConf(final Builder builder) {

        _assetsDir = builder._assetsDir;
        _sourceDir = Objects.requireNonNull(builder._sourceDir);
        _targetDir = Objects.requireNonNull(builder._targetDir);
        _templatesDir = builder._templatesDir;
        _externalPageVars = builder._externalPageVars;
    }


    /**
     * Path of directory containing the assets files. These files will
     * be copied without any changes to the target directory.
     */
    public Optional<Path> getAssetsDir() {
        return _assetsDir;
    }


    /**
     * Path of directory containing the documents to be baked.
     */
    public Path getSourceDir() {
        return _sourceDir;
    }


    /**
     * Path of directory where the results of the baking will be
     * stored.
     */
    public Path getTargetDir() {
        return _targetDir;
    }


    /**
     * Path of directory containing the templates files.
     */
    public Optional<Path> getTemplatesDir() {
        return _templatesDir;
    }


    /**
     * Set of page variables provided externally.
     *
     * <p>Page variables will intended to be used by templates.</p>
     */
    public PageVars getExternalPageVars() {
        return _externalPageVars;
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {
        return new Builder();
    }


    /**
     * A builder of <code>SiteBakerConf</code> instances.
     */
    public static final class Builder {


        private Optional<Path> _assetsDir = Optional.empty();
        private Path _sourceDir = null;
        private Path _targetDir = null;
        private Optional<Path> _templatesDir = Optional.empty();
        private PageVars _externalPageVars = new PageVars();


        /**
         * 
         */
        private Builder() {
            // Nothing to do.
        }


        /**
         *
         */
        public Builder setAssetsDir(final Path assetsDir) {

            _assetsDir = Optional.ofNullable(assetsDir);
            return this;
        }


        /**
         *
         */
        public Builder setSourceDir(final Path sourceDir) {

            _sourceDir = Objects.requireNonNull(sourceDir);
            return this;
        }


        /**
         *
         */
        public Builder setTargetDir(final Path targetDir) {

            _targetDir = Objects.requireNonNull(targetDir);
            return this;
        }


        /**
         *
         */
        public Builder setTemplatesDir(final Path templatesDir) {

            _templatesDir = Optional.ofNullable(templatesDir);
            return this;
        }


        /**
         *
         */
        public Builder setExternalPageVars(final PageVars externalPageVars) {

            _externalPageVars = externalPageVars;
            return this;
        }


        /**
         *
         */
        public SiteBakerConf build() {

            return new SiteBakerConf(this);
        }

    }


}
