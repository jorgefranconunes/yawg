/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
import com.varmateo.yawg.util.GlobMatcher;


/**
 * Set of configuration parameters used for baking the files in a
 * directory.
 */
/* default */ final class DirBakeOptions {


    /**
     * Name of template to use when baking a directory and all its
     * sub-directories.
     */
    public final Option<String> templateName;

    /**
     * List of files to exclude when baking a directory and its
     * sub-directories.
     */
    public final Option<GlobMatcher> filesToExclude;

    /**
     * List of files to exclude when baking the current directory.
     */
    public final Option<GlobMatcher> filesToExcludeHere;

    /**
     * Strict list of files to include when baking the current
     * directory.
     */
    public final Option<GlobMatcher> filesToIncludeHere;

    /**
     * Association of baker types to some lists of files.
     */
    public final Option<BakerMatcher> bakerTypes;

    /**
     * Set of variables to be made available to the template that will
     * also be propagated to child directories.
     */
    public final PageVars pageVars;


    /**
     * Set of variables made available to the template that will not
     * be propagated to child directories.
     */
    public final PageVars pageVarsHere;


    /**
     *
     */
    public final TemplateNameMatcher templatesHere;


    /**
     * Collection of additional directories with content to be
     * baked. The bake results from those directories will be placed
     * in the same location as the bake results from the directory
     * currently being baked.
     */
    public final Seq<Path> extraDirsHere;


    /**
     *
     */
    /* default */ DirBakeOptions(final Builder builder) {

        this.templateName = builder._templateName;
        this.filesToExclude = builder._filesToExclude;
        this.filesToExcludeHere = builder._filesToExcludeHere;
        this.filesToIncludeHere = builder._filesToIncludeHere;
        this.bakerTypes = builder._bakerTypes;
        this.pageVars = builder._pageVarsBuilder.build();
        this.pageVarsHere = builder._pageVarsHere;
        this.templatesHere = builder._templatesHere;
        this.extraDirsHere = builder._extraDirsHere;
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    public static DirBakeOptions empty() {

        return DirBakeOptions.builder().build();
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
    public DirBakeOptions mergeOnTopOf(final DirBakeOptions that) {

        final Builder builder = new Builder(that);

        this.templateName.forEach(builder::templateName);
        this.filesToExclude.forEach(builder::addFilesToExclude);
        this.filesToExcludeHere.forEach(builder::filesToExcludeHere);
        this.filesToIncludeHere.forEach(builder::filesToIncludeHere);
        this.bakerTypes.forEach(builder::addBakerTypes);
        builder
                .addPageVars(this.pageVars)
                .pageVarsHere(this.pageVarsHere)
                .templatesHere(this.templatesHere)
                .extraDirsHere(this.extraDirsHere);


        return builder.build();
    }


    /**
     * A builder of <code>DirBakeOptions</code> instances.
     */
    public static final class Builder {


        // These always start empty.
        private Option<GlobMatcher> _filesToExcludeHere = Option.none();
        private Option<GlobMatcher> _filesToIncludeHere = Option.none();
        private PageVars _pageVarsHere = PageVars.empty();
        private TemplateNameMatcher _templatesHere = TemplateNameMatcher.empty();

        // These adopt the values from externally provided
        // initialization data.
        private Option<String> _templateName;
        private Option<GlobMatcher> _filesToExclude;
        private Option<BakerMatcher> _bakerTypes;
        private PageVarsBuilder _pageVarsBuilder;
        private Seq<Path> _extraDirsHere;


        /**
         *
         */
        /* default */ Builder() {

            _templateName = Option.none();
            _filesToExclude = Option.none();
            _bakerTypes = Option.none();
            _pageVarsBuilder = PageVarsBuilder.create();
            _extraDirsHere = List.of();
        }


        /**
         * Prepares a Builder for performing a merge.
         */
        /* default */ Builder(final DirBakeOptions defaults) {

            _templateName = defaults.templateName;
            _filesToExclude = defaults.filesToExclude;
            _bakerTypes = defaults.bakerTypes;
            _pageVarsBuilder = PageVarsBuilder.create(defaults.pageVars);
            _extraDirsHere = defaults.extraDirsHere;
        }


        /**
         *
         */
        public Builder templateName(final String templateName) {

            _templateName = Option.of(templateName);

            return this;
        }


        /**
         *
         */
        public Builder filesToExclude(final GlobMatcher fileNames) {

            _filesToExclude = Option.of(fileNames);

            return this;
        }


        /**
         * @throws PatternSyntaxException If any of the given glob
         * expressions are invalid.
         */
        public Builder filesToExclude(final String... fileNames) {

            final GlobMatcher patterns = GlobMatcher.create(fileNames);

            filesToExclude(patterns);

            return this;
        }


        /**
         *
         */
        private Builder addFilesToExclude(final GlobMatcher fileNames) {

            _filesToExclude = _filesToExclude
                    .map(x ->
                         GlobMatcher.builder(x).addGlobMatcher(fileNames).build())
                    .orElse(() -> Option.some(fileNames));

            return this;
        }


        /**
         *
         */
        public Builder filesToExcludeHere(final GlobMatcher fileNames) {

            _filesToExcludeHere = Option.of(fileNames);

            return this;
        }


        /**
         * @throws PatternSyntaxException If any of the given regular
         * expressions are invalid.
         */
        public Builder filesToExcludeHere(final String... fileNames) {

            final GlobMatcher patterns = GlobMatcher.create(fileNames);

            return filesToExcludeHere(patterns);
        }


        /**
         *
         */
        public Builder filesToIncludeHere(final GlobMatcher fileNames) {

            _filesToIncludeHere = Option.of(fileNames);

            return this;
        }


        /**
         * @throws PatternSyntaxException If any of the given regular
         * expressions are invalid.
         */
        public Builder filesToIncludeHere(final String... fileNames) {

            final GlobMatcher patterns = GlobMatcher.create(fileNames);

            return filesToIncludeHere(patterns);
        }


        /**
         *
         */
        public Builder bakerTypes(final BakerMatcher bakerTypes) {

            _bakerTypes = Option.of(bakerTypes);

            return this;
        }


        /**
         *
         */
        public Builder addBakerType(
                final String bakerType,
                final String... fileNames) {

            final BakerMatcher bakerTypes = BakerMatcher.builder()
                    .addBakerType(bakerType, fileNames)
                    .build();

            addBakerTypes(bakerTypes);

            return this;
        }


        /**
         *
         */
        private Builder addBakerTypes(final BakerMatcher bakerTypes) {

            _bakerTypes = _bakerTypes
                    .map(x ->
                         BakerMatcher.builder(x).addBakerTypes(bakerTypes).build())
                    .orElse(() -> Option.of(bakerTypes));

            return this;
        }


        /**
         *
         */
        public  Builder pageVars(final PageVars pageVars) {

            _pageVarsBuilder = PageVarsBuilder.create(pageVars);

            return this;
        }


        /**
         *
         */
        private Builder addPageVars(final PageVars pageVars) {

            _pageVarsBuilder.addPageVars(pageVars);

            return this;
        }


        /**
         *
         */
        public Builder pageVarsHere(final PageVars pageVarsHere) {

            _pageVarsHere = pageVarsHere;

            return this;
        }


        /**
         *
         */
        public Builder templatesHere(final TemplateNameMatcher templatesHere) {

            _templatesHere = templatesHere;

            return this;
        }


        /**
         *
         */
        public Builder extraDirsHere(final Seq<Path> extraDirsHere) {

            _extraDirsHere = extraDirsHere;

            return this;
        }


        /**
         *
         */
        public DirBakeOptions build() {

            return new DirBakeOptions(this);
        }


    }


}
