/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;


/**
 * Set of configuration parameters for a baking a site with
 * <code>SiteBaker</code>.
 *
 * <p>Objects of this class are immutable.</p>
 */
public final class SiteBakerConf
    extends Object {


    /**
     * Path of directory containing the assets files. These files will
     * be copied without any changes to teh target directory.
     */
    public final Optional<Path> assetsDir;


    /**
     * Path of directory containing the documents to be baked.
     */
    public final Path sourceDir;


    /**
     * Path of directory where the results of the baking will be
     * stored.
     */
    public final Path targetDir;


    /**
     * Path of directory containing the templates files.
     */
    public final Optional<Path> templatesDir;


    /**
     *
     */
    private SiteBakerConf(final Builder builder) {

        this.assetsDir = builder._assetsDir;
        this.sourceDir = Objects.requireNonNull(builder._sourceDir);
        this.targetDir = Objects.requireNonNull(builder._targetDir);
        this.templatesDir = builder._templatesDir;
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
    }


    /**
     * A builder of <code>SiteBakerConf</code> instances.
     */
    public static final class Builder
        extends Object {


        private Optional<Path> _assetsDir = Optional.empty();
        private Path _sourceDir = null;
        private Path _targetDir = null;
        private Optional<Path> _templatesDir = Optional.empty();


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
        public SiteBakerConf build() {

            SiteBakerConf result = new SiteBakerConf(this);
            return result;
        }

    }


}
