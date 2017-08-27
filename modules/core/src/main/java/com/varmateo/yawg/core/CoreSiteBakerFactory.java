/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.api.SiteBakerConf;
import com.varmateo.yawg.api.SiteBakerFactory;
import com.varmateo.yawg.core.SingleSiteBaker;
import com.varmateo.yawg.core.SiteBakerDomain;


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

        return new SiteBakerImpl();
    }


    /**
     *
     */
    private static final class SiteBakerImpl
            implements SiteBaker {


        /**
         *
         */
        @Override
        public void bake(final SiteBakerConf conf) {

            SiteBakerDomain domain = new SiteBakerDomain(conf);
            SingleSiteBaker singleSiteBaker = domain.getSingleSiteBaker();

            singleSiteBaker.bake();
        }


    }


}
