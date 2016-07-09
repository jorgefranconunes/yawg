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
     * The relative URL for the top URL of the baked site. Useful for
     * refering to resources at the top of the document tree.
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
        templateVars = Objects.requireNonNull(builder._templateVars);
    }


    /**
     * A builder of <code>PageContext</code> instances.
     */
    public static final class Builder
            extends Object {


        private Optional<Template> _pageTemplate = Optional.empty();
        private String _rootRelativeUrl = null;
        private TemplateVars _templateVars = new TemplateVars();


        /**
         *
         */
        public Builder() {
            // Nothing to do.
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

            _templateVars = templateVars;

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
