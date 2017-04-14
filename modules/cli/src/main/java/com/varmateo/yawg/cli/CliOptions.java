/**************************************************************************
 *
 * Copyright (c) 2015-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javaslang.collection.Array;
import javaslang.collection.Seq;
import javaslang.collection.Set;
import javaslang.control.Option;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import com.varmateo.yawg.cli.CliException;
import com.varmateo.yawg.cli.CliOption;


/**
 * Represents the set of options provided in a command line.
 */
final class CliOptions {


    private static final String FLAG_TRUE  = "true";

    private Set<CliOption> _allOptions = null;
    private CommandLine _cmdLine    = null;


    /**
     * Only used internally.
     */
    private CliOptions(
            final Set<CliOption> allOptions,
            final CommandLine cmdLine) {

        _allOptions = allOptions;
        _cmdLine = cmdLine;
    }


    /**
     *
     */
    public static CliOptions parse(
            final Set<CliOption> options,
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
            throw CliException.raise("unknown option {0}", argList.get(0));
        }

        return new CliOptions(options, cmdLine);
    }


    /**
     *
     */
    private static Options buildApacheOptions(final Set<CliOption> options) {

        return options
                .map(CliOption::apacheOption)
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

        final String msg;
        final Object[] fmtArgs;

        if ( e instanceof MissingOptionException ) {
            MissingOptionException ex = (MissingOptionException)e;
            String shortOpt = (String)ex.getMissingOptions().get(0);
            org.apache.commons.cli.Option option = options.getOption(shortOpt);
            String optionName = getApacheOptionName(option);
            msg     = "missing mandatory option {0}";
            fmtArgs = new Object[]{ optionName };
        } else if ( e instanceof UnrecognizedOptionException ) {
            UnrecognizedOptionException ex = (UnrecognizedOptionException)e;
            String optionName = ex.getOption();
            msg     = "unknown option {0}";
            fmtArgs = new Object[]{ optionName };
        } else if ( e instanceof MissingArgumentException ) {
            MissingArgumentException ex = (MissingArgumentException)e;
            String optionName = getApacheOptionName(ex.getOption());
            msg     = "argument for option {0} is missing";
            fmtArgs = new Object[]{ optionName };
        } else {
            msg     = "failed to parse options - {0} - {1}";
            fmtArgs = new Object[]{ e.getClass().getName(), e.getMessage() };
        }

        throw CliException.raise(msg, fmtArgs);
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
    public Set<CliOption> supportedOptions() {

        return _allOptions;
    }


    /**
     *
     */
    public boolean hasOption(final CliOption option) {

        String  optionName = option.getName();
        boolean result     = _cmdLine.hasOption(optionName);

        return result;
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
    public Seq<String> getAll(final CliOption option) {

        String   optionName   = option.getName();
        String[] optionValues = _cmdLine.getOptionValues(optionName);
        Seq<String> result =
                Option.of(optionValues)
                .map(Array::of)
                .getOrElse(Array::of);

        return result;
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
    public String get(final CliOption option)
            throws CliException {

        String   result       = null;
        String   optionName   = option.getName();
        String[] optionValues = _cmdLine.getOptionValues(optionName);

        if ( optionValues != null ) {
            int indexOfLast = optionValues.length-1;
            result = optionValues[indexOfLast];
        } else {
            String   msgFmt  = "missing mandatory option --{0}";
            Object[] fmtArgs = { optionName };
            CliException.raise(msgFmt, fmtArgs);
        }

        return result;
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
            final CliOption option,
            final String defaultValue) {

        String   result       = null;
        String   optionName   = option.getName();
        String[] optionValues = _cmdLine.getOptionValues(optionName);

        if ( optionValues != null ) {
            int indexOfLast = optionValues.length-1;
            result = optionValues[indexOfLast];
        } else {
            result = defaultValue;
        }

        return result;
    }


    /**
     * Retrieves the value of a mandatory option as a path.
     */
    public Path getPath(final CliOption option)
            throws CliException {

        String pathStr = get(option);
        Path   result  = stringToPath(option, pathStr);

        return result;
    }


    /**
     *
     */
    public Path getPath(
            final CliOption option,
            final Path defaultValue)
            throws CliException {

        String pathStr = get(option, null);
        Path result = null;

        if ( pathStr == null ) {
            result = defaultValue;
        } else {
            result = stringToPath(option, pathStr);
        }

        return result;
    }


    /**
     *
     */
    private Path stringToPath(
            final CliOption option,
            final String pathStr)
            throws CliException {

        Path result = null;

        try {
            result = Paths.get(pathStr);
        } catch ( InvalidPathException e ) {
            String   msgFmt  = "value of option {0} is an invalid path ({1})";
            Object[] fmtArgs = { option.getLiteral(), pathStr };
            CliException.raise(msgFmt, fmtArgs);
        }

        return result;
    }


    /**
     * Checks the given option represents a "true" valued flag.
     */
    public boolean isTrue(final CliOption option) {

        boolean result = false;

        if ( hasOption(option) ) {
            String value = get(option, FLAG_TRUE);

            result = FLAG_TRUE.equals(value);
        }

        return result;
    }


}
