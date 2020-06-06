/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
public interface PageBakeResult {

    /**
     *
     */
    boolean isSuccess();


    /**
     *
     */
    YawgException failureCause();
}
