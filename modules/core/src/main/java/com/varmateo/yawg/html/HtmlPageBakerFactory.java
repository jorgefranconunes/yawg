/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageBakerFactory;


/**
 *
 */
public final class HtmlPageBakerFactory
        implements PageBakerFactory {


    /**
     *
     */
    @Override
    public PageBaker createPageBaker() {

        return new HtmlPageBaker();
    }


}
