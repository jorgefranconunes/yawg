/**************************************************************************
 *
 * Copyright (c) 2015-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import javaslang.collection.HashSet;
import javaslang.collection.Set;

import com.varmateo.yawg.cli.util.CliException;
import com.varmateo.yawg.cli.util.CliOption;
import com.varmateo.yawg.cli.util.CliOptions;


/**
 * Represents the set of supported command line options.
 */
/* package private */ final class BakerCliOptions {


    public static final CliOption ASSETS_DIR =
            CliOption.builder()
            .setLongName("assets")
            .setArgName("PATH")
            .setDescription("path of assets directory")
            .build();

    public static final CliOption HELP =
            CliOption.builder()
            .setLongName("help")
            .setDescription("show this help text and exit")
            .setShortName("h")
            .build();

    public static final CliOption PAGE_VAR =
            CliOption.builder()
            .setLongName("page-var")
            .setArgName("NAME=VALUE")
            .setDescription("additional page variable")
            .build();

    public static final CliOption SOURCE_DIR =
            CliOption.builder()
            .setLongName("source")
            .setArgName("PATH")
            .setDescription("path of source directory")
            .build();

    public static final CliOption TARGET_DIR =
            CliOption.builder()
            .setLongName("target")
            .setArgName("PATH")
            .setDescription("path of target directory")
            .build();

    public static final CliOption TEMPLATES_DIR =
            CliOption.builder()
            .setLongName("templates")
            .setArgName("PATH")
            .setDescription("path of templates directory")
            .build();

    public static final CliOption VERBOSE =
            CliOption.builder()
            .setLongName("verbose")
            .setDescription("show abundant logging")
            .build();

    public static final CliOption VERSION =
            CliOption.builder()
            .setShortName("v")
            .setLongName("version")
            .setDescription("show version and exit")
            .build();


    /**
     *
     */
    private static final Set<CliOption> OPTIONS =
            HashSet.of(
                    ASSETS_DIR,
                    HELP,
                    PAGE_VAR,
                    SOURCE_DIR,
                    TARGET_DIR,
                    TEMPLATES_DIR,
                    VERBOSE,
                    VERSION);


    /**
     *
     */
    public static Set<CliOption> options() {

        return OPTIONS;
    }


    /**
     * No instances of this class are to be created.
     */
    private BakerCliOptions() {
    }


    /**
     *
     */
    public static CliOptions parse(final String[] args)
        throws CliException {

        CliOptions options = CliOptions.parse(OPTIONS, args);

        return options;
    }


}
