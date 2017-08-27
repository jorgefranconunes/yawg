/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import com.varmateo.yawg.breadcrumbs.BreadcrumbsBakeListener;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.DirBakeListenerFactory;


/**
 *
 */
public final class BreadcrumbsBakeListenerFactory
        implements DirBakeListenerFactory {


    /**
     *
     */
    @Override
    public DirBakeListener newDirBakeListener() {

        DirBakeListener result = new BreadcrumbsBakeListener();

        return result;
    }


}
