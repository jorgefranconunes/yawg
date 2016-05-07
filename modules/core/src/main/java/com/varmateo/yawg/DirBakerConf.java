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
 * Set of configuration parameters used for baking the files in a
 * directory.
 */
/* package private */ final class DirBakerConf
        extends Object {


    /**
     * Name of template to use when baking a directory and all its
     * sub-directories.
     */
    public final Optional<String> templateName;

    /**
     * List of files to ignore when baking a directory.
     */
    public final Optional<Collection<Pattern>> filesToIgnore;

    /**
     * Strict list of files to include when baking a directory.
     */
    public final Optional<Collection<Pattern>> filesToIncludeOnly;


    /**
     *
     */
    private DirBakerConf(final Builder builder) {

        this.templateName =
                builder._templateName;
        this.filesToIgnore =
                builder._filesToIgnore.map(Lists::readOnlyCopy);
        this.filesToIncludeOnly =
                builder._filesToIncludeOnly.map(Lists::readOnlyCopy);
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

        that.templateName.ifPresent(builder::setTemplateName);
        that.filesToIgnore.ifPresent(builder::setFilesToIgnore);
        that.filesToIncludeOnly.ifPresent(builder::setFilesToIncludeOnly);

        DirBakerConf result = builder.build();

        return result;
    }


    /**
     * A builder of <code>DirBakerConf</code> instances.
     */
    public static final class Builder
            extends Object {


        private Optional<String> _templateName =
                Optional.empty();
        private Optional<Collection<Pattern>> _filesToIgnore =
                Optional.empty();
        private Optional<Collection<Pattern>> _filesToIncludeOnly =
                Optional.empty();


        /**
         *
         */
        public Builder() {

            // Nothing to do.
        }


        /**
         * Prepares a Builder for performing a merge.
         */
        private Builder(final DirBakerConf defaults) {

            _templateName = defaults.templateName;
            // _filesToIgnore always starts empty.
            // The _filesToIncludeOnly always start empty.
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
        public Builder setFilesToIgnore(final Collection<Pattern> fileNames) {

            _filesToIgnore = Optional.of(new ArrayList<>(fileNames));

            return this;
        }


        /**
         *
         */
        public Builder setFilesToIncludeOnly(
                final Collection<Pattern> fileNames) {

            _filesToIncludeOnly = Optional.of(new ArrayList<>(fileNames));

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
