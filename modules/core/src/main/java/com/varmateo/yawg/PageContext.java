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
     * Instances of this class are only created by the Builder.
     */
    private PageContext(final Builder builder) {

        pageTemplate = builder._pageTemplate;
        rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
    }


    /**
     *
     */
    public static final class Builder
            extends Object {


        private Optional<PageTemplate> _pageTemplate = Optional.empty();
        private String _rootRelativeUrl = null;


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
        public PageContext build() {

            PageContext result = new PageContext(this);

            return result;
        }


    }


}
