/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests.api;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.api.YawgInfo;


/**
 *
 */
public final class YawgInfoIT {

    // This test is part of the "acceptance" sub-project because the
    // implementation of YawgInfo expects the JAR file with the
    // YawgInfo class to have a MANIFEST.MF with an
    // Implementation-Version entry.
    //
    // If this test was part of the "api" sub-project, the JAR file
    // would not have yet been built by the time Maven run tests (unit
    // or integration both). So this test is here actually because
    // Maven JUnit test runner is not that great.


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

        String key = YawgInfoIT.class.getSimpleName() + ".version";

        return TestUtils.getSystemProperty(key);
    }


}
