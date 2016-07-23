/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import java.util.Collection;

import com.varmateo.yawg.DirBakeListenerFactory;
import com.varmateo.yawg.breadcrumbs.BreadcrumbsBakeListenerFactory;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 *
 */
public final class BreadcrumbsBakeListenerFactoryTest
        extends Object {


    /**
     *
     */
    @Test
    public void loadAsSpi() {

        Collection<DirBakeListenerFactory> allFactories =
                DirBakeListenerFactory.getAllFactories();
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
