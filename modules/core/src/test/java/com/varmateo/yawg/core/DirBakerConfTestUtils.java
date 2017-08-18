/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.util.GlobMatcher;


/**
 * Utility methods used by unit tests.
 */
/* package private */ final class DirBakerConfTestUtils
 {


    /**
     * No instances of this class are to be created.
     */
    private DirBakerConfTestUtils() {
        // Nothing to do.
    }


    /**
     *
     */
    public static void assertConfEquals(
            final DirBakerConf expectedConf,
            final DirBakerConf actualConf) {

        assertThat(actualConf.templateName)
                .isEqualTo(expectedConf.templateName);
        assertThat(actualConf.filesToExclude.map(GlobMatcher::toString))
                .isEqualTo(
                        expectedConf.filesToExclude.map(GlobMatcher::toString));
        assertThat(actualConf.filesToIncludeHere.map(GlobMatcher::toString))
                .isEqualTo(
                        expectedConf.filesToIncludeHere.map(GlobMatcher::toString));
        assertThat(actualConf.bakerTypes.map(BakerMatcher::toString))
                .isEqualTo(expectedConf.bakerTypes.map(BakerMatcher::toString));
    }


}
