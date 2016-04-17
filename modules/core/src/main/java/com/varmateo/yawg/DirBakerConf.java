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
     * Creates a new container starting with the values from
     * <code>that</code> and overriding it with the values we possess.
     *
     * @param that The source of default values.
     *
     * @return A newly created container initialized with out values,
     * and using as default values the ones contained in
     * <code>that</code>.
     */
    public DirBakerConf merge(final DirBakerConf that) {

        Builder builder = new Builder(that);

        if ( templateName.isPresent() ) {
            builder.setTemplateName(templateName.get());
        }

        DirBakerConf result = builder.build();

        return result;
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
        private Builder(final DirBakerConf defaults) {

            _templateName = defaults.templateName;
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
