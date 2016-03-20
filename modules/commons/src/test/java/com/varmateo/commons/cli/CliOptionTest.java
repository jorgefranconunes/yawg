/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

import com.varmateo.commons.cli.CliOption;


/**
 *
 */
public final class CliOptionTest
    extends Object {


    /**
     *
     */
    @Test
    public void withoutArg() {

        CliOption option =
            new CliOption.Builder()
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
            new CliOption.Builder()
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
            new CliOption.Builder()
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
            new CliOption.Builder()
            .setShortName("s")
            .setLongName("something")
            .build();

        assertEquals("s", option.getShortName());
        assertEquals("something", option.getLongName());
        assertEquals("something", option.getName());
        assertEquals("--something", option.getLiteral());
    }


}