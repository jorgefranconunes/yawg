/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.Objects;
import java.util.UUID;

import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.TemplateDataModel;
import com.varmateo.yawg.YawgInfo;


/**
 * Builder of <code>TemplateDataModel</code> instances.
 */
public final class TemplateDataModelBuilder
        extends Object {


    private String _body;
    private String _pageUrl;
    private String _rootRelativeUrl;
    private PageVars _pageVars;
    private String _title;


    /**
     *
     */
    public TemplateDataModelBuilder() {

        _body = null;
        _pageUrl = null;
        _rootRelativeUrl = null;
        _pageVars = new PageVars();
        _title = null;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setBody(final String body) {

        _body = Objects.requireNonNull(body);

        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setPageUrl(final String pageUrl) {

        _pageUrl = pageUrl;

        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setRootRelativeUrl(
            final String rootRelativeUrl) {

        _rootRelativeUrl = rootRelativeUrl;

        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setPageVars(final PageVars pageVars) {

        _pageVars = pageVars;

        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setTitle(final String title) {

        _title = Objects.requireNonNull(title);

        return this;
    }


    /**
     *
     */
    public TemplateDataModel build() {

        TemplateDataModel result = new TemplateDataModelImpl(this);

        return result;
    }


    /**
     *
     */
    private static final class TemplateDataModelImpl
            extends Object
            implements TemplateDataModel {


        private final String _bakeId;
        private final String _body;
        private final String _pageUrl;
        private final PageVars _pageVars;
        private final String _productName;
        private final String _rootRelativeUrl;
        private final String _title;
        private final String _version;


        /**
         *
         */
        public TemplateDataModelImpl(final TemplateDataModelBuilder builder) {

            _bakeId = UUID.randomUUID().toString();
            _body = Objects.requireNonNull(builder._body);
            _pageUrl = Objects.requireNonNull(builder._pageUrl);
            _pageVars = Objects.requireNonNull(builder._pageVars);
            _productName = YawgInfo.PRODUCT_NAME;
            _rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
            _title = Objects.requireNonNull(builder._title);
            _version = YawgInfo.VERSION;
        }


        /**
         *
         */
        @Override
        public String getBakeId() {
            return _bakeId;
        }


        /**
         *
         */
        @Override
        public String getBody() {
            return _body;
        }


        /**
         *
         */
        @Override
        public String getPageUrl() {
            return _pageUrl;
        }


        /**
         *
         */
        @Override
        public PageVars getPageVars() {
            return _pageVars;
        }


        /**
         *
         */
        @Override
        public String getProductName() {
            return _productName;
        }


        /**
         *
         */
        @Override
        public String getRootRelativeUrl() {
            return _rootRelativeUrl;
        }


        /**
         *
         */
        @Override
        public String getTitle() {
            return _title;
        }


        /**
         *
         */
        @Override
        public String getVersion() {
            return _version;
        }


    }


}
