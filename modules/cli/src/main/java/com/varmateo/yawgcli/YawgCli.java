/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawgcli;

import java.io.PrintWriter;
import java.nio.file.Path;

import com.varmateo.commons.cli.CliException;
import com.varmateo.commons.cli.CliOptions;

import com.varmateo.yawg.YawgBaker;
import com.varmateo.yawg.YawgBakerConf;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawgcli.InfoPrinter;
import com.varmateo.yawgcli.YawgCliOptions;


/**
 *
 */
public final class YawgCli
    extends Object {


    private static final int EXIT_STATUS_OK      = 0;
    private static final int EXIT_STATUS_FAILURE = 1;

    private String      _argv0       = null;
    private InfoPrinter _infoPrinter = null;


    /**
     *
     */
    public YawgCli() {

        // Nothing to do.
    }


    /**
     *
     */
    public int main(final String   argv0,
                    final String[] args) {

        _argv0       = argv0;
        _infoPrinter = buildInfoPrinter(argv0);

        CliException error = null;

        try {
            doEverything(args);
        } catch ( CliException e ) {
            error = e;
        }

        int exitStatus = EXIT_STATUS_OK;

        if ( error != null ) {
            _infoPrinter.printError(error);
            exitStatus = EXIT_STATUS_FAILURE;
        }

        return exitStatus;
    }


    /**
     *
     */
    private InfoPrinter buildInfoPrinter(final String argv0) {

        boolean autoFlush = true;
        PrintWriter stdout = new PrintWriter(System.out, autoFlush);

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
    public void doEverything(final String[] args)
        throws CliException {

        if ( args.length == 0 ) {
            _infoPrinter.printHelp();
        } else {
            CliOptions cliOptions = YawgCliOptions.parse(args);

            if ( cliOptions.hasOption(YawgCliOptions.HELP) ) {
                _infoPrinter.printHelp();
            } else if ( cliOptions.hasOption(YawgCliOptions.VERSION) ) {
                _infoPrinter.printVersion();
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

        Path sourceDir = cliOptions.getPath(YawgCliOptions.SOURCE_DIR);
        Path targetDir = cliOptions.getPath(YawgCliOptions.TARGET_DIR);
        Path templatesDir =
                cliOptions.getPath(YawgCliOptions.TEMPLATES_DIR, null);

        YawgBakerConf conf =
                new YawgBakerConf.Builder()
                .setSourceDir(sourceDir)
                .setTargetDir(targetDir)
                .setTemplatesDir(templatesDir)
                .build();
        YawgBaker baker = new YawgBaker(conf);

        try {
            baker.bake();
        } catch ( YawgException e ) {
            CliException.raise(e, "Baking failed - {0}", e.getMessage());
        }
    }


}
