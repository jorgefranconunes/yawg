/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.SiteBakerFactory;


/**
 *
 */
public final class SiteBakerFactoryTest
 {


    /**
     *
     */
    @Test
    public void noService() {

        // The SiteBakerFactory interface is inside a JAR that does
        // not contain any service implementing the SiteBakerFactory
        // interface. And that is SiteBakerFactory.get() method is
        // supposed to fail the specific environment where this unit
        // test is run.

        TestUtils.assertThrows(
                IllegalStateException.class,
                () -> SiteBakerFactory.get());
    }


}
