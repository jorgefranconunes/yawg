/**************************************************************************
 *
 * Copyright (c) 2015-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.vavr.collection.Array;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;


/**
 * Represents the set of options supported in the command line.
 */
/* default */ final class CliParameterSet {


    private static final String FLAG_TRUE  = "true";

    private final Set<CliParameter> _allOptions;
    private final CommandLine _cmdLine;


    /**
     * Only used internally.
     */
    private CliParameterSet(
            final Set<CliParameter> allOptions,
            final CommandLine cmdLine) {

        _allOptions = allOptions;
        _cmdLine = cmdLine;
    }


    /**
     *
     */
    public static CliParameterSet parse(
            final Set<CliParameter> options,
            final String[] args)
            throws CliException {

        Options apacheOptions = buildApacheOptions(options);
        CommandLineParser cliParser = new GnuParser();
        final CommandLine cmdLine;

        try {
            cmdLine = cliParser.parse(apacheOptions, args, false);
        } catch ( ParseException e ) {
            throw raiseCliException(apacheOptions, e);
        }

        @SuppressWarnings("unchecked")
        java.util.List<String> argList = cmdLine.getArgList();
        if ( (argList!=null) && !argList.isEmpty() ) {
            // There were arguments that were not options. We require
            // all arguments to be options, thus this is an invalid
            // command line.
            throw CliException.unknownOption(argList.get(0));
        }

        return new CliParameterSet(options, cmdLine);
    }


    /**
     *
     */
    private static Options buildApacheOptions(final Set<CliParameter> options) {

        return options
                .map(CliParameter::apacheOption)
                .foldLeft(
                        new Options(),
                        (xs, x) -> xs.addOption(x));
    }


    /**
     *
     */
    private static CliException raiseCliException(
            final Options options,
            final ParseException e)
            throws CliException {

        final CliException cause;

        if ( e instanceof MissingOptionException ) {
            final MissingOptionException ex = (MissingOptionException)e;
            final String shortOpt = (String)ex.getMissingOptions().get(0);
            final org.apache.commons.cli.Option option = options.getOption(shortOpt);
            final String optionName = getApacheOptionName(option);
            cause = CliException.missingOption(optionName);
        } else if ( e instanceof UnrecognizedOptionException ) {
            final UnrecognizedOptionException ex = (UnrecognizedOptionException)e;
            final String optionName = ex.getOption();
            cause = CliException.unknownOption(optionName);
        } else if ( e instanceof MissingArgumentException ) {
            final MissingArgumentException ex = (MissingArgumentException)e;
            final String optionName = getApacheOptionName(ex.getOption());
            cause = CliException.missingOptionArg(optionName);
        } else {
            cause = CliException.optionParseFailure(e);
        }

        throw cause;
    }


    /**
     *
     */
    private static String getApacheOptionName(
            final org.apache.commons.cli.Option apacheOption) {

        final String result;

        String longName = apacheOption.getLongOpt();
        if ( longName != null ) {
            result = "--" + longName;
        } else {
            String shortName = apacheOption.getOpt();
            result = "-" + shortName;
        }

        return result;
    }


    /**
     *
     */
    public Set<CliParameter> supportedOptions() {

        return _allOptions;
    }


    /**
     *
     */
    public boolean hasOption(final CliParameter option) {

        final String optionName = option.name();

        return _cmdLine.hasOption(optionName);
    }


    /**
     * Retrieves all the values for an option.
     *
     * <p>An option will have multiple values when it is given more
     * than onde in the list of command line arguments.</p>
     *
     * @param option The name of the option whose values are to be
     * returned.
     *
     * @return A list with the values of the given option, in the same
     * order theay appeared in the command line. It will be empty if
     * the option was not given in the command line.
     */
    public Seq<String> getAll(final CliParameter option) {

        final String   optionName   = option.name();
        final String[] optionValues = _cmdLine.getOptionValues(optionName);

        return Option.of(optionValues)
                .map(Array::of)
                .getOrElse(Array::of);
    }


    /**
     * Retrieves the value of a mandatory option.
     *
     * <p>If the given option does not exist, then a
     * <code>CliException</code> will be thrown.
     *
     * @param option The name (short or long) of the option whose
     * value is to be retrieved.
     *
     * @return The value of the given option.
     *
     * @exception CliException Thrown if the given command line does
     * not contain the option.
     */
    public String get(final CliParameter option)
            throws CliException {

        final String optionValue;
        final String optionName = option.name();
        final String[] optionValues = _cmdLine.getOptionValues(optionName);

        if ( optionValues != null ) {
            final int indexOfLast = optionValues.length-1;
            optionValue = optionValues[indexOfLast];
        } else {
            throw CliException.missingOption(option.literal());
        }

        return optionValue;
    }


    /**
     * Retrieves the value of a conditional option.
     *
     * @param option The name (short or long) of the option whose
     * value is to be retrieved.
     *
     * @param defaultValue The value to be returned if the option is
     * not present.
     */
    public String get(
            final CliParameter option,
            final String defaultValue) {

        final String optionValue;
        final String optionName = option.name();
        final String[] optionValues = _cmdLine.getOptionValues(optionName);

        if ( optionValues != null ) {
            final int indexOfLast = optionValues.length-1;
            optionValue = optionValues[indexOfLast];
        } else {
            optionValue = defaultValue;
        }

        return optionValue;
    }


    /**
     * Retrieves the value of a mandatory option as a path.
     */
    public Path getPath(final CliParameter option)
            throws CliException {

        String pathStr = get(option);

        return stringToPath(option, pathStr);
    }


    /**
     *
     */
    public Path getPath(
            final CliParameter option,
            final Path defaultValue)
            throws CliException {

        String pathStr = get(option, null);

        return pathStr == null
                ? defaultValue
                : stringToPath(option, pathStr);
    }


    /**
     *
     */
    private Path stringToPath(
            final CliParameter option,
            final String pathStr)
            throws CliException {

        try {
            return Paths.get(pathStr);
        } catch ( InvalidPathException e ) {
            throw CliException.invalidPath(option.literal(), pathStr);
        }
    }


    /**
     * Checks the given option represents a "true" valued flag.
     */
    public boolean isTrue(final CliParameter option) {

        boolean result = false;

        if ( hasOption(option) ) {
            String value = get(option, FLAG_TRUE);

            result = FLAG_TRUE.equals(value);
        }

        return result;
    }


}
