/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import com.varmateo.yawg.TemplateDataModel;


/**
 * Java bean for passing data to the Freemarker template engine.
 */
public final class FreemarkerDataModel
        extends Object {


    private final TemplateDataModel _data;


    /**
     *
     */
    public FreemarkerDataModel(final TemplateDataModel data) {

        _data = data;
    }


    /**
     *
     */
    public String getBakeId() {

        return _data.getBakeId();
    }


    /**
     *
     */
    public String getBody() {

        return _data.getBody();
    }


    /**
     *
     */
    public String getPageUrl() {

        return _data.getPageUrl();
    }


    /**
     *
     */
    public String getProductName() {

        return _data.getProductName();
    }


    /**
     *
     */
    public String getRootRelativeUrl() {

        return _data.getRootRelativeUrl();
    }


    /**
     *
     */
    public String getTitle() {

        return _data.getTitle();
    }


    /**
     *
     */
    public String getVersion() {

        return _data.getVersion();
    }


    /**
     *
     */
    public Object get(final String key) {

        Object result = _data.getPageVars().get(key).orElse(null);

        return result;
    }


}
