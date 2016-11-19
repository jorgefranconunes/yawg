/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli.util;

import org.junit.Test;
import static org.junit.Assert.*;

import com.varmateo.yawg.cli.util.CliOption;


/**
 *
 */
public final class CliOptionTest
 {


    /**
     *
     */
    @Test
    public void withoutArg() {

        CliOption option =
            CliOption.builder()
            .setShortName("demo")
            .build();

        assertEquals("demo", option.getShortName());
        assertFalse(option.isWithArg());
    }


    /**
     *
     */
    @Test
    public void withArg() {

        CliOption option =
            CliOption.builder()
            .setShortName("demo")
            .setArgName("something")
            .build();

        assertEquals("demo", option.getShortName());
        assertTrue(option.isWithArg());
        assertEquals("something", option.getArgName());
    }


    /**
     *
     */
    @Test
    public void shortName() {

        CliOption option =
            CliOption.builder()
            .setShortName("s")
            .build();

        assertEquals("s", option.getShortName());
        assertNull(option.getLongName());
        assertEquals("s", option.getName());
        assertEquals("-s", option.getLiteral());
    }


    /**
     *
     */
    @Test
    public void longName() {

        CliOption option =
            CliOption.builder()
            .setShortName("s")
            .setLongName("something")
            .build();

        assertEquals("s", option.getShortName());
        assertEquals("something", option.getLongName());
        assertEquals("something", option.getName());
        assertEquals("--something", option.getLiteral());
    }


}
