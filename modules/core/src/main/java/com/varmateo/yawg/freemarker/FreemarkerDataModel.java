/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
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

        return _data.bakeId;
    }


    /**
     *
     */
    public String getBody() {

        return _data.body;
    }


    /**
     *
     */
    public String getTitle() {

        return _data.title;
    }


    /**
     *
     */
    public String getRootRelativeUrl() {

        return _data.rootRelativeUrl;
    }


    /**
     *
     */
    public String getProductName() {

        return _data.productName;
    }


    /**
     *
     */
    public String getVersion() {

        return _data.version;
    }


    /**
     *
     */
    public Object get(final String key) {

        Object result = _data.templateVars.get(key).orElse(null);

        return result;
    }


}
