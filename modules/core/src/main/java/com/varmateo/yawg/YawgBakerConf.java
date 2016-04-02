/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;


/**
 *
 */
public final class YawgBakerConf
    extends Object {


    public final Path sourceDir;
    public final Path targetDir;
    public final Path templatesDir;


    /**
     *
     */
    private YawgBakerConf(final Builder builder) {

        this.sourceDir = builder._sourceDir;
        this.targetDir = builder._targetDir;
        this.templatesDir = builder._templatesDir;
    }


    /**
     *
     */
    public static final class Builder
        extends Object {


        private Path _sourceDir = null;
        private Path _targetDir = null;
        private Path _templatesDir = null;


        /**
         * A builder of <code>YawgBakerConf</code> instances.
         */
        public Builder() {

            // Nothing to do.
        }


        /**
         *
         */
        public Builder setSourceDir(final Path sourceDir) {

            _sourceDir = sourceDir;
            return this;
        }


        /**
         *
         */
        public Builder setTargetDir(final Path targetDir) {

            _targetDir = targetDir;
            return this;
        }


        /**
         *
         */
        public Builder setTemplatesDir(final Path templatesDir) {

            _templatesDir = templatesDir;
            return this;
        }


        /**
         *
         */
        public YawgBakerConf build() {

            YawgBakerConf result = new YawgBakerConf(this);
            return result;
        }

    }


}
