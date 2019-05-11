/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.api.SiteBakerOptions;
import com.varmateo.yawg.api.SiteBakerFactory;
import com.varmateo.yawg.core.SingleSiteBaker;
import com.varmateo.yawg.core.SiteBakerModule;


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
        public void bake(final SiteBakerOptions options) {

            final SiteBakerModule domain = new SiteBakerModule(options);
            final SingleSiteBaker singleSiteBaker = domain.getSingleSiteBaker();

            singleSiteBaker.bake();
        }


    }


}
