/**************************************************************************
 *
 * Copyright (c) 2019-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import io.vavr.control.Try;

/**
 * Sum type representing one of the possible actions to be performed
 * by the CLI app.
 */
/* default */ sealed interface BakerCliAction {

    record PrintHelp() implements BakerCliAction {}

    record PrintVersion() implements BakerCliAction {}

    record Bake(
            BakerCliBakeOptions options
    ) implements BakerCliAction {}

    static BakerCliAction printHelp() {
        return new PrintHelp();
    }

    static BakerCliAction printVersion() {
        return new PrintVersion();
    }

    static BakerCliAction bake(final BakerCliBakeOptions options) {
        return new Bake(options);
    }

    static Try<BakerCliAction> parse(final String[] args) {
        if (args.length == 0) {
            return Try.success(printHelp());
        } else {
            return BakerCliParameters.parse(args)
                    .flatMap(BakerCliAction::parseParameters);
        }
    }

    static Try<BakerCliAction> parseParameters(final CliParameterSet cliParams) {
        if (cliParams.hasOption(BakerCliParameters.HELP)) {
            return Try.success(printHelp());
        } else if (cliParams.hasOption(BakerCliParameters.VERSION)) {
            return Try.success(printVersion());
        } else {
            return BakerCliBakeOptionsParser.parse(cliParams)
                    .map(BakerCliAction::bake);
        }
    }
}
