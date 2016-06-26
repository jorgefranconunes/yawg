/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;


/**
 * Set of configuration parameters for a <code>Baker</code>.
 *
 * <p>Objects of this class are immutable.</p>
 */
public final class SiteBakerConf
    extends Object {


    public final Path assetsDir;
    public final Path sourceDir;
    public final Path targetDir;
    public final Optional<Path> templatesDir;


    /**
     *
     */
    private SiteBakerConf(final Builder builder) {

        this.assetsDir = builder._assetsDir;
        this.sourceDir = builder._sourceDir;
        this.targetDir = builder._targetDir;
        this.templatesDir = builder._templatesDir;
    }


    /**
     * A builder of <code>SiteBakerConf</code> instances.
     */
    public static final class Builder
        extends Object {


        private Path _assetsDir = null;
        private Path _sourceDir = null;
        private Path _targetDir = null;
        private Optional<Path> _templatesDir = Optional.empty();


        /**
         * 
         */
        public Builder() {

            // Nothing to do.
        }


        /**
         *
         */
        public Builder setAssetsDir(final Path assetsDir) {

            _assetsDir = assetsDir;
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
