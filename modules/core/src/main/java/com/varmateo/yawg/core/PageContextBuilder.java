/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.Objects;
import java.util.Optional;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.Template;


/**
 * A builder of <code>PageContext</code> instances.
 */
public final class PageContextBuilder
        extends Object {


    private String _dirUrl;
    private Optional<Template> _pageTemplate;
    private String _rootRelativeUrl;
    private PageVars.Builder _pageVarsBuilder;


    /**
     *
     */
    public PageContextBuilder() {

        _dirUrl = null;
        _pageTemplate = Optional.empty();
        _rootRelativeUrl = null;
        _pageVarsBuilder = PageVars.builder();
    }


    /**
     *
     */
    public PageContextBuilder(final PageContext data) {

        _dirUrl = data.getDirUrl();
        _pageTemplate = data.getPageTemplate();
        _rootRelativeUrl = data.getRootRelativeUrl();
        _pageVarsBuilder = PageVars.builder(data.getPageVars());
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
    public PageContextBuilder setTemplate(final Template template) {

        _pageTemplate = Optional.of(template);

        return this;
    }


    /**
     *
     */
    public PageContextBuilder setTemplate(final Optional<Template> template) {

        _pageTemplate = template;

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

        PageContext result = new PageContextImpl(this);

        return result;
    }


    /**
     *
     */
    private static final class PageContextImpl
            extends Object
            implements PageContext {


        private final String _dirUrl;
        private final Optional<Template> _pageTemplate;
        private final String _rootRelativeUrl;
        private final PageVars _pageVars;


        /**
         *
         */
        public PageContextImpl(final PageContextBuilder builder) {

            _dirUrl = Objects.requireNonNull(builder._dirUrl);
            _pageTemplate = builder._pageTemplate;
            _rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
            _pageVars = builder._pageVarsBuilder.build();
        }


        /**
         *
         */
        @Override
        public String getDirUrl() {
            return _dirUrl;
        }


        /**
         *
         */
        @Override
        public Optional<Template> getPageTemplate() {
            return _pageTemplate;
        }


        /**
         *
         */
        @Override
        public String getRootRelativeUrl() {
            return _rootRelativeUrl;
        }


        /**
         *
         */
        @Override
        public PageVars getPageVars() {
            return _pageVars;
        }


    }


}
