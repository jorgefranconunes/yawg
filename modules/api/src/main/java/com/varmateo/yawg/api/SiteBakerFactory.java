/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.varmateo.yawg.api.SiteBaker;


/**
 * Factory for <code>SiteBaker</code> objects.
 */
public interface SiteBakerFactory {


    /**
     * Creates a new <code>SiteBake</code> instance.
     *
     * @return The baker object.
     */
    SiteBaker newSiteBaker();


    /**
     * Retrieves the default <code>SiteBakerFactory</code>
     * implementation that is always available. This is the main entry
     * point for baking a site.
     *
     * @return The default factory instance.
     */
    static SiteBakerFactory get() {

        ServiceLoader<SiteBakerFactory> loader =
                ServiceLoader.load(SiteBakerFactory.class);
        Iterator<SiteBakerFactory> allFactories = loader.iterator();
        SiteBakerFactory result =
                allFactories.hasNext() ? allFactories.next() : null;

        if ( result == null ) {
            String msg = ""
                    + "No \""
                    + SiteBakerFactory.class.getName()
                    + "\" service available";
            throw new IllegalStateException(msg);
        }

        return result;
    }


}
