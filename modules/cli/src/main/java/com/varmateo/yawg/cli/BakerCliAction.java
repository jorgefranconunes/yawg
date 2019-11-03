/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.vavr.control.Try;


/**
 * ADT representing one of the possible actions to be performed by the
 * CLI app.
 */
/* default */ abstract class BakerCliAction {


    /**
     *
     */
    public static Try<BakerCliAction> parse(final String[] args) {

        if ( args.length == 0 ) {
            return Try.success(printHelp());
        } else {
            return BakerCliParameters.parse(args)
                    .flatMap(BakerCliAction::parseParameters);
        }
    }


    private static Try<BakerCliAction> parseParameters(final CliParameterSet cliParams) {

        if ( cliParams.hasOption(BakerCliParameters.HELP) ) {
            return Try.success(printHelp());
        } else if ( cliParams.hasOption(BakerCliParameters.VERSION) ) {
            return Try.success(printVersion());
        } else {
            return BakerCliBakeOptionsParser.parse(cliParams)
                    .map(BakerCliAction::bake);
        }
    }


    /**
     *
     */
    public abstract <T> T match(
            Supplier<T> a,
            Supplier<T> b,
            Function<Bake, T> c);


    /**
     *
     */
    public abstract void onMatch(
            Runnable a,
            Runnable b,
            Consumer<Bake> c);


    /**
     *
     */
    public static BakerCliAction printHelp() {

        return new PrintHelp();
    }


    /**
     *
     */
    public static BakerCliAction printVersion() {

        return new PrintVersion();
    }


    /**
     *
     */
    public static BakerCliAction bake(final BakerCliBakeOptions options) {

        return new Bake(options);
    }


    /**
     *
     */
    public static final class PrintHelp
            extends BakerCliAction {


        private PrintHelp() {
            // Nothing to do.
        }


        /**
         *
         */
        @Override
        public <T> T match(
                final Supplier<T> a,
                final Supplier<T> b,
                final Function<Bake, T> c) {

            return a.get();
        }


        /**
         *
         */
        @Override
        public void onMatch(
                final Runnable a,
                final Runnable b,
                final Consumer<Bake> c) {

            a.run();
        }
    }


    /**
     *
     */
    public static final class PrintVersion
            extends BakerCliAction {


        private PrintVersion() {
            // Nothing to do.
        }


        /**
         *
         */
        @Override
        public <T> T match(
                final Supplier<T> a,
                final Supplier<T> b,
                final Function<Bake, T> c) {

            return b.get();
        }


        /**
         *
         */
        @Override
        public void onMatch(
                final Runnable a,
                final Runnable b,
                final Consumer<Bake> c) {

            b.run();
        }
    }


    /**
     *
     */
    public static final class Bake
            extends BakerCliAction {


        private final BakerCliBakeOptions _options;


        private Bake(final BakerCliBakeOptions options) {

            _options = options;
        }


        /**
         *
         */
        public BakerCliBakeOptions options() {

            return _options;
        }


        /**
         *
         */
        @Override
        public <T> T match(
                final Supplier<T> a,
                final Supplier<T> b,
                final Function<Bake, T> c) {

            return c.apply(this);
        }


        /**
         *
         */
        @Override
        public void onMatch(
                final Runnable a,
                final Runnable b,
                final Consumer<Bake> c) {

            c.accept(this);
        }
    }

}
