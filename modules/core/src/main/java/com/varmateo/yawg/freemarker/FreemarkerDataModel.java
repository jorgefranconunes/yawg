/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.spi.TemplateDataModel.Author;
import java.util.UUID;


/**
 * Java bean for passing data to the Freemarker template engine.
 */
public final class FreemarkerDataModel {


    private static final String KEY_YAWG_DATA = "yawg";


    private final YawgFreemarkerDataModel _data;
    private final PageVars _pageVars;


    /**
     * @param templateDataModel Source of page variables.
     */
    public FreemarkerDataModel(final TemplateDataModel templateDataModel) {

        _data = new YawgFreemarkerDataModel(templateDataModel);
        _pageVars = templateDataModel.getPageVars();
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


        private final TemplateDataModel _templateDataModel;
        private final String _bakeId;
        private final String _productName;
        private final String _version;


        /**
         *
         */
        /* default */ YawgFreemarkerDataModel(
                final TemplateDataModel templateDataModel) {

            _templateDataModel = templateDataModel;
            _bakeId = UUID.randomUUID().toString();
            _productName = YawgInfo.PRODUCT_NAME;
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

            return _templateDataModel.getAuthors();
        }


        public String getBody() {

            return _templateDataModel.getBody();
        }


        public String getPageUrl() {

            return _templateDataModel.getPageUrl();
        }


        public String getRootRelativeUrl() {

            return _templateDataModel.getRootRelativeUrl();
        }


        public String getTitle() {

            return _templateDataModel.getTitle();
        }

    }

}
