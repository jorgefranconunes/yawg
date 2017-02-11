/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.yawg.core.CoreSiteBakerFactory;
import com.varmateo.yawg.spi.SiteBakerFactory;


/**
 *
 */
public final class CoreSiteBakerFactoryTest
 {


    /**
     *
     */
    @Test
    public void loadAsSpi() {

        SiteBakerFactory factory = SiteBakerFactory.get();

        assertNotNull(factory);
        assertTrue(factory instanceof CoreSiteBakerFactory);
    }


}
