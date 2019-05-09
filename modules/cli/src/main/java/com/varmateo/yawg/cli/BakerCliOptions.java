/**************************************************************************
 *
 * Copyright (c) 2015-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import com.varmateo.yawg.cli.CliException;
import com.varmateo.yawg.cli.CliOption;
import com.varmateo.yawg.cli.CliOptionSet;


/**
 * Represents the set of supported command line options.
 */
final class BakerCliOptions {


    public static final CliOption ASSETS_DIR = CliOption.builder()
            .longName("assets")
            .argName("PATH")
            .description("path of assets directory")
            .build();

    public static final CliOption HELP = CliOption.builder()
            .longName("help")
            .description("show this help text and exit")
            .shortName("h")
            .build();

    public static final CliOption PAGE_VAR = CliOption.builder()
            .longName("page-var")
            .argName("NAME=VALUE")
            .description("additional page variable")
            .build();

    public static final CliOption SOURCE_DIR = CliOption.builder()
            .longName("source")
            .argName("PATH")
            .description("path of source directory")
            .build();

    public static final CliOption TARGET_DIR = CliOption.builder()
            .longName("target")
            .argName("PATH")
            .description("path of target directory")
            .build();

    public static final CliOption TEMPLATES_DIR = CliOption.builder()
            .longName("templates")
            .argName("PATH")
            .description("path of templates directory")
            .build();

    public static final CliOption VERBOSE = CliOption.builder()
            .longName("verbose")
            .description("show abundant logging")
            .build();

    public static final CliOption VERSION = CliOption.builder()
            .shortName("v")
            .longName("version")
            .description("show version and exit")
            .build();


    /**
     *
     */
    private static final Set<CliOption> ALL_OPTIONS =
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

        return ALL_OPTIONS;
    }


    /**
     * No instances of this class are to be created.
     */
    private BakerCliOptions() {
    }


    /**
     *
     */
    public static CliOptionSet parse(final String[] args)
        throws CliException {

        return CliOptionSet.parse(ALL_OPTIONS, args);
    }


}
