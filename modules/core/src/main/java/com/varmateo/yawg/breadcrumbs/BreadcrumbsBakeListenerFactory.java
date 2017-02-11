/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import com.varmateo.yawg.api.DirBakeListener;
import com.varmateo.yawg.api.DirBakeListenerFactory;
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
