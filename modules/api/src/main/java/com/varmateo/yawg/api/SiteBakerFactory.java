/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import com.varmateo.yawg.api.SiteBaker;


/**
 * Factory for <code>SiteBaker</code> objects.
 */
public interface SiteBakerFactory {


    /**
     * Creates a new <code>SiteBaker</code> instance.
     *
     * @return The baker object.
     */
    SiteBaker newSiteBaker();


}
