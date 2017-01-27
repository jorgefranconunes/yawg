/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import com.varmateo.yawg.BakerService;
import com.varmateo.yawg.BakerServiceFactory;


/**
 *
 */
public final class HtmlBakerServiceFactory
        implements BakerServiceFactory {


    private final HtmlBakerService _baker;


    /**
     *
     */
    public HtmlBakerServiceFactory() {

        _baker = new HtmlBakerService();
    }


    /**
     *
     */
    @Override
    public BakerService newBakerService() {

        return _baker;
    }


}
