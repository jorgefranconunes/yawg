/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.spi.TemplateContext.Author;


/**
 * Java bean for passing data to the Freemarker template engine.
 */
public final class FreemarkerDataModel {


    private static final String KEY_YAWG_DATA = "yawg";


    private final YawgFreemarkerDataModel _data;
    private final PageVars _pageVars;


    /**
     * @param templateContext Source of page variables.
     */
    public FreemarkerDataModel(final TemplateContext templateContext) {

        _data = new YawgFreemarkerDataModel(templateContext);
        _pageVars = templateContext.pageVars();
    }


    /**
     * Retrieves the value of a template variable.
     *
     * <p>This method is intended to be called by the Freemarker
     * engine for retrieving a template variable value when a template
     * is being processed.</p>
     *
     * @param key The name of the template variable being retrieved.
     *
     * @return The value of the template variable with the given
     * name. Or null if there is no template variable with that name.
     */
    public Object get(final String varName) {

        return KEY_YAWG_DATA.equals(varName)
                ? _data
                : _pageVars.get(varName).orElse(null);
    }


    /**
     * This is a class that provides the right getters in order to
     * have the appropriate template variables visible inside the
     * Freemarker templates.
     *
     * <p>The purpose of this Java bean is to provide the template
     * variables named with prefix "yawg.".</p>
     */
    public static final class YawgFreemarkerDataModel {


        private final TemplateContext _templateContext;
        private final String _productName;
        private final String _version;


        /**
         *
         */
        /* default */ YawgFreemarkerDataModel(final TemplateContext templateContext) {

            _templateContext = templateContext;
            _productName = YawgInfo.PRODUCT_NAME;
            _version = YawgInfo.VERSION;
        }

        /**
         * Randomly generated unique bake identifier. Each bake will have
         * a different identifier.
         */
        public String getBakeId() {

            return _templateContext.bakeId();
        }


        /**
         * A fixed string with the Yawg product name.
         */
        public String getProductName() {

            return _productName;
        }


        /**
         * The version of the Yawg software being used.
         */
        public String getVersion() {

            return _version;
        }


        public Iterable<Author> getAuthors() {

            return _templateContext.authors();
        }


        public String getBody() {

            return _templateContext.body();
        }


        public String getPageUrl() {

            return _templateContext.pageUrl();
        }


        public String getRootRelativeUrl() {

            return _templateContext.rootRelativeUrl();
        }


        public String getTitle() {

            return _templateContext.title();
        }

    }

}
