/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Objects;

import com.varmateo.yawg.YawgInfo;


/**
 * Data available to a template during processing.
 */
public final class PageTemplateDataModel
        extends Object {


    public final String body;
    public final String title;
    public final String rootRelativeUrl;
    public final String productName;
    public final String version;


    /**
     *
     */
    private PageTemplateDataModel(final Builder builder) {

        body = Objects.requireNonNull(builder._body);
        title = Objects.requireNonNull(builder._title);
        rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
        productName = YawgInfo.PRODUCT_NAME;
        version = YawgInfo.VERSION;
    }


    /**
     * Builder of <code>PageTemplateDataModel</code> instances.
     */
    public static final class Builder
            extends Object {


        private String _body = null;
        private String _title = null;
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
        public Builder setBody(final String body) {

            _body = Objects.requireNonNull(body);
            return this;
        }


        /**
         *
         */
        public Builder setTitle(final String title) {

            _title = Objects.requireNonNull(title);
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
        public PageTemplateDataModel build() {

            PageTemplateDataModel result = new PageTemplateDataModel(this);

            return result;
        }


    }


}
