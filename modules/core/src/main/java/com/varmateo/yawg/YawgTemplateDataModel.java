/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;


/**
 * Data available to a template during processing.
 */
public final class YawgTemplateDataModel
        extends Object {


    private final String _body;
    private final String _title;


    /**
     *
     */
    private YawgTemplateDataModel(final Builder builder) {

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
     *
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
        public YawgTemplateDataModel build() {

            YawgTemplateDataModel result = new YawgTemplateDataModel(this);

            return result;
        }


    }


}
