/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import com.varmateo.yawg.spi.TemplateDataModel;


/**
 * Java bean for passing data to the Freemarker template engine.
 */
public final class FreemarkerDataModel {


    private static final String KEY_YAWG_DATA = "yawg";


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
    public Object get(final String key) {

        Object result =
                KEY_YAWG_DATA.equals(key)
                ? _data
                : _data.getPageVars().get(key).orElse(null);

        return result;
    }


}
