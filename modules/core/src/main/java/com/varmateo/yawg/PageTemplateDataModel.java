/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;


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

        _body = builder._body;
        _title = builder._title;
    }


    /**
     *
     */
    public String getBody() {

        return _body;
    }


    /**
     *
     */
    public String getTitle() {

        return _title;
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

            _body = body;
            return this;
        }


        /**
         *
         */
        public Builder setTitle(final String title) {

            _title = title;
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
