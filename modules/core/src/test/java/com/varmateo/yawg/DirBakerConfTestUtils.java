/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

import com.varmateo.yawg.DirBakerConf;
import com.varmateo.yawg.util.GlobMatcher;
import com.varmateo.yawg.util.Lists;


/**
 * Utility methods used by unit tests.
 */
/* package private */ final class DirBakerConfTestUtils
        extends Object {


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
                expectedConf.filesToIgnore.map(GlobMatcher::toString),
                actualConf.filesToIgnore.map(GlobMatcher::toString));
        assertEquals(
                expectedConf.filesToIncludeOnly.map(GlobMatcher::toString),
                actualConf.filesToIncludeOnly.map(GlobMatcher::toString));
        assertEquals(
                expectedConf.bakerTypes.map(BakerMatcher::toString),
                actualConf.bakerTypes.map(BakerMatcher::toString));
    }


}
