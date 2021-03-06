/**************************************************************************
 *
 * Copyright (c) 2015-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.PrintWriter;
import java.text.MessageFormat;

import com.varmateo.yawg.api.YawgInfo;


/**
 * Provides utility methods for printing help and varied info intended
 * for end users.
 */
/* package private */ final class InfoPrinter {


    private static final String TOOL_NAME = "CLI Baker";

    private static final String PRODUCT_HEADER_FMT = ""
        + "\n"
        + "{0} {1} - {2}\n"
        + "{3}\n";
    private static final String PRODUCT_HEADER = MessageFormat.format(
            PRODUCT_HEADER_FMT,
            YawgInfo.PRODUCT_NAME,
            YawgInfo.VERSION,
            TOOL_NAME,
            YawgInfo.COPYRIGHT_HEADER);

    private static final String USAGE_HEADER_FMT = ""
        + PRODUCT_HEADER
        + "\n"
        + "Usage: {0} [OPTION] ...\n"
        + "\n"
        + "Bakes a site from a directory tree.\n"
        +" \n"
        + "Options:";

    private static final String USAGE_FOOTER = ""
            + "\n"
            + "Find additional information at http://yawg.varmateo.com/\n";

    private final PrintWriter    _output;
    private final CliInfoPrinter _cliInfoPrinter;


    /**
     *
     */
    private InfoPrinter(final Builder builder) {

        final String argv0 = builder._argv0;

        _output = builder._output;
        _cliInfoPrinter = CliInfoPrinter.builder()
                .argv0(argv0)
                .versionMessage(PRODUCT_HEADER)
                .usageMessageHeader(USAGE_HEADER_FMT)
                .usageMessageFooter(USAGE_FOOTER)
                .build();
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    public void printHelp() {

        _cliInfoPrinter.printUsage(_output, BakerCliParameters.options());
    }


    /**
     *
     */
    public void printVersion() {

        _cliInfoPrinter.printVersion(_output);
    }


    /**
     *
     */
    public void printError(final Throwable error) {

        _cliInfoPrinter.printError(_output, error);
    }


    /**
     *
     */
    public static final class Builder {


        private String      _argv0  = null;
        private PrintWriter _output = null;


        /**
         *
         */
        private Builder() {
            // Nothing to do.
        }


        /**
         *
         */
        public Builder argv0(final String argv0) {

            _argv0 = argv0;

            return this;
        }


        /**
         *
         */
        public Builder output(final PrintWriter output) {

            _output = output;

            return this;
        }


        /**
         *
         */
        public InfoPrinter build() {

            return new InfoPrinter(this);
        }


    }


}
