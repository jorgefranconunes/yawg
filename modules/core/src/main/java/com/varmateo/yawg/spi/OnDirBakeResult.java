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
public interface OnDirBakeResult {

    /**
     *
     */
    boolean isSuccess();


    /**
     *
     */
    PageVars pageVars();


    /**
     *
     */
    YawgException failureCause();
}
