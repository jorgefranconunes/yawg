/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;


/**
 * Set of configuration parameters for a <code>Baker</code>.
 *
 * <p>Objects of this class are immutable.</p>
 */
public final class BakerConf
    extends Object {


    public final Path sourceDir;
    public final Path targetDir;
    public final Path templatesDir;


    /**
     *
     */
    private BakerConf(final Builder builder) {

        this.sourceDir = builder._sourceDir;
        this.targetDir = builder._targetDir;
        this.templatesDir = builder._templatesDir;
    }


    /**
     * A builder of <code>BakerConf</code> instances.
     */
    public static final class Builder
        extends Object {


        private Path _sourceDir = null;
        private Path _targetDir = null;
        private Path _templatesDir = null;


        /**
         * A builder of <code>BakerConf</code> instances.
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
        public BakerConf build() {

            BakerConf result = new BakerConf(this);
            return result;
        }

    }


}
