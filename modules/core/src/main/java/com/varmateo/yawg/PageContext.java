/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Objects;
import java.util.Optional;

import com.varmateo.yawg.Template;


/**
 * Information to be used by a baker when baking a file.
 */
public final class PageContext
        extends Object {


    /**
     * The URL of the directory containing the current page relative
     * to the top of the document tree.
     */
    public final String dirUrl;


    /**
     * Template to be used when generating the target page.
     */
    public final Optional<Template> pageTemplate;


    /**
     * The URL of the top of the baked site relative to the current
     * page. Useful for refering to resources at the top of the
     * document tree.
     */
    public final String rootRelativeUrl;


    /**
     * Additional variables made available to the template.
     */
    public final TemplateVars templateVars;


    /**
     * Instances of this class are only created by the Builder.
     */
    private PageContext(final Builder builder) {

        dirUrl = Objects.requireNonNull(builder._dirUrl);
        pageTemplate = builder._pageTemplate;
        rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
        templateVars = builder._templateVarsBuilder.build();
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
     * Creates a new builder initialized with the data from the given
     * <code>PageContext</code>.
     *
     * @param data Used for initializing the builder state.
     *
     * @return A newly created <code>Builder</code> instance.
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
        private TemplateVars.Builder _templateVarsBuilder;


        /**
         *
         */
        private Builder() {

            _dirUrl = null;
            _pageTemplate = Optional.empty();
            _rootRelativeUrl = null;
            _templateVarsBuilder = TemplateVars.builder();
        }


        /**
         *
         */
        private Builder(final PageContext data) {

            _dirUrl = data.dirUrl;
            _pageTemplate = data.pageTemplate;
            _rootRelativeUrl = data.rootRelativeUrl;
            _templateVarsBuilder = TemplateVars.builder(data.templateVars);
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
        public Builder setTemplateVars(final TemplateVars templateVars) {

            _templateVarsBuilder = TemplateVars.builder(templateVars);

            return this;
        }


        /**
         *
         */
        public Builder addVar(
                final String varName,
                final Object varValue) {

            _templateVarsBuilder.addVar(varName, varValue);

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
