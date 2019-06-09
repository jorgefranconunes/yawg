/**************************************************************************
 *
 * Copyright (c) 2015-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Try;


/**
 * Represents the set of supported command line options.
 */
final class BakerCliParameters {


    public static final CliParameter ASSETS_DIR = CliParameter.builder()
            .longName("assets")
            .argName("PATH")
            .description("path of assets directory")
            .build();

    public static final CliParameter HELP = CliParameter.builder()
            .longName("help")
            .description("show this help text and exit")
            .shortName("h")
            .build();

    public static final CliParameter PAGE_VAR = CliParameter.builder()
            .longName("page-var")
            .argName("NAME=VALUE")
            .description("additional page variable")
            .build();

    public static final CliParameter SOURCE_DIR = CliParameter.builder()
            .longName("source")
            .argName("PATH")
            .description("path of source directory")
            .build();

    public static final CliParameter TARGET_DIR = CliParameter.builder()
            .longName("target")
            .argName("PATH")
            .description("path of target directory")
            .build();

    public static final CliParameter TEMPLATES_DIR = CliParameter.builder()
            .longName("templates")
            .argName("PATH")
            .description("path of templates directory")
            .build();

    public static final CliParameter VERBOSE = CliParameter.builder()
            .longName("verbose")
            .description("show abundant logging")
            .build();

    public static final CliParameter VERSION = CliParameter.builder()
            .shortName("v")
            .longName("version")
            .description("show version and exit")
            .build();


    /**
     *
     */
    private static final Set<CliParameter> ALL_OPTIONS = HashSet.of(
            ASSETS_DIR,
            HELP,
            PAGE_VAR,
            SOURCE_DIR,
            TARGET_DIR,
            TEMPLATES_DIR,
            VERBOSE,
            VERSION);


    /**
     * No instances of this class are to be created.
     */
    private BakerCliParameters() {
        // Nothin to do.
    }


    /**
     *
     */
    public static Set<CliParameter> options() {

        return ALL_OPTIONS;
    }


    /**
     *
     */
    public static Try<CliParameterSet> parse(final String[] args) {

        return Try.of(() -> CliParameterSet.parse(ALL_OPTIONS, args));
    }

}
