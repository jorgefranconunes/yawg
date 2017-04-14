/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import javaslang.collection.List;
import javaslang.collection.Seq;
import javaslang.control.Option;

import com.varmateo.yawg.api.PageVars;
import com.varmateo.yawg.core.TemplateNameMatcher;
import com.varmateo.yawg.util.GlobMatcher;


/**
 * Set of configuration parameters used for baking the files in a
 * directory.
 */
/* package private */ final class DirBakerConf {


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
    private DirBakerConf(final Builder builder) {

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

        Builder result = new Builder();

        return result;
    }


    /**
     *
     */
    public static DirBakerConf empty() {

        DirBakerConf result = DirBakerConf.builder().build();

        return result;
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
    public DirBakerConf mergeOnTopOf(final DirBakerConf that) {

        Builder builder = new Builder(that);

        this.templateName.forEach(builder::setTemplateName);
        this.filesToExclude.forEach(builder::addFilesToExclude);
        this.filesToExcludeHere.forEach(builder::setFilesToExcludeHere);
        this.filesToIncludeHere.forEach(builder::setFilesToIncludeHere);
        this.bakerTypes.forEach(builder::addBakerTypes);
        builder
                .addPageVars(this.pageVars)
                .setPageVarsHere(this.pageVarsHere)
                .setTemplatesHere(this.templatesHere)
                .setExtraDirsHere(this.extraDirsHere);


        DirBakerConf result = builder.build();

        return result;
    }


    /**
     * A builder of <code>DirBakerConf</code> instances.
     */
    public static final class Builder {


        // These always start empty.
        private Option<GlobMatcher> _filesToExcludeHere = Option.none();
        private Option<GlobMatcher> _filesToIncludeHere = Option.none();
        private PageVars _pageVarsHere = new PageVars();
        private TemplateNameMatcher _templatesHere = new TemplateNameMatcher();

        // These adopt the values from externally provided
        // initialization data.
        private Option<String> _templateName;
        private Option<GlobMatcher> _filesToExclude;
        private Option<BakerMatcher> _bakerTypes;
        private PageVars.Builder _pageVarsBuilder;
        private Seq<Path> _extraDirsHere;


        /**
         *
         */
        private Builder() {

            _templateName = Option.none();
            _filesToExclude = Option.none();
            _bakerTypes = Option.none();
            _pageVarsBuilder = PageVars.builder();
            _extraDirsHere = List.of();
        }


        /**
         * Prepares a Builder for performing a merge.
         */
        private Builder(final DirBakerConf defaults) {

            _templateName = defaults.templateName;
            _filesToExclude = defaults.filesToExclude;
            _bakerTypes = defaults.bakerTypes;
            _pageVarsBuilder = PageVars.builder(defaults.pageVars);
            _extraDirsHere = defaults.extraDirsHere;
        }


        /**
         *
         */
        public Builder setTemplateName(final String templateName) {

            _templateName = Option.of(templateName);

            return this;
        }


        /**
         *
         */
        public Builder setFilesToExclude(final GlobMatcher fileNames) {

            _filesToExclude = Option.of(fileNames);

            return this;
        }


        /**
         * @throws PatternSyntaxException If any of the given glob
         * expressions are invalid.
         */
        public Builder setFilesToExclude(final String... fileNames) {

            GlobMatcher patterns = new GlobMatcher(fileNames);

            setFilesToExclude(patterns);

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
        public Builder setFilesToExcludeHere(final GlobMatcher fileNames) {

            _filesToExcludeHere = Option.of(fileNames);

            return this;
        }


        /**
         * @throws PatternSyntaxException If any of the given regular
         * expressions are invalid.
         */
        public Builder setFilesToExcludeHere(final String... fileNames) {

            GlobMatcher patterns = new GlobMatcher(fileNames);

            return setFilesToExcludeHere(patterns);
        }


        /**
         *
         */
        public Builder setFilesToIncludeHere(final GlobMatcher fileNames) {

            _filesToIncludeHere = Option.of(fileNames);

            return this;
        }


        /**
         * @throws PatternSyntaxException If any of the given regular
         * expressions are invalid.
         */
        public Builder setFilesToIncludeHere(final String... fileNames) {

            GlobMatcher patterns = new GlobMatcher(fileNames);

            return setFilesToIncludeHere(patterns);
        }


        /**
         *
         */
        public Builder setBakerTypes(final BakerMatcher bakerTypes) {

            _bakerTypes = Option.of(bakerTypes);

            return this;
        }


        /**
         *
         */
        public Builder addBakerType(
                final String bakerType,
                final String... fileNames) {

            BakerMatcher bakerTypes =
                    BakerMatcher.builder()
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
        public  Builder setPageVars(final PageVars pageVars) {

            _pageVarsBuilder = PageVars.builder(pageVars);

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
        public Builder setPageVarsHere(final PageVars pageVarsHere) {

            _pageVarsHere = pageVarsHere;

            return this;
        }


        /**
         *
         */
        public Builder setTemplatesHere(
                final TemplateNameMatcher templatesHere) {

            _templatesHere = templatesHere;

            return this;
        }


        /**
         *
         */
        public Builder setExtraDirsHere(
                final Seq<Path> extraDirsHere) {

            _extraDirsHere = extraDirsHere;

            return this;
        }


        /**
         *
         */
        public DirBakerConf build() {

            return new DirBakerConf(this);
        }


    }


}
