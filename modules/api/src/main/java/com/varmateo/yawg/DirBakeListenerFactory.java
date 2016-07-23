/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import com.varmateo.yawg.DirBakeListener;


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
