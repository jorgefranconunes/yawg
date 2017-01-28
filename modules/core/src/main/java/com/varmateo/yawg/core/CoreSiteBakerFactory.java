/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.SiteBaker;
import com.varmateo.yawg.SiteBakerConf;
import com.varmateo.yawg.SiteBakerFactory;

import com.varmateo.yawg.core.SingleSiteBaker;
import com.varmateo.yawg.core.SiteBakerDomain;


/**
 * Factory for <code>SiteBaker</code> objects.
 */
public final class CoreSiteBakerFactory
        implements SiteBakerFactory {


    /**
     * 
     */
    public CoreSiteBakerFactory() {
        // Nothing to do.
    }


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
