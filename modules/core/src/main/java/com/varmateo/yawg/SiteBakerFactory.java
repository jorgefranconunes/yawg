/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import com.varmateo.yawg.SingleSiteBaker;
import com.varmateo.yawg.SiteBaker;
import com.varmateo.yawg.SiteBakerConf;
import com.varmateo.yawg.SiteBakerDomain;


/**
 * Factory for <code>SiteBaker</code> objects.
 */
public final class SiteBakerFactory
        extends Object {


    /**
     * @param conf Configuration parameters.
     */
    public SiteBakerFactory() {
        // Nothing to do.
    }


    /**
     * @return The baker object.
     */
    public SiteBaker newSiteBaker() {

        SiteBaker result = new SiteBakerImpl();

        return result;
    }


    /**
     *
     */
    private static final class SiteBakerImpl
            extends Object
            implements SiteBaker {


        /**
         *
         */
        public SiteBakerImpl() {
            // Nothing to do.
        }


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
