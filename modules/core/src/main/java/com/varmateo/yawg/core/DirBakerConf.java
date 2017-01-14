/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Optional;

import javaslang.collection.List;
import javaslang.collection.Seq;

import com.varmateo.yawg.PageVars;
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
    public final Optional<String> templateName;

    /**
     * List of files to exclude when baking a directory.
     */
    public final Optional<GlobMatcher> filesToExclude;

    /**
     * Strict list of files to include when baking a directory.
     */
    public final Optional<GlobMatcher> filesToIncludeHere;

    /**
     * Association of baker types to some lists of files.
     */
    public final Optional<BakerMatcher> bakerTypes;

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

        this.templateName.ifPresent(builder::setTemplateName);
        this.filesToExclude.ifPresent(builder::addFilesToExclude);
        this.filesToIncludeHere.ifPresent(builder::setFilesToIncludeHere);
        this.bakerTypes.ifPresent(builder::addBakerTypes);
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
        private Optional<GlobMatcher> _filesToIncludeHere = Optional.empty();;
        private PageVars _pageVarsHere = new PageVars();
        private TemplateNameMatcher _templatesHere = new TemplateNameMatcher();

        // These adopt the values from externally provided
        // initialization data.
        private Optional<String> _templateName;
        private Optional<GlobMatcher> _filesToExclude;
        private Optional<BakerMatcher> _bakerTypes;
        private PageVars.Builder _pageVarsBuilder;
        private Seq<Path> _extraDirsHere;


        /**
         *
         */
        private Builder() {

            _templateName = Optional.empty();
            _filesToExclude = Optional.empty();
            _bakerTypes = Optional.empty();
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

            _templateName = Optional.of(templateName);

            return this;
        }


        /**
         *
         */
        public Builder setFilesToExclude(final GlobMatcher fileNames) {

            _filesToExclude = Optional.of(fileNames);

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

            GlobMatcher newFilesToExclude = null;

            if ( _filesToExclude.isPresent() ) {
                newFilesToExclude =
                        GlobMatcher.builder(_filesToExclude.get())
                        .addGlobMatcher(fileNames)
                        .build();
            } else {
                newFilesToExclude = fileNames;
            }

            _filesToExclude = Optional.of(newFilesToExclude);

            return this;
        }


        /**
         *
         */
        public Builder setFilesToIncludeHere(final GlobMatcher fileNames) {

            _filesToIncludeHere = Optional.of(fileNames);

            return this;
        }


        /**
         * @throws PatternSyntaxException If any of the given regular
         * expressions are invalid.
         */
        public Builder setFilesToIncludeHere(final String... fileNames) {

            GlobMatcher patterns = new GlobMatcher(fileNames);

            setFilesToIncludeHere(patterns);

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

            BakerMatcher newBakerTypes = null;

            if ( _bakerTypes.isPresent() ) {
                newBakerTypes =
                        BakerMatcher.builder(_bakerTypes.get())
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

            DirBakerConf result = new DirBakerConf(this);
            return result;
        }


    }


}
