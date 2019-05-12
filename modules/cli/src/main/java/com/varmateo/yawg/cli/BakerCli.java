/**************************************************************************
 *
 * Copyright (c) 2015-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;

import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.api.SiteBakerOptions;
import com.varmateo.yawg.api.SiteBakerFactory;


/**
 * Command line interface for the baker utility.
 */
public final class BakerCli {


    private static final int EXIT_STATUS_OK = 0;
    private static final int EXIT_STATUS_FAILURE = 1;


    private final BakerCliOptions _options;


    /**
     * @param options Configuration settings.
     */
    public BakerCli(final BakerCliOptions options) {

        _options = options;
    }


    /**
     * The utility entry point.
     *
     * @return The utility exit status. Zero means all went
     * well. Non-zero means something failed.
     */
    public int run() {

        final InfoPrinter infoPrinter = buildInfoPrinter(_options.argv0, _options.output);
        CliException error = null;

        try {
            doEverything(infoPrinter, _options.args);
        } catch ( CliException e ) {
            error = e;
        }

        final int exitStatus;

        if ( error != null ) {
            infoPrinter.printError(error);
            exitStatus = EXIT_STATUS_FAILURE;
        } else {
            exitStatus = EXIT_STATUS_OK;
        }

        return exitStatus;
    }


    /**
     *
     */
    private static InfoPrinter buildInfoPrinter(
            final String argv0,
            final OutputStream output) {

        final boolean autoFlush = true;
        final Writer stdoutWriter = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        final PrintWriter stdout = new PrintWriter(stdoutWriter, autoFlush);

        return InfoPrinter.builder()
                .argv0(argv0)
                .output(stdout)
                .build();
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
            final CliParameterSet cliOptions = BakerCliParameters.parse(args);

            if ( cliOptions.hasOption(BakerCliParameters.HELP) ) {
                infoPrinter.printHelp();
            } else if ( cliOptions.hasOption(BakerCliParameters.VERSION) ) {
                infoPrinter.printVersion();
            } else {
                Try<Void> result = doBake(cliOptions);

                if ( result.isFailure() ) {
                    Throwable cause = result.getCause();
                    throw CliException.bakeFailure(cause);
                }
            }
        }
    }


    /**
     *
     */
    private static Try<Void> doBake(final CliParameterSet cliOptions)
            throws CliException {

        final SiteBakerOptions options = buildSiteBakerOptions(cliOptions);
        final SiteBakerFactory factory = SiteBakerFactory.get();
        final SiteBaker siteBaker = factory.newSiteBaker();

        return Try.run(() -> siteBaker.bake(options));
    }


    /**
     *
     */
    private static SiteBakerOptions buildSiteBakerOptions(final CliParameterSet cliOptions)
            throws CliException {

        final Path sourceDir = cliOptions.getPath(BakerCliParameters.SOURCE_DIR);
        final Path targetDir = cliOptions.getPath(BakerCliParameters.TARGET_DIR);
        final Path templatesDir = cliOptions.getPath(BakerCliParameters.TEMPLATES_DIR, null);
        final Path assetsDir = cliOptions.getPath(BakerCliParameters.ASSETS_DIR, null);
        final java.util.Map<String,Object> externalPageVars = buildExternalPageVars(cliOptions);

        return SiteBakerOptions.builder()
                .sourceDir(sourceDir)
                .targetDir(targetDir)
                .templatesDir(Optional.ofNullable(templatesDir))
                .assetsDir(Optional.ofNullable(assetsDir))
                .putAllExternalPageVars(externalPageVars)
                .build();
    }


    /**
     *
     */
    private static java.util.Map<String,Object> buildExternalPageVars(
            final CliParameterSet cliOptions) {

        return cliOptions
                .getAll(BakerCliParameters.PAGE_VAR)
                .map(BakerCli::getVarNameAndValueFromOptionValue)
                .foldLeft(
                        new java.util.HashMap<>(),
                        (map, t) -> { map.put(t._1, t._2); return map; });
    }


    private static Tuple2<String,String> getVarNameAndValueFromOptionValue(
            final String optionValue) {

        final String varName;
        final String varValue;
        final int indexOfEqSign = optionValue.indexOf('=');

        if ( indexOfEqSign < 0 ) {
            varName = optionValue;
            varValue = "";
        } else {
            varName = optionValue.substring(0, indexOfEqSign);
            varValue = optionValue.substring(indexOfEqSign+1);
        }

        return Tuple.of(varName, varValue);
    }


}
