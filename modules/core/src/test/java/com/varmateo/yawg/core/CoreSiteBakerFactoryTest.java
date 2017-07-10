/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.varmateo.yawg.core.CoreSiteBakerFactory;
import com.varmateo.yawg.spi.SiteBakerFactory;


/**
 *
 */
public final class CoreSiteBakerFactoryTest
 {


    /**
     *
     */
    @Test
    public void loadAsSpi() {

        SiteBakerFactory factory = SiteBakerFactory.get();

        assertThat(factory).isNotNull();
        assertThat(factory).isInstanceOf(CoreSiteBakerFactory.class);
    }


}
