/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.YawgInfo;


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

        assertEquals(expectedVersion, actualVersion);
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
