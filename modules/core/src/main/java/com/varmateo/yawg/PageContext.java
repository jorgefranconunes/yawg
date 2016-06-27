/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Objects;
import java.util.Optional;

import com.varmateo.yawg.PageTemplate;


/**
 * Information to be used by a baker when baking a file.
 */
public final class PageContext
        extends Object {


    /**
     *
     */
    public final Optional<PageTemplate> pageTemplate;

    /**
     *
     */
    public final String rootRelativeUrl;


    /**
     *
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
     *
     */
    public static final class Builder
            extends Object {


        private Optional<PageTemplate> _pageTemplate = Optional.empty();
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
        public Builder setPageTemplate(final PageTemplate template) {

            _pageTemplate = Optional.of(template);

            return this;
        }


        /**
         *
         */
        public Builder setPageTemplate(final Optional<PageTemplate> template) {

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
