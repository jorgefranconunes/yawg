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


    /**
     * The raw HTML contents of the baked document. This is actually
     * an HTML snippet appropriate for inclusion under a
     * <code>&lt;body&gt;</code> tag, or any other block level
     * element.
     */
    public final String body;

    /**
     * The title of the document, as extracted from its source.
     */
    public final String title;

    /**
     * The URL of the root directory of the site being baked relative
     * to the document about to be baked.
     */
    public final String rootRelativeUrl;

    /**
     * A fixed string with the Yawg product name.
     */
    public final String productName;

    /**
     * The version of the Yawg software being used.
     */
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
