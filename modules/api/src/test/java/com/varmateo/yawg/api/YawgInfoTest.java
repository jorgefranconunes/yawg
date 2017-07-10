/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.api.YawgInfo;


/**
 *
 */
public final class YawgInfoTest
 {


    /**
     *
     */
    @Test
    public void checkVersion() {

        String expectedVersion = getYawgVersion();
        String actualVersion = YawgInfo.VERSION;

        assertThat(actualVersion).isEqualTo(expectedVersion);
    }


    /**
     *
     */
    private String getYawgVersion() {

        String key = YawgInfoTest.class.getSimpleName() + ".version";
        String version = TestUtils.getSystemProperty(key);

        return version;
    }


}
