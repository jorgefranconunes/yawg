/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Objects;
import java.util.UUID;

import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.YawgInfo;


/**
 * Data available to a template during processing.
 */
public final class TemplateDataModel
        extends Object {


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
    private TemplateDataModel(final Builder builder) {

        _bakeId = UUID.randomUUID().toString();
        _body = Objects.requireNonNull(builder._body);
        _pageUrl = Objects.requireNonNull(builder._pageUrl);
        _pageVars =
                (builder._pageVars!=null) ? builder._pageVars: new PageVars();
        _productName = YawgInfo.PRODUCT_NAME;
        _rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
        _title = Objects.requireNonNull(builder._title);
        _version = YawgInfo.VERSION;
    }


    /**
     * Randomly generated unique bake identifier. Each bake will have
     * a different identifier.
     */
    public String getBakeId() {
        return _bakeId;
    }


    /**
     * The raw HTML contents of the baked document. This is actually
     * an HTML snippet appropriate for inclusion under a
     * <code>&lt;body&gt;</code> tag, or any other block level
     * element.
     */
    public String getBody() {
        return _body;
    }


    /**
     *
     */
    public String getPageUrl() {
        return _pageUrl;
    }


    /**
     * Set of variables available to the page template. These
     * variables are immutable.
     */
    public PageVars getPageVars() {
        return _pageVars;
    }


    /**
     * A fixed string with the Yawg product name.
     */
    public String getProductName() {
        return _productName;
    }


    /**
     * The URL of the root directory of the site being baked relative
     * to the document about to be baked.
     */
    public String getRootRelativeUrl() {
        return _rootRelativeUrl;
    }


    /**
     * The title of the document, as extracted from its source.
     */
    public String getTitle() {
        return _title;
    }


    /**
     * The version of the Yawg software being used.
     */
    public String getVersion() {
        return _version;
    }


    /**
     *
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
    }


    /**
     * Builder of <code>TemplateDataModel</code> instances.
     */
    public static final class Builder
            extends Object {


        private String _body;
        private String _pageUrl;
        private String _rootRelativeUrl;
        private PageVars _pageVars;
        private String _title;


        /**
         *
         */
        public Builder() {

            _body = null;
            _pageUrl = null;
            _rootRelativeUrl = null;
            _pageVars = null;
            _title = null;
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
        public Builder setPageUrl(final String pageUrl) {

            _pageUrl = pageUrl;

            return this;
        }


        /**
         *
         */
        public Builder setRootRelativeUrl(
                final String rootRelativeUrl) {

            _rootRelativeUrl = rootRelativeUrl;

            return this;
        }


        /**
         *
         */
        public Builder setPageVars(final PageVars pageVars) {

            _pageVars = pageVars;

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
        public TemplateDataModel build() {

            TemplateDataModel result = new TemplateDataModel(this);

            return result;
        }

    }


}
