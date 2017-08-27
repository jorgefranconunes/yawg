/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import com.varmateo.yawg.spi.DirBakeListener;


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
