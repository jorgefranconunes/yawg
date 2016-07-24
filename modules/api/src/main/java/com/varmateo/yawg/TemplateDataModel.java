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


    /**
     * Randomly generated unique bake identifier. Each bake will have
     * a different identifier.
     */
    public final String bakeId;

    /**
     * The raw HTML contents of the baked document. This is actually
     * an HTML snippet appropriate for inclusion under a
     * <code>&lt;body&gt;</code> tag, or any other block level
     * element.
     */
    public final String body;


    /**
     *
     */
    public final String pageUrl;

    /**
     * A fixed string with the Yawg product name.
     */
    public final String productName;

    /**
     * The URL of the root directory of the site being baked relative
     * to the document about to be baked.
     */
    public final String rootRelativeUrl;

    /**
     * The title of the document, as extracted from its source.
     */
    public final String title;

    /**
     * The version of the Yawg software being used.
     */
    public final String version;


    /**
     * Set of variables available to the page template. These
     * variables are immutable.
     */
    public final PageVars pageVars;


    /**
     *
     */
    private TemplateDataModel(final Builder builder) {

        bakeId = UUID.randomUUID().toString();
        body = Objects.requireNonNull(builder._body);
        rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
        pageUrl = Objects.requireNonNull(builder._pageUrl);
        productName = YawgInfo.PRODUCT_NAME;
        title = Objects.requireNonNull(builder._title);
        version = YawgInfo.VERSION;
        pageVars = Objects.requireNonNull(builder._pageVars);
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
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


        private String _body = null;
        private String _pageUrl = null;
        private String _rootRelativeUrl = null;
        private PageVars _pageVars = new PageVars();
        private String _title = null;


        /**
         *
         */
        private Builder() {

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
        public Builder setPageUrl(final String pageUrl) {

            _pageUrl = pageUrl;
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
