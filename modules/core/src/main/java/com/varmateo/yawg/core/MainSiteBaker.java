/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.api.SiteBaker;


/**
 *
 */
public final class MainSiteBaker {


    private MainSiteBaker() {
        // Nothing to do.
    }


    /**
     *
     */
    public static SiteBaker create() {

        final DefaultSiteBakerModule module = new DefaultSiteBakerModule();

        return module.siteBaker();
    }

}
