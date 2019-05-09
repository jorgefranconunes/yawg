/**************************************************************************
 *
 * Copyright (c) 2015-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.text.MessageFormat;


/**
 * An exception signaling an occurrence intended to be communicated to
 * the end user.
 */
public final class CliException
        extends Exception {


    /**
     *
     */
    private CliException(final String msg) {

        super(msg);
    }


    /**
     *
     */
    private CliException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     *
     */
    public static CliException bakeFailure(final Throwable cause) {

        final String msg = String.format("Baking failed - %s", cause.getMessage());

        return new CliException(msg, cause);
    }


    /**
     *
     */
    public static CliException unknownOption(final String optionName) {

        final String msg = String.format("Unknown option \"%s\"", optionName);

        return new CliException(msg);
    }


    /**
     *
     */
    public static CliException missingOption(final String optionName) {

        final String msg = String.format("Missing mandatory option %s", optionName);

        return new CliException(msg);
    }


    /**
     *
     */
    public static CliException missingOptionArg(final String optionName) {

        final String msg = String.format("Argument for option %s is missing", optionName);

        return new CliException(msg);
    }


    /**
     *
     */
    public static CliException optionParseFailure(final Throwable cause) {

        final String msg = String.format(
                "Failed to parse options - %s", cause.getClass().getName());

        return new CliException(msg, cause);
    }


    /**
     *
     */
    public static CliException invalidPath(
            final String optionName,
            final String path) {

        final String msg = String.format(
                "Value of option %s is an invalid path (%s)", optionName, path);

        return new CliException(msg);
    }


}
