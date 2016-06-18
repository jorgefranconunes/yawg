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
     * Association of baker types to some lists of files.
     */
    public final Optional<BakerMatcher> bakerTypes;


    /**
     *
     */
    private DirBakerConf(final Builder builder) {

        this.templateName = builder._templateName;
        this.filesToIgnore = builder._filesToIgnore;
        this.filesToIncludeOnly = builder._filesToIncludeOnly;
        this.bakerTypes = builder._bakerTypes;
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
        this.bakerTypes.ifPresent(builder::addBakerTypes);

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
        private Optional<BakerMatcher> _bakerTypes = Optional.empty();


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
            _bakerTypes = defaults.bakerTypes;
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

            GlobMatcher newFilesToIgnore = null;

            if ( _filesToIgnore.isPresent() ) {
                newFilesToIgnore =
                        new GlobMatcher.Builder(_filesToIgnore.get())
                        .addGlobMatcher(fileNames)
                        .build();
            } else {
                newFilesToIgnore = fileNames;
            }

            _filesToIgnore = Optional.of(newFilesToIgnore);

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

            GlobMatcher patterns = new GlobMatcher(fileNames);

            setFilesToIncludeOnly(patterns);

            return this;
        }


        /**
         *
         */
        public Builder setBakerTypes(final BakerMatcher bakerTypes) {

            _bakerTypes = Optional.of(bakerTypes);

            return this;
        }


        /**
         *
         */
        public Builder addBakerType(
                final String bakerType,
                final String... fileNames) {

            BakerMatcher bakerTypes =
                    new BakerMatcher.Builder()
                    .addBakerType(bakerType, fileNames)
                    .build();

            addBakerTypes(bakerTypes);

            return this;
        }


        /**
         *
         */
        private Builder addBakerTypes(final BakerMatcher bakerTypes) {

            BakerMatcher newBakerTypes = null;

            if ( _bakerTypes.isPresent() ) {
                newBakerTypes =
                        new BakerMatcher.Builder(_bakerTypes.get())
                        .addBakerTypes(bakerTypes)
                        .build();
            } else {
                newBakerTypes = bakerTypes;
            }

            _bakerTypes = Optional.of(newBakerTypes);

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
