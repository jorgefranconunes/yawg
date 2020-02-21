/**************************************************************************
 *
 * Copyright (c) 2015-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import io.vavr.control.Try;

import com.varmateo.yawg.api.BakeOptions;
import com.varmateo.yawg.api.BakeOptionsBuilder;
import com.varmateo.yawg.api.Result;
import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.core.DefaultSiteBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.util.Results;


/**
 * Command line interface for the baker utility.
 */
public final class BakerCli {


    private static final int EXIT_STATUS_OK = 0;
    private static final int EXIT_STATUS_FAILURE = 1;


    private final Log _log = LogFactory.createFor(BakerCli.class);


    private BakerCli() {
        // Nothing to do.
    }


    /**
     *
     */
    public static BakerCli create() {

        return new BakerCli();
    }


    /**
     * The utility entry point.
     *
     * @return The utility exit status. Zero means all went
     * well. Non-zero means something failed.
     */
    public int run(final BakerCliRunOptions options) {

        final InfoPrinter infoPrinter = buildInfoPrinter(options.argv0, options.output);
        final Try<Void> bakeResult = tryRun(infoPrinter, options.args)
                .onFailure(cause -> infoPrinter.printError(cause));

        final int exitStatus = bakeResult
                .map(x -> EXIT_STATUS_OK)
                .recover(x -> EXIT_STATUS_FAILURE)
                .get();

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
    private Try<Void> tryRun(
            final InfoPrinter infoPrinter,
            final String[] args) {

        return BakerCliAction.parse(args)
                .flatMap(action -> performAction(infoPrinter, action));
    }


    private Try<Void> performAction(
            final InfoPrinter infoPrinter,
            final BakerCliAction action) {

        return action.match(
                () -> Try.run(infoPrinter::printHelp),
                () -> Try.run(infoPrinter::printVersion),
                bake -> doBake(bake.options()));
    }


    private Try<Void> doBake(final BakerCliBakeOptions options) {

        logOptions(options);

        final SiteBaker siteBaker = options.templatesDir()
                .map(path -> DefaultSiteBaker.create(path))
                .getOrElse(() -> DefaultSiteBaker.create());

        final BakeOptions bakeOptions = BakeOptionsBuilder.create()
                .sourceDir(options.sourceDir())
                .targetDir(options.targetDir())
                .putAllExternalPageVars(options.externalPageVars().toJavaMap())
                .build();

        final Result<Void> result = siteBaker.bake(bakeOptions);

        return Results.toTry(result)
                .recoverWith(cause -> Try.failure(CliException.bakeFailure(cause)));
    }


    private void logOptions(final BakerCliBakeOptions options) {

        final Path sourceDir = options.sourceDir();
        final Path targetDir = options.targetDir();
        final String templatesDir = options.templatesDir().map(Path::toString).getOrElse("NONE");

        _log.info("{0} {1}", YawgInfo.PRODUCT_NAME, YawgInfo.VERSION);
        _log.info("    Source    : {0}", sourceDir);
        _log.info("    Target    : {0}", targetDir);
        _log.info("    Templates : {0}", templatesDir);
    }

}
