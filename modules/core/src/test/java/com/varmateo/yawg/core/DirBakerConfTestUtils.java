/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

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

        assertEquals(
                expectedConf.templateName,
                actualConf.templateName);
        assertEquals(
                expectedConf.filesToExclude.map(GlobMatcher::toString),
                actualConf.filesToExclude.map(GlobMatcher::toString));
        assertEquals(
                expectedConf.filesToIncludeHere.map(GlobMatcher::toString),
                actualConf.filesToIncludeHere.map(GlobMatcher::toString));
        assertEquals(
                expectedConf.bakerTypes.map(BakerMatcher::toString),
                actualConf.bakerTypes.map(BakerMatcher::toString));
    }


}