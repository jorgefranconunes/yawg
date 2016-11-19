/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.yawg.atests.BakerRunner;


/**
 *
 */
public final class CliOptionsIT
        extends Object {


    /**
     *
     */
    @Test
    public void noArgs() {

        BakerRunner baker = BakerRunner.builder().build();
        int actualExitStatus = baker.run();

        assertEquals(0, actualExitStatus);
        // TBD - Check the first non-empty line contains the version number.
    }


    /**
     *
     */
    @Test
    public void unknownOption() {

        BakerRunner baker =
                BakerRunner.builder()
                .addArgs("--this-is-an-unknown-option")
                .build();
        int actualExitStatus = baker.run();

        assertNotEquals(0, actualExitStatus);
        // TBD - Check the error message contains "unknown option"
    }


}
