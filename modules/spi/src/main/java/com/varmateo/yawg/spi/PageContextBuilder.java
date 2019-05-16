/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
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
    private PageVarsBuilder _pageVarsBuilder;


    /**
     *
     */
    private PageContextBuilder() {

        _dirUrl = null;
        _templateFetcher = (path -> Optional.empty());
        _rootRelativeUrl = null;
        _pageVarsBuilder = PageVarsBuilder.create();
    }


    /**
     *
     */
    private PageContextBuilder(final PageContext data) {

        _dirUrl = data.dirUrl();
        _templateFetcher = data::templateFor;
        _rootRelativeUrl = data.rootRelativeUrl();
        _pageVarsBuilder = PageVarsBuilder.create(data.pageVars());
    }


    /**
     * Creates a new empty <code>PageContextBuilder</code> instance.
     *
     * @return A new empty builder object.
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
     *
     * @return A new builder object initialized with the given data.
     */
    public static PageContextBuilder create(final PageContext initialData) {

        return new PageContextBuilder(initialData);
    }


    /**
     * @return A reference to this builder.
     */
    public PageContextBuilder dirUrl(final String dirUrl) {

        _dirUrl = dirUrl;
        return this;
    }


    /**
     * @param templateFetcher Function that can be used to retrieve
     * the template to be applied to a given path.
     *
     * @return A reference to this builder.
     */
    public PageContextBuilder templateFetcher(
            final Function<Path,Optional<Template>> templateFetcher) {

        _templateFetcher = templateFetcher;
        return this;
    }


    /**
     * @return A reference to this builder.
     */
    public PageContextBuilder rootRelativeUrl(final String rootRelativeUrl) {

        _rootRelativeUrl = rootRelativeUrl;
        return this;
    }


    /**
     * @return A reference to this builder.
     */
    public PageContextBuilder pageVars(final PageVars pageVars) {

        _pageVarsBuilder = PageVarsBuilder.create(pageVars);
        return this;
    }


    /**
     * Merges the given set of page variables into the set of page
     * variables currently hold by this builder. Page variables
     * currently in this builder with the same name will be superseded
     * by the new provided page variables.
     *
     * @param pageVars The set of page variables to add to the set of
     * page variables hold by this builder.
     *
     * @return A reference to this builder.
     */
    public PageContextBuilder addPageVars(final PageVars pageVars) {

        _pageVarsBuilder.addPageVars(pageVars);
        return this;
    }


    /**
     * Adds one page variable to this builder. An existing page
     * variable in this builder with the same name will be superseded
     * by the given page variable.
     *
     * @param varName The name of the page variable to be added.
     *
     * @param varValue The value of the page variable to be added.
     *
     * @return A reference to this builder.
     */
    public PageContextBuilder addVar(
            final String varName,
            final Object varValue) {

        _pageVarsBuilder.addVar(varName, varValue);
        return this;
    }


    /**
     * Creates a new <code>PageContext</code> instance initialized
     * with the current values in this builder.
     *
     * @return A newly created <code>PageContext</code> instance.
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
        public String dirUrl() {
            return _dirUrl;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Optional<Template> templateFor(final Path path) {
            return _templateFetcher.apply(path);
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public PageVars pageVars() {
            return _pageVars;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String rootRelativeUrl() {
            return _rootRelativeUrl;
        }

    }

}
