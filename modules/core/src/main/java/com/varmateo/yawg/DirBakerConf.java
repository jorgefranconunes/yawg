/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Optional;


/**
 * Set of cofniguration parameters used for baking the files in a
 * directory.
 */
/* package private */ final class DirBakerConf
        extends Object {


    public final Optional<String> templateName;


    /**
     *
     */
    private DirBakerConf(final Builder builder) {

        this.templateName = builder._templateName;
    }


    /**
     * A builder of <code>DirBakerConf</code> instances.
     */
    public static final class Builder
            extends Object {


        private Optional<String> _templateName = Optional.empty();


        /**
         *
         */
        public Builder() {

            // Nothing to do.
        }


        /**
         *
         */
        public Builder setTemplateName(final String templateName) {

            _templateName = Optional.of(templateName);

            return this;
        }


        /**
         *
         */
        public DirBakerConf build() {

            DirBakerConf result = new DirBakerConf(this);
            return result;
        }


    }


}