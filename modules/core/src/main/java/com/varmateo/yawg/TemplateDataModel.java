/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Objects;
import java.util.UUID;

import com.varmateo.yawg.TemplateVars;
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
     * Set of variables available to the page template. These
     * variables are immutable.
     */
    public final TemplateVars templateVars;


    /**
     *
     */
    private TemplateDataModel(final Builder builder) {

        bakeId = UUID.randomUUID().toString();
        body = Objects.requireNonNull(builder._body);
        title = Objects.requireNonNull(builder._title);
        rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
        productName = YawgInfo.PRODUCT_NAME;
        version = YawgInfo.VERSION;
        templateVars = Objects.requireNonNull(builder._templateVars);
    }


    /**
     * Builder of <code>TemplateDataModel</code> instances.
     */
    public static final class Builder
            extends Object {


        private String _body = null;
        private String _title = null;
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
        public Builder setTemplateVars(final TemplateVars templateVars) {

            _templateVars = templateVars;
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
