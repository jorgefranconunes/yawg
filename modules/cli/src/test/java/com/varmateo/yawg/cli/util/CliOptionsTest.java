/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.cli.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import static org.junit.Assert.*;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.cli.util.CliOption;
import com.varmateo.yawg.cli.util.CliOptions;


/**
 *
 */
public final class CliOptionsTest
    extends Object {


    private static final CliOption OPT1 =
        new CliOption.Builder()
        .setLongName("option1")
        .setDescription("This is option one")
        .setShortName("o1")
        .build();

    private static final String OPT_WITH_ARG_NAME = "option-with-arg";
    private static final String OPT_WITH_ARG_OPT = "--" + OPT_WITH_ARG_NAME;
    private static final CliOption OPT_WITH_ARG =
        new CliOption.Builder()
        .setLongName(OPT_WITH_ARG_NAME)
        .setDescription("This is option has one argument")
        .setArgName("ARG")
        .build();

    private static final String OPT_NO_ARG_NAME = "option-with-no-arg";
    private static final String OPT_NO_ARG_OPT = "--" + OPT_WITH_ARG_NAME;
    private static final CliOption OPT_NO_ARG =
        new CliOption.Builder()
        .setLongName(OPT_WITH_ARG_NAME)
        .setDescription("This is option has one argument")
        .build();


    @Test
    public void parseNoArgsNoOptions()
        throws CliException {

        Collection<CliOption> options = Collections.emptyList();
        String[] args = {};
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertEquals(0, cliOptions.supportedOptions().size());
    }


    @Test
    public void parseArgsNoOptions() {

        Collection<CliOption> options = Collections.emptyList();
        String[] args = { "--something" };

        TestUtils.assertThrows(CliException.class,
                               () -> CliOptions.parse(options, args));
    }


    @Test
    public void parseMissingOptionArg() {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT };

        TestUtils.assertThrows(CliException.class,
                               () -> CliOptions.parse(options, args));
    }


    @Test
    public void parseTooMany() {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "something", "else" };

        TestUtils.assertThrows(CliException.class,
                               () -> CliOptions.parse(options, args));
    }


    @Test
    public void getStringMandatoryOk()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "something" };
        CliOptions cliOptions = CliOptions.parse(options, args);
        String actualValue = cliOptions.get(OPT_WITH_ARG);

        assertEquals("something", actualValue);
    }


    @Test
    public void getStringMandatoryMissing()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = {};
        CliOptions cliOptions = CliOptions.parse(options, args);

        TestUtils.assertThrows(CliException.class,
                               () -> cliOptions.get(OPT_WITH_ARG));
    }


    @Test
    public void getDefaultWithValue()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "something" };
        CliOptions cliOptions = CliOptions.parse(options, args);
        String actualValue = cliOptions.get(OPT_WITH_ARG, "whatever");

        assertEquals("something", actualValue);
    }


    @Test
    public void getDefaultWithoutValue()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = {};
        CliOptions cliOptions = CliOptions.parse(options, args);
        String actualValue = cliOptions.get(OPT_WITH_ARG, "whatever");

        assertEquals("whatever", actualValue);
    }


    @Test
    public void getPath()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "this/is/a/path" };
        CliOptions cliOptions = CliOptions.parse(options, args);
        Path actualPath = cliOptions.getPath(OPT_WITH_ARG);
        Path expectedPath = Paths.get("this", "is", "a", "path");

        assertEquals(expectedPath, actualPath);
    }


    @Test
    public void isTrueWithArg()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "true" };
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertTrue(cliOptions.isTrue(OPT_WITH_ARG));
    }


    @Test
    public void isFalseWithArg()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "false" };
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertFalse(cliOptions.isTrue(OPT_WITH_ARG));
    }


    @Test
    public void isTrueNoArg()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_NO_ARG);
        String[] args = { OPT_NO_ARG_OPT };
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertTrue(cliOptions.isTrue(OPT_NO_ARG));
    }


    @Test
    public void isFalseNoArg()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_NO_ARG);
        String[] args = {};
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertFalse(cliOptions.isTrue(OPT_NO_ARG));
    }


}