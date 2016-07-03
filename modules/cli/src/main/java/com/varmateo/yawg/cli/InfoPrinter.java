/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.PrintWriter;
import java.text.MessageFormat;

import com.varmateo.yawg.YawgInfo;

import com.varmateo.yawg.cli.BakerCliOptions;
import com.varmateo.yawg.cli.util.CliException;
import com.varmateo.yawg.cli.util.CliInfoPrinter;


/**
 * Provides utility methods for printing help and varied info intended
 * for end users.
 */
final class InfoPrinter
    extends Object {


    private static final String TOOL_NAME = "CLI Baker";

    private static final String PRODUCT_HEADER_FMT = ""
        + "\n"
        + "{0} {1} - {2}\n"
        + "{3}\n";
    private static final String PRODUCT_HEADER =
        MessageFormat.format(PRODUCT_HEADER_FMT,
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

    private PrintWriter    _output         = null;
    private CliInfoPrinter _cliInfoPrinter = null;


    /**
     *
     */
    public InfoPrinter(final Builder builder) {

        String argv0 = builder._argv0;

        _output = builder._output;
        _cliInfoPrinter =
            new CliInfoPrinter.Builder()
            .setArgv0(argv0)
            .setVersionMessage(PRODUCT_HEADER)
            .setUsageMessageHeader(USAGE_HEADER_FMT)
            .setUsageMessageFooter(USAGE_FOOTER)
            .build();
    }


    /**
     *
     */
    public void printHelp() {

        _cliInfoPrinter.printUsage(_output, BakerCliOptions.options());
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
    public void printError(final CliException error) {

        _cliInfoPrinter.printError(_output, error);
    }


    /**
     *
     */
    public static final class Builder
        extends Object {


        private String      _argv0  = null;
        private PrintWriter _output = null;


        /**
         *
         */
        public Builder() {
            // Nothing to do.
        }


        /**
         *
         */
        public Builder setArgv0(final String argv0) {

            _argv0 = argv0;

            return this;
        }


        /**
         *
         */
        public Builder setOutput(final PrintWriter output) {

            _output = output;

            return this;
        }


        /**
         *
         */
        public InfoPrinter build() {

            InfoPrinter result = new InfoPrinter(this);

            return result;
        }


    }


}