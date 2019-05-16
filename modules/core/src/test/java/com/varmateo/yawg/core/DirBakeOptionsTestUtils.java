/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.varmateo.yawg.core.DirBakeOptions;
import com.varmateo.yawg.util.GlobMatcher;


/**
 * Utility methods used by unit tests.
 */
/* package private */ final class DirBakeOptionsTestUtils
 {


    /**
     * No instances of this class are to be created.
     */
    private DirBakeOptionsTestUtils() {
        // Nothing to do.
    }


    /**
     *
     */
    public static void assertConfEquals(
            final DirBakeOptions expectedConf,
            final DirBakeOptions actualConf) {

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
