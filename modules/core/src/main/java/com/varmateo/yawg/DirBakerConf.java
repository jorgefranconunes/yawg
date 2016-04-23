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
     * Name of template to use when baking a directory and all its
     * sub-directories.
     */
    public final Optional<String> templateName;

    /**
     * List of files to ignore when baking a directory.
     */
    public final Collection<Pattern> filesToIgnore;

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
                Lists.readOnlyCopy(builder._filesToIgnore);
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

        if ( this.templateName.isPresent() ) {
            builder.setTemplateName(this.templateName.get());
        }
        builder.addFilesToIgnore(this.filesToIgnore);
        this.filesToIncludeOnly.ifPresent(builder::setFilesToIncludeOnly);

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
        private Collection<Pattern> _filesToIgnore =
                new ArrayList<>();
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
            _filesToIgnore.addAll(defaults.filesToIgnore);

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
        public Builder addFilesToIgnore(final Collection<Pattern> fileNames) {

            _filesToIgnore.addAll(fileNames);

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
