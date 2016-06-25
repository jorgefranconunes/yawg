/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import com.varmateo.yawg.PageTemplateDataModel;


/**
 * Java bean for passing data to the Freemarker template engine.
 */
public final class FreemarkerDataModel
        extends Object {


    private final PageTemplateDataModel _data;


    /**
     *
     */
    public FreemarkerDataModel(final PageTemplateDataModel data) {

        _data = data;
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


}
