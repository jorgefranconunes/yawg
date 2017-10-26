/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.Template;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;


/**
 * A builder of <code>PageContext</code> instances.
 */
public final class PageContextBuilder {


    private String _dirUrl;
    private Function<Path,Optional<Template>> _templateFetcher;
    private String _rootRelativeUrl;
    private PageVars.Builder _pageVarsBuilder;


    /**
     *
     */
    private PageContextBuilder() {

        _dirUrl = null;
        _templateFetcher = (path -> Optional.empty());
        _rootRelativeUrl = null;
        _pageVarsBuilder = PageVars.builder();
    }


    /**
     *
     */
    private PageContextBuilder(final PageContext data) {

        _dirUrl = data.getDirUrl();
        _templateFetcher = data::getTemplateFor;
        _rootRelativeUrl = data.getRootRelativeUrl();
        _pageVarsBuilder = PageVars.builder(data.getPageVars());
    }


    /**
     * Creates a new empty <code>PageContextBuilder</code> instance.
     */
    public static PageContextBuilder create() {

        return new PageContextBuilder();
    }


    /**
     * Creates a new <code>PageContextBuilder</code> instance
     * initialized with the data from the given
     * <code>PageContext</code>.
     *
     * @param initialData Provides the data used for initializing the
     * returned builder.
     */
    public static PageContextBuilder create(final PageContext initialData) {

        return new PageContextBuilder(initialData);
    }


    /**
     *
     */
    public PageContextBuilder setDirUrl(final String dirUrl) {

        _dirUrl = dirUrl;
        return this;
    }


    /**
     *
     */
    public PageContextBuilder setTemplateFetcher(
            final Function<Path,Optional<Template>> templateFetcher) {

        _templateFetcher = templateFetcher;
        return this;
    }


    /**
     *
     */
    public PageContextBuilder setRootRelativeUrl(final String rootRelativeUrl) {

        _rootRelativeUrl = rootRelativeUrl;
        return this;
    }


    /**
     *
     */
    public PageContextBuilder setPageVars(final PageVars pageVars) {

        _pageVarsBuilder = PageVars.builder(pageVars);
        return this;
    }


    /**
     *
     */
    public PageContextBuilder addPageVars(final PageVars pageVars) {

        _pageVarsBuilder.addPageVars(pageVars);
        return this;
    }


    /**
     *
     */
    public PageContextBuilder addVar(
            final String varName,
            final Object varValue) {

        _pageVarsBuilder.addVar(varName, varValue);
        return this;
    }


    /**
     *
     */
    public PageContext build() {

        return new PageContextImpl(this);
    }


    /**
     *
     */
    private final class PageContextImpl
            implements PageContext {


        private final String _dirUrl;
        private final Function<Path,Optional<Template>> _templateFetcher;
        private final PageVars _pageVars;
        private final String _rootRelativeUrl;


        /**
         *
         */
        private PageContextImpl(final PageContextBuilder builder) {

            _dirUrl = Objects.requireNonNull(builder._dirUrl);
            _templateFetcher = builder._templateFetcher;
            _rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
            _pageVars = builder._pageVarsBuilder.build();
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String getDirUrl() {
            return _dirUrl;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Optional<Template> getTemplateFor(final Path path) {
            return _templateFetcher.apply(path);
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public PageVars getPageVars() {
            return _pageVars;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String getRootRelativeUrl() {
            return _rootRelativeUrl;
        }

    }

}
