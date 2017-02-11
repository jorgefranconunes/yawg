/**************************************************************************
 *
 * Copyright (c) 2015-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Seq;
import javaslang.control.Try;

import com.varmateo.yawg.api.PageVars;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.SiteBaker;
import com.varmateo.yawg.spi.SiteBakerConf;
import com.varmateo.yawg.spi.SiteBakerFactory;

import com.varmateo.yawg.cli.InfoPrinter;
import com.varmateo.yawg.cli.BakerCliOptions;
import com.varmateo.yawg.cli.CliException;
import com.varmateo.yawg.cli.CliOptions;


/**
 * Command line interface for the baker utility.
 */
public final class BakerCli {


    private static final int EXIT_STATUS_OK = 0;
    private static final int EXIT_STATUS_FAILURE = 1;

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
        _args = builder._args.toJavaArray(String.class);
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
    private InfoPrinter buildInfoPrinter(
            final String argv0,
            final OutputStream output) {

        boolean autoFlush = true;
        Writer stdoutWriter =
                new OutputStreamWriter(output, StandardCharsets.UTF_8);
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
                Try<Void> result = doBake(cliOptions);

                if ( result.isFailure() ) {
                    Throwable e = result.getCause();
                    CliException.raise(e, "Baking failed - {0}", e.getMessage());
                }
            }
        }
    }


    /**
     *
     */
    private Try<Void> doBake(final CliOptions cliOptions)
            throws CliException {

        LogInitializer logInitializer = initLogInitializer(cliOptions);
        SiteBakerConf conf = buildSiteBakerConf(cliOptions);
        SiteBakerFactory factory = SiteBakerFactory.get();
        SiteBaker siteBaker = factory.newSiteBaker();
        Try<Void> result = Try.run(() -> siteBaker.bake(conf));

        logInitializer.close();

        return result;
    }


    /**
     *
     */
    private LogInitializer initLogInitializer(final CliOptions cliOptions) {

        LogInitializer logInitializer = JulLogInitializer.builder()
                .setVerbose(cliOptions.hasOption(BakerCliOptions.VERBOSE))
                .setOutput(_output)
                .build();

        logInitializer.init();

        return logInitializer;
    }


    /**
     *
     */
    private SiteBakerConf buildSiteBakerConf(final CliOptions cliOptions)
            throws CliException {

        Path sourceDir =
                cliOptions.getPath(BakerCliOptions.SOURCE_DIR);
        Path targetDir =
                cliOptions.getPath(BakerCliOptions.TARGET_DIR);
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

        return cliOptions
                .getAll(BakerCliOptions.PAGE_VAR)
                .map(BakerCli::getVarNameAndValueFromOptionValue)
                .foldLeft(
                        PageVars.builder(),
                        (builder, t) -> builder.addVar(t._1(), t._2()))
                .build();
    }


    private static Tuple2<String,String> getVarNameAndValueFromOptionValue(
            final String optionValue) {

        final String varName;
        final String varValue;
        int indexOfEqSign = optionValue.indexOf('=');

        if ( indexOfEqSign < 0 ) {
            varName = optionValue;
            varValue = "";
        } else {
            varName = optionValue.substring(0, indexOfEqSign);
            varValue = optionValue.substring(indexOfEqSign+1);
        }

        return Tuple.of(varName, varValue);
    }


    /**
     *
     */
    public static final class Builder {


        private String _argv0;
        private Seq<String> _args;
        private OutputStream _output;


        /**
         *
         */
        private Builder() {

            _argv0 = DEFAULT_ARGV0;
            _args = List.of();
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

            _args = _args.appendAll(List.of(args));

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
