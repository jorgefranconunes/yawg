/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

import com.varmateo.yawg.util.Lists;


/**
 * Set of cofniguration parameters used for baking the files in a
 * directory.
 */
/* package private */ final class DirBakerConf
        extends Object {


    /**
     * Name of template to use for the current directory and all
     * sub-directories.
     */
    public final Optional<String> templateName;

    /**
     * List of files to ignore.
     */
    public final Collection<Pattern> filesToIgnore;


    /**
     *
     */
    private DirBakerConf(final Builder builder) {

        this.templateName = builder._templateName;
        this.filesToIgnore = Lists.readOnlyCopy(builder._filesToIgnore);
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

        if ( this.templateName.isPresent() ) {
            builder.setTemplateName(this.templateName.get());
        }
        builder.addFilesToIgnore(this.filesToIgnore);

        DirBakerConf result = builder.build();

        return result;
    }


    /**
     * A builder of <code>DirBakerConf</code> instances.
     */
    public static final class Builder
            extends Object {


        private Optional<String> _templateName = Optional.empty();
        private Collection<Pattern> _filesToIgnore = new ArrayList<>();


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
            _filesToIgnore.addAll(defaults.filesToIgnore);
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
        public Builder addFilesToIgnore(final Collection<Pattern> fileNames) {

            _filesToIgnore.addAll(fileNames);

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
