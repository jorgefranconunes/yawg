/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.Template;


/**
 * Information to be used by a baker when baking a file.
 */
public final class PageContext {


    private final String _dirUrl;
    private final Function<Path,Optional<Template>> _templateFetcher;
    private final PageVars _pageVars;
    private final String _rootRelativeUrl;


    /**
     *
     */
    private PageContext(final Builder builder) {

        _dirUrl = Objects.requireNonNull(builder._dirUrl);
        _templateFetcher = builder._templateFetcher;
        _rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
        _pageVars = builder._pageVarsBuilder.build();
    }


    /**
     * The URL of the directory containing the current page relative
     * to the top of the document tree.
     */
    public String getDirUrl() {
        return _dirUrl;
    }


    /**
     * Template to be used when generating the target page.
     */
    public Optional<Template> getTemplateFor(final Path path) {

        Optional<Template> result = _templateFetcher.apply(path);

        return result;
    }


    /**
     * Additional variables made available to the template.
     */
    public PageVars getPageVars() {
        return _pageVars;
    }


    /**
     * The URL of the top of the baked site relative to the current
     * page. Useful for refering to resources at the top of the
     * document tree.
     */
    public String getRootRelativeUrl() {
        return _rootRelativeUrl;
    }


    /**
     * Creates a new builder with an empty initialization.
     *
     * @return The new <code>Builder</code> object.
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
    }


    /**
     * Creates a new builder initialized with the given data.
     *
     * @param data The data used for initializing the new builder.
     *
     * @return The new <code>Builder</code> object.
     */
    public static Builder builder(final PageContext data) {

        Builder result = new Builder(data);

        return result;
    }


    /**
     * A builder of <code>PageContext</code> instances.
     */
    public static final class Builder {


        private String _dirUrl;
        private Function<Path,Optional<Template>> _templateFetcher;
        private String _rootRelativeUrl;
        private PageVars.Builder _pageVarsBuilder;


        /**
         *
         */
        private Builder() {

            _dirUrl = null;
            _templateFetcher = (path -> Optional.empty());
            _rootRelativeUrl = null;
            _pageVarsBuilder = PageVars.builder();
        }


        /**
         *
         */
        private Builder(final PageContext data) {

            _dirUrl = data._dirUrl;
            _templateFetcher = data._templateFetcher;
            _rootRelativeUrl = data._rootRelativeUrl;
            _pageVarsBuilder = PageVars.builder(data._pageVars);
        }


        /**
         *
         */
        public Builder setDirUrl(final String dirUrl) {

            _dirUrl = dirUrl;

            return this;
        }


        /**
         *
         */
        public Builder setTemplateFetcher(
                final Function<Path,Optional<Template>> templateFetcher) {

            _templateFetcher = templateFetcher;

            return this;
        }


        /**
         *
         */
        public Builder setRootRelativeUrl(final String rootRelativeUrl) {

        _rootRelativeUrl = rootRelativeUrl;

        return this;
        }


        /**
         *
         */
        public Builder setPageVars(final PageVars pageVars) {

            _pageVarsBuilder = PageVars.builder(pageVars);

            return this;
        }


        /**
         *
         */
        public Builder addPageVars(final PageVars pageVars) {

            _pageVarsBuilder.addPageVars(pageVars);

            return this;
        }


        /**
         *
         */
        public Builder addVar(
                final String varName,
                final Object varValue) {

            _pageVarsBuilder.addVar(varName, varValue);

            return this;
        }


        /**
         *
         */
        public PageContext build() {

            PageContext result = new PageContext(this);

            return result;
        }


    }


}
