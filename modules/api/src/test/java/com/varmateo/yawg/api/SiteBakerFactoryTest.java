/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.varmateo.yawg.api.SiteBakerFactory;


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

        assertThatThrownBy(() -> SiteBakerFactory.get())
                .isInstanceOf(IllegalStateException.class);
    }


}
