/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.varmateo.yawg.SiteBaker;
import com.varmateo.yawg.SiteBakerConf;
import com.varmateo.yawg.SiteBakerFactory;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.logging.PlainFormatter;

import com.varmateo.yawg.cli.InfoPrinter;
import com.varmateo.yawg.cli.BakerCliOptions;
import com.varmateo.yawg.cli.util.CliException;
import com.varmateo.yawg.cli.util.CliOptions;


/**
 * Command line interface for the baker.
 */
public final class BakerCli
    extends Object {


    private static final int EXIT_STATUS_OK = 0;
    private static final int EXIT_STATUS_FAILURE = 1;

    private static final String LOG_FMT_CONSOLE =
            "{0,date,yyyy-MM.dd HH:mm:ss.SSS} {1} {2}\n";


    /**
     *
     */
    public BakerCli() {

        // Nothing to do.
    }


    /**
     *
     */
    public int main(
            final String   argv0,
            final String[] args) {

        InfoPrinter infoPrinter = buildInfoPrinter(argv0);
        CliException error = null;

        try {
            doEverything(infoPrinter, args);
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
    private InfoPrinter buildInfoPrinter(final String argv0) {

        boolean autoFlush = true;
        Writer stdoutWriter =
                new OutputStreamWriter(System.out, Charset.defaultCharset());
        PrintWriter stdout =
                new PrintWriter(stdoutWriter, autoFlush);

        InfoPrinter infoPrinter =
            new InfoPrinter.Builder()
            .setArgv0(argv0)
            .setOutput(stdout)
            .build();

        return infoPrinter;
    }


    /**
     *
     */
    public void doEverything(
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

        Path sourceDir = cliOptions.getPath(BakerCliOptions.SOURCE_DIR);
        Path targetDir = cliOptions.getPath(BakerCliOptions.TARGET_DIR);
        Path templatesDir =
                cliOptions.getPath(BakerCliOptions.TEMPLATES_DIR, null);
        Path assetsDir =
                cliOptions.getPath(BakerCliOptions.ASSETS_DIR, null);

        SiteBakerConf conf =
                new SiteBakerConf.Builder()
                .setSourceDir(sourceDir)
                .setTargetDir(targetDir)
                .setTemplatesDir(templatesDir)
                .setAssetsDir(assetsDir)
                .build();
        SiteBakerFactory factory = new SiteBakerFactory();
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


}
