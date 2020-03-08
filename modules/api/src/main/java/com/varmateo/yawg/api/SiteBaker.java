/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;


/**
 * Baker of static sites.
 */
public interface SiteBaker {


    /**
     * Performs the baking of one directory tree into another.
     *
     * @param options Information on the ingredients for the baking.
     *
     * @return A result signaling success of failure.
     */
    Result<Void> bake(BakeOptions options);


}
