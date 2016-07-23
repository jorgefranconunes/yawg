/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Iterator;
import java.util.ServiceLoader;


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
     *
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
