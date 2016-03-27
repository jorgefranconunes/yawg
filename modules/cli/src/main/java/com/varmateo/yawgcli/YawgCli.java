/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawgcli;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.varmateo.commons.cli.CliException;
import com.varmateo.commons.cli.CliOptions;
import com.varmateo.commons.logging.Log;
import com.varmateo.commons.logging.LogFactory;

import com.varmateo.yawg.YawgBakeConf;
import com.varmateo.yawg.YawgBaker;
import com.varmateo.yawg.YawgInfo;

import com.varmateo.yawgcli.CliOptionsLogger;
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

        Log log = LogFactory.createFor(YawgBaker.class);

        log.info("{0} {1}", YawgInfo.PRODUCT_NAME, YawgInfo.VERSION);

        YawgBaker baker = new YawgBaker(log);
        YawgBakeConf conf =
            new YawgBakeConf.Builder()
            .setSourceDir(cliOptions.getPath(YawgCliOptions.SOURCE_DIR))
            .setTargetDir(cliOptions.getPath(YawgCliOptions.TARGET_DIR))
            .build();

        baker.bake(conf);
    }


}


