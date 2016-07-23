/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.BakerFactory;


/**
 *
 */
public final class HtmlBakerFactory
        extends Object
        implements BakerFactory {


    private final HtmlBaker _baker;


    /**
     *
     */
    public HtmlBakerFactory() {

        _baker = new HtmlBaker();
    }


    /**
     *
     */
    @Override
    public Baker newBaker() {

        return _baker;
    }


}
