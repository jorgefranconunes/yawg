/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.api.BakeOptions;
import com.varmateo.yawg.api.SiteBakerFactory;


/**
 * Factory for <code>SiteBaker</code> objects.
 */
public final class CoreSiteBakerFactory
        implements SiteBakerFactory {


    /**
     * Creates a new <code>SiteBaker</code> instance.
     *
     * @return The baker object.
     */
    @Override
    public SiteBaker newSiteBaker() {

        return CoreSiteBaker.create();
    }


}
