/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Objects;
import java.util.Optional;

import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.Template;


/**
 * Information to be used by a baker when baking a file.
 */
public final class PageContext {


    private final String _dirUrl;
    private final Optional<Template> _pageTemplate;
    private final String _rootRelativeUrl;
    private final PageVars _pageVars;


    /**
     *
     */
    private PageContext(final Builder builder) {

        _dirUrl = Objects.requireNonNull(builder._dirUrl);
        _pageTemplate = builder._pageTemplate;
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
    public Optional<Template> getPageTemplate() {
        return _pageTemplate;
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
     * Additional variables made available to the template.
     */
    public PageVars getPageVars() {
        return _pageVars;
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
    public static final class Builder
        extends Object {


        private String _dirUrl;
        private Optional<Template> _pageTemplate;
        private String _rootRelativeUrl;
        private PageVars.Builder _pageVarsBuilder;


        /**
         *
         */
        private Builder() {

            _dirUrl = null;
            _pageTemplate = Optional.empty();
            _rootRelativeUrl = null;
            _pageVarsBuilder = PageVars.builder();
        }


        /**
         *
         */
        private Builder(final PageContext data) {

            _dirUrl = data.getDirUrl();
            _pageTemplate = data.getPageTemplate();
            _rootRelativeUrl = data.getRootRelativeUrl();
            _pageVarsBuilder = PageVars.builder(data.getPageVars());
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
        public Builder setTemplate(final Template template) {

            _pageTemplate = Optional.of(template);

            return this;
        }


        /**
         *
         */
        public Builder setTemplate(final Optional<Template> template) {

            _pageTemplate = template;

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
