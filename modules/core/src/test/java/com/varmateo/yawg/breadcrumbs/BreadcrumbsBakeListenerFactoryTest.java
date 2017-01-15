/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import javaslang.collection.Seq;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.yawg.DirBakeListenerFactory;

import com.varmateo.yawg.breadcrumbs.BreadcrumbsBakeListenerFactory;
import com.varmateo.yawg.util.Services;


/**
 *
 */
public final class BreadcrumbsBakeListenerFactoryTest
 {


    /**
     *
     */
    @Test
    public void loadAsSpi() {

        Seq<DirBakeListenerFactory> allFactories =
                Services.getAll(DirBakeListenerFactory.class);
        boolean isFound = false;

        for ( DirBakeListenerFactory factory : allFactories ) {
            if ( factory instanceof BreadcrumbsBakeListenerFactory ) {
                isFound = true;
                break;
            }
        }

        assertTrue(isFound);
    }


}
