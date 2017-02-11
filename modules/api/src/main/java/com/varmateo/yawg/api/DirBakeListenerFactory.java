/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import com.varmateo.yawg.api.DirBakeListener;


/**
 * A factory of <code>DirBakeListener</code> instances.
 */
public interface DirBakeListenerFactory {


    /**
     * Returns a <code>DirBakeListener</code> object.
     *
     * @return A newly created <code>DirBakeListener</code> instance.
     */
    DirBakeListener newDirBakeListener();


}
