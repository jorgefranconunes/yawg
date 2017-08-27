/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import com.varmateo.yawg.spi.BakerService;
import com.varmateo.yawg.spi.BakerServiceFactory;


/**
 *
 */
public final class HtmlBakerServiceFactory
        implements BakerServiceFactory {


    /**
     *
     */
    @Override
    public BakerService newBakerService() {

        return new HtmlBakerService();
    }


}
