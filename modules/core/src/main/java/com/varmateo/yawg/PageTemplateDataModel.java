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


    private final String _body;
    private final String _title;


    /**
     *
     */
    private PageTemplateDataModel(final Builder builder) {

        _body = Objects.requireNonNull(builder._body);
        _title = Objects.requireNonNull(builder._title);
    }


    /**
     * @return The raw HTML contents of the baked document. This is
     * actually an HTML snippet appropriate for inclusion under a
     * <code>&lt;body&gt;</code> tag, or any other block level
     * element.
     */
    public String getBody() {

        return _body;
    }


    /**
     * @return The title of the document, as extracted from its source
     * file.
     */
    public String getTitle() {

        return _title;
    }


    /**
     * @return A fixed string with the Yawg product name.
     */
    public String getProductName() {

        return YawgInfo.PRODUCT_NAME;
    }


    /**
     * @return The version of the Yawg software being used.
     */
    public String getVersion() {

        return YawgInfo.VERSION;
    }


    /**
     * Builder of <code>PageTemplateDataModel</code>s.
     */
    public static final class Builder
            extends Object {


        private String _body = null;
        private String _title = null;


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
        public PageTemplateDataModel build() {

            PageTemplateDataModel result = new PageTemplateDataModel(this);

            return result;
        }


    }


}
