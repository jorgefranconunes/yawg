/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
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
                expectedConf.filesToIgnore.map(
                        c -> Lists.map(c, GlobMatcher::toString)),
                actualConf.filesToIgnore.map(
                        c -> Lists.map(c, GlobMatcher::toString)));
        assertEquals(
                expectedConf.filesToIncludeOnly.map(
                        c -> Lists.map(c, GlobMatcher::toString)),
                actualConf.filesToIncludeOnly.map(
                        c -> Lists.map(c, GlobMatcher::toString)));
    }


}