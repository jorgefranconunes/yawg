/**************************************************************************
 *
 * Copyright (c) 2015-2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.SiteBaker;
import com.varmateo.yawg.SiteBakerConf;
import com.varmateo.yawg.SiteBakerFactory;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.commons.util.Lists;
import com.varmateo.yawg.logging.PlainFormatter;

import com.varmateo.yawg.cli.InfoPrinter;
import com.varmateo.yawg.cli.BakerCliOptions;
import com.varmateo.yawg.cli.util.CliException;
import com.varmateo.yawg.cli.util.CliOptions;


/**
 * Command line interface for the baker utility.
 */
public final class BakerCli {


    private static final int EXIT_STATUS_OK = 0;
    private static final int EXIT_STATUS_FAILURE = 1;

    private static final String LOG_FMT_CONSOLE =
            "{0,date,yyyy-MM.dd HH:mm:ss.SSS} {1} {2}\n";

    private static final String DEFAULT_ARGV0 = "yawg";


    private final String _argv0;
    private final String[] _args;
    private final OutputStream _output;


    /**
     * Creates a newly initialized builder for creating a
     * <code>BakerCli</code> instance.
     *
     * @return A new builder.
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
    }


    /**
     *
     */
    private BakerCli(final Builder builder) {

        _argv0 = builder._argv0;
        _args = Lists.toArray(builder._args, String.class);
        _output = builder._output;
    }


    /**
     * The utility entry point.
     *
     * @return The utility exit status. Zero means all went
     * well. Non-zero means something failed.
     */
    public int run() {

        InfoPrinter infoPrinter = buildInfoPrinter(_argv0, _output);
        CliException error = null;

        try {
            doEverything(infoPrinter, _args);
        } catch ( CliException e ) {
            error = e;
        }

        int exitStatus = EXIT_STATUS_OK;

        if ( error != null ) {
            infoPrinter.printError(error);
            exitStatus = EXIT_STATUS_FAILURE;
        }

        return exitStatus;
    }


    /**
     *
     */
    private InfoPrinter buildInfoPrinter(
            final String argv0,
            final OutputStream output) {

        boolean autoFlush = true;
        Writer stdoutWriter =
                new OutputStreamWriter(output, Charset.defaultCharset());
        PrintWriter stdout =
                new PrintWriter(stdoutWriter, autoFlush);

        InfoPrinter infoPrinter =
            InfoPrinter.builder()
            .setArgv0(argv0)
            .setOutput(stdout)
            .build();

        return infoPrinter;
    }


    /**
     *
     */
    private void doEverything(
            final InfoPrinter infoPrinter,
            final String[] args)
            throws CliException {

        if ( args.length == 0 ) {
            infoPrinter.printHelp();
        } else {
            CliOptions cliOptions = BakerCliOptions.parse(args);

            if ( cliOptions.hasOption(BakerCliOptions.HELP) ) {
                infoPrinter.printHelp();
            } else if ( cliOptions.hasOption(BakerCliOptions.VERSION) ) {
                infoPrinter.printVersion();
            } else {
                doBake(cliOptions);
            }
        }
    }


    /**
     *
     */
    private void doBake(final CliOptions cliOptions)
        throws CliException {

        setupLogging(cliOptions);

        SiteBakerConf conf = buildSiteBakerConf(cliOptions);
        SiteBakerFactory factory = SiteBakerFactory.get();
        SiteBaker siteBaker = factory.newSiteBaker();

        try {
            siteBaker.bake(conf);
        } catch ( YawgException e ) {
            CliException.raise(e, "Baking failed - {0}", e.getMessage());
        }
    }


    /**
     *
     */
    private void setupLogging(final CliOptions cliOptions) {

        Level loggerLevel = null;
        if ( cliOptions.hasOption(BakerCliOptions.VERBOSE) ) {
            loggerLevel = Level.FINEST;
        } else {
            loggerLevel = Level.INFO;
        }

        Formatter formatter = new PlainFormatter(LOG_FMT_CONSOLE);

        Handler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        handler.setLevel(loggerLevel);

        String loggerName = "com.varmateo";
        Logger logger = Logger.getLogger(loggerName);
        logger.addHandler(handler);
        logger.setLevel(Level.FINEST);
        logger.setUseParentHandlers(false);
    }


    /**
     *
     */
    private SiteBakerConf buildSiteBakerConf(final CliOptions cliOptions)
            throws CliException {

        Path sourceDir = cliOptions.getPath(BakerCliOptions.SOURCE_DIR);
        Path targetDir = cliOptions.getPath(BakerCliOptions.TARGET_DIR);
        Path templatesDir =
                cliOptions.getPath(BakerCliOptions.TEMPLATES_DIR, null);
        Path assetsDir =
                cliOptions.getPath(BakerCliOptions.ASSETS_DIR, null);
        PageVars externalPageVars = buildExternalPageVars(cliOptions);

        SiteBakerConf conf =
                SiteBakerConf.builder()
                .setSourceDir(sourceDir)
                .setTargetDir(targetDir)
                .setTemplatesDir(templatesDir)
                .setAssetsDir(assetsDir)
                .setExternalPageVars(externalPageVars)
                .build();

        return conf;
    }


    /**
     *
     */
    private PageVars buildExternalPageVars(final CliOptions cliOptions) {

        PageVars.Builder builder = PageVars.builder();

        for ( String optionValue : cliOptions.getAll(BakerCliOptions.PAGE_VAR)){
            String varName = null;
            String varValue = null;
            int indexOfEqSign = optionValue.indexOf('=');

            if ( indexOfEqSign < 0 ) {
                varName = optionValue;
                varValue = "";
            } else {
                varName = optionValue.substring(0, indexOfEqSign);
                varValue = optionValue.substring(indexOfEqSign+1);
            }

            builder.addVar(varName, varValue);
        }

        PageVars result = builder.build();

        return result;
    }


    /**
     *
     */
    public static final class Builder {


        private String _argv0;
        private List<String> _args;
        private OutputStream _output;


        /**
         *
         */
        private Builder() {

            _argv0 = DEFAULT_ARGV0;
            _args = new ArrayList<>();
            _output = System.out;
        }


        /**
         * @param argv0 The name the utility was launched with on the
         * command line. It will be used in informative or error
         * messages.
         */
        public Builder setArgv0(final String argv0) {

            _argv0 = argv0;

            return this;
        }


        /**
         * @param args The command line arguments passed to the
         * utility.
         */
        public Builder addArgs(final String... args) {

            for ( String arg : args ) {
                _args.add(arg);
            }

            return this;
        }


        /**
         *
         */
        public Builder setOutput(final OutputStream output) {

            _output = output;

            return this;
        }


        /**
         *
         */
        public BakerCli build() {

            BakerCli result = new BakerCli(this);

            return result;
        }


    }


}
