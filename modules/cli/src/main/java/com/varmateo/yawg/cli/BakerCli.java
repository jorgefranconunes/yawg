/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.PrintWriter;
import java.nio.file.Path;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.BakerConf;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.cli.InfoPrinter;
import com.varmateo.yawg.cli.BakerCliOptions;
import com.varmateo.yawg.cli.util.CliException;
import com.varmateo.yawg.cli.util.CliOptions;


/**
 *
 */
public final class BakerCli
    extends Object {


    private static final int EXIT_STATUS_OK      = 0;
    private static final int EXIT_STATUS_FAILURE = 1;

    private String      _argv0       = null;
    private InfoPrinter _infoPrinter = null;


    /**
     *
     */
    public BakerCli() {

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
            CliOptions cliOptions = BakerCliOptions.parse(args);

            if ( cliOptions.hasOption(BakerCliOptions.HELP) ) {
                _infoPrinter.printHelp();
            } else if ( cliOptions.hasOption(BakerCliOptions.VERSION) ) {
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

        Path sourceDir = cliOptions.getPath(BakerCliOptions.SOURCE_DIR);
        Path targetDir = cliOptions.getPath(BakerCliOptions.TARGET_DIR);
        Path templatesDir =
                cliOptions.getPath(BakerCliOptions.TEMPLATES_DIR, null);

        BakerConf conf =
                new BakerConf.Builder()
                .setSourceDir(sourceDir)
                .setTargetDir(targetDir)
                .setTemplatesDir(templatesDir)
                .build();
        Baker baker = new Baker(conf);

        try {
            baker.bake();
        } catch ( YawgException e ) {
            CliException.raise(e, "Baking failed - {0}", e.getMessage());
        }
    }


}
