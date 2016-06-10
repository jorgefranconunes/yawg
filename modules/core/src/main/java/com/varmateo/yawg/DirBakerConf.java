/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Arrays;
import java.util.Optional;

import com.varmateo.yawg.util.GlobMatcher;


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
    public final Optional<GlobMatcher> filesToIgnore;

    /**
     * Strict list of files to include when baking a directory.
     */
    public final Optional<GlobMatcher> filesToIncludeOnly;


    /**
     *
     */
    private DirBakerConf(final Builder builder) {

        this.templateName = builder._templateName;
        this.filesToIgnore = builder._filesToIgnore;
        this.filesToIncludeOnly = builder._filesToIncludeOnly;
    }


    /**
     * Creates a new container starting with the values from
     * <code>that</code> and overriding it with the values we possess.
     *
     * @param that The source of default values.
     *
     * @return A newly created container initialized with our values,
     * and using as default values the ones contained in
     * <code>that</code>.
     */
    public DirBakerConf merge(final DirBakerConf that) {

        Builder builder = new Builder(that);

        this.templateName.ifPresent(builder::setTemplateName);
        this.filesToIgnore.ifPresent(builder::addFilesToIgnore);
        this.filesToIncludeOnly.ifPresent(builder::setFilesToIncludeOnly);

        DirBakerConf result = builder.build();

        return result;
    }


    /**
     * A builder of <code>DirBakerConf</code> instances.
     */
    public static final class Builder
            extends Object {


        private Optional<String> _templateName = Optional.empty();
        private Optional<GlobMatcher> _filesToIgnore = Optional.empty();
        private Optional<GlobMatcher> _filesToIncludeOnly = Optional.empty();


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
            _filesToIgnore = defaults.filesToIgnore;
            // _filesToIncludeOnly always start empty.
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
        public Builder setFilesToIgnore(final GlobMatcher fileNames) {

            _filesToIgnore = Optional.of(fileNames);

            return this;
        }


        /**
         * @throws PatternSyntaxException If any of the given glob
         * expressions are invalid.
         */
        public Builder setFilesToIgnore(final String... fileNames) {

            GlobMatcher patterns = new GlobMatcher(Arrays.asList(fileNames));

            setFilesToIgnore(patterns);

            return this;
        }


        /**
         *
         */
        private Builder addFilesToIgnore(final GlobMatcher fileNames) {

            if ( _filesToIgnore.isPresent() ) {
                _filesToIgnore = _filesToIgnore.map(m -> m.add(fileNames));
            } else {
                _filesToIgnore = Optional.of(fileNames);
            }

            return this;
        }


        /**
         *
         */
        public Builder setFilesToIncludeOnly(final GlobMatcher fileNames) {

            _filesToIncludeOnly = Optional.of(fileNames);

            return this;
        }


        /**
         * @throws PatternSyntaxException If any of the given regular
         * expressions are invalid.
         */
        public Builder setFilesToIncludeOnly(final String... fileNames) {

            GlobMatcher patterns = new GlobMatcher(Arrays.asList(fileNames));

            setFilesToIncludeOnly(patterns);

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
