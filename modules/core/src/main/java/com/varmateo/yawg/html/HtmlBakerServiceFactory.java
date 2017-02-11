/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import com.varmateo.yawg.api.BakerService;
import com.varmateo.yawg.api.BakerServiceFactory;


/**
 *
 */
public final class HtmlBakerServiceFactory
        implements BakerServiceFactory {


    /**
     *
     */
    public HtmlBakerServiceFactory() {
        // Nothing to do.
    }


    /**
     *
     */
    @Override
    public BakerService newBakerService() {

        return new HtmlBakerService();
    }


}
