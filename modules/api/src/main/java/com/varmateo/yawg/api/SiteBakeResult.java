/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;


/**
 *
 */
public interface SiteBakeResult {

    /**
     *
     */
    boolean isSuccess();


    /**
     *
     */
    YawgException failureCause();
}
