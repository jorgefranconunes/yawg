/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.cli.util;

import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Collection;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.varmateo.yawg.cli.util.CliException;
import com.varmateo.yawg.cli.util.CliOption;


/**
 * Provides utility methods for printing help and varied info intended
 * for end users.
 */
public final class CliInfoPrinter
    extends Object {


    private static final String DEFAULT_ARGV0 = "";
    private static final String DEFAULT_VERSION = "";
    private static final String DEFAULT_USAGE_HEADER = "";
    private static final String DEFAULT_USAGE_FOOTER = ""
        + "\n";

    private static final String ERROR_FMT = ""
        + "\n"
        + "{0}: {1}\n";

    private String _argv0 = null;
    private String _versionMessage = null;
    private String _usageMessageHeader = null;
    private String _usageMessageFooter = null;


    /**
     * Only used internally.
     *
     */
    private CliInfoPrinter(final Builder builder) {

        _argv0 = builder._argv0;
        _versionMessage = builder._versionMessage;
        _usageMessageHeader = builder._usageMessageHeader;
        _usageMessageFooter = builder._usageMessageFooter;
    }


    /**
     *
     */
    public void printUsage(
            final PrintWriter output,
            final Collection<CliOption> options) {

        String header = MessageFormat.format(_usageMessageHeader, _argv0);
        String footer = _usageMessageFooter;

        output.println(header);

        Options       apacheOptions = buildApacheOptions(options);
        HelpFormatter formatter = new HelpFormatter();

        formatter.printOptions(
                output,
                HelpFormatter.DEFAULT_WIDTH,
                apacheOptions,
                HelpFormatter.DEFAULT_LEFT_PAD,
                HelpFormatter.DEFAULT_DESC_PAD);

        output.println(footer);
    }


    /**
     *
     */
    private static Options
        buildApacheOptions(final Collection<CliOption> options) {

        Options apacheOptions = new Options();

        options.stream()
            .map(CliOption::apacheOption)
            .forEach(o -> apacheOptions.addOption(o));

        return apacheOptions;
    }


    /**
     *
     */
    public void printVersion(final PrintWriter output) {

        String version = _versionMessage;

        output.println(version);
    }


    /**
     *
     */
    public void printError(
            final PrintWriter  output,
            final CliException error) {

        String errorMsg = error.getMessage();
        String msg      = MessageFormat.format(ERROR_FMT, _argv0, errorMsg);

        output.println(msg);
    }


    /**
     * A builder of <code>CliInfoPrinter</code> instances.
     */
    public static final class Builder
        extends Object {


        private String _argv0 = DEFAULT_ARGV0;
        private String _versionMessage = DEFAULT_VERSION;
        private String _usageMessageHeader = DEFAULT_USAGE_HEADER;
        private String _usageMessageFooter = DEFAULT_USAGE_FOOTER;


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
        public Builder setVersionMessage(final String versionMessage) {

            _versionMessage = versionMessage;

            return this;
        }


        /**
         *
         */
        public Builder setUsageMessageHeader(final String usageMessageHeader) {

            _usageMessageHeader = usageMessageHeader;

            return this;
        }


        /**
         *
         */
        public Builder setUsageMessageFooter(final String usageMessageFooter) {

            _usageMessageFooter = usageMessageFooter;

            return this;
        }


        /**
         *
         */
        public CliInfoPrinter build() {

            CliInfoPrinter result = new CliInfoPrinter(this);

            return result;
        }


    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

