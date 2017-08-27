/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import com.varmateo.yawg.api.SiteBakerConf;
import com.varmateo.yawg.api.YawgException;


/**
 * Baker of sites.
 */
public interface SiteBaker {


    /**
     * Performs the baking of one directory tree into another.
     *
     * @param conf Information on the ingredients for the baking.
     *
     * @exception YawgException Thrown if the baking could not be
     * completed for whatever reason.
     */
    void bake(SiteBakerConf conf)
            throws YawgException;


}
