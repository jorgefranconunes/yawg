/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.util.Arrays;
import java.util.Collection;

import com.varmateo.yawg.cli.util.CliException;
import com.varmateo.yawg.cli.util.CliOption;
import com.varmateo.yawg.cli.util.CliOptions;





/**
 * Represents the set of supported command line options.
 */
/* package private */ final class BakerCliOptions
    extends Object {


    public static final CliOption ASSETS_DIR =
        new CliOption.Builder()
        .setLongName("assets")
        .setArgName("PATH")
        .setDescription("path of assets directory")
        .build();

    public static final CliOption HELP =
        new CliOption.Builder()
        .setLongName("help")
        .setDescription("show this help text and exit")
        .setShortName("h")
        .build();

    public static final CliOption SOURCE_DIR =
        new CliOption.Builder()
        .setLongName("source")
        .setArgName("PATH")
        .setDescription("path of source directory")
        .build();

    public static final CliOption TARGET_DIR =
        new CliOption.Builder()
        .setLongName("target")
        .setArgName("PATH")
        .setDescription("path of target directory")
        .build();

    public static final CliOption TEMPLATES_DIR =
        new CliOption.Builder()
        .setLongName("templates")
        .setArgName("PATH")
        .setDescription("path of templates directory")
        .build();

    public static final CliOption VERBOSE =
        new CliOption.Builder()
        .setLongName("verbose")
        .setDescription("show abundant logging")
        .build();

    public static final CliOption VERSION =
        new CliOption.Builder()
        .setShortName("v")
        .setLongName("version")
        .setDescription("show version and exit")
        .build();





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    private static final Collection<CliOption> OPTIONS;

    static {
        CliOption[] allOptions = {
            ASSETS_DIR,
            HELP,
            SOURCE_DIR,
            TARGET_DIR,
            TEMPLATES_DIR,
            VERBOSE,
            VERSION,
        };

        OPTIONS = Arrays.asList(allOptions);
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    public static Collection<CliOption> options() {

        return OPTIONS;
    }





/**************************************************************************
 *
 * No instances of this class are to be created.
 *
 **************************************************************************/

    private BakerCliOptions() {
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    public static CliOptions parse(final String[] args)
        throws CliException {

        CliOptions options = CliOptions.parse(OPTIONS, args);

        return options;
    }


}
