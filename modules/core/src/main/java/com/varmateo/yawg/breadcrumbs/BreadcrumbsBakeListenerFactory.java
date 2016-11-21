/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.DirBakeListenerFactory;
import com.varmateo.yawg.breadcrumbs.BreadcrumbsBakeListener;


/**
 *
 */
public final class BreadcrumbsBakeListenerFactory
        implements DirBakeListenerFactory {


    /**
     *
     */
    public BreadcrumbsBakeListenerFactory() {
        // Nothing to do.
    }


    /**
     *
     */
    @Override
    public DirBakeListener newDirBakeListener() {

        DirBakeListener result = new BreadcrumbsBakeListener();

        return result;
    }


}
