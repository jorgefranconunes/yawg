/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;


/**
 * Baker of sites.
 */
public interface SiteBaker {


    /**
     * Performs the baking of one directory tree into another.
     *
     * @param conf Information on the ingredients for the baking.
     *
     * @return A result signaling success of failure.
     */
    SiteBakeResult bake(BakeOptions conf);


}
