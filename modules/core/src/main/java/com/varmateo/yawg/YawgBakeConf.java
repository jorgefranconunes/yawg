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
public final class YawgBakeConf
    extends Object {


    public final Path sourceDir;
    public final Path targetDir;


    /**
     *
     */
    private YawgBakeConf(final Builder builder) {

        this.sourceDir = builder._sourceDir;
        this.targetDir = builder._targetDir;
    }


    /**
     *
     */
    public static final class Builder
        extends Object {


        private Path _sourceDir = null;
        private Path _targetDir = null;


        /**
         *
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
        public YawgBakeConf build() {

            YawgBakeConf result = new YawgBakeConf(this);
            return result;
        }

    }


}