/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import com.varmateo.yawg.SiteBakerConf;
import com.varmateo.yawg.YawgException;


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
