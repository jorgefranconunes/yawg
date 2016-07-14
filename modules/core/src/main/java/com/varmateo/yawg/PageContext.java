/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
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

        pageTemplate = builder._pageTemplate;
        rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
        templateVars = builder._templateVarsBuilder.build();
    }


    /**
     * A builder of <code>PageContext</code> instances.
     */
    public static final class Builder
            extends Object {


        private Optional<Template> _pageTemplate;
        private String _rootRelativeUrl;
        private TemplateVars.Builder _templateVarsBuilder;


        /**
         *
         */
        public Builder() {

            _pageTemplate = Optional.empty();
            _rootRelativeUrl = null;
            _templateVarsBuilder = new TemplateVars.Builder();
        }


        /**
         *
         */
        public Builder(final PageContext data) {

            _pageTemplate = data.pageTemplate;
            _rootRelativeUrl = data.rootRelativeUrl;
            _templateVarsBuilder = new TemplateVars.Builder(data.templateVars);
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

            _templateVarsBuilder = new TemplateVars.Builder(templateVars);

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
