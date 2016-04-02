/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.commons.cli;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import com.varmateo.commons.cli.CliException;
import com.varmateo.commons.cli.CliOption;


/**
 * Represents the set of options provided in a command line.
 */
public final class CliOptions
        extends Object {


    private static final String FLAG_TRUE  = "true";
    private static final String FLAG_FALSE = "false";

    private Collection<CliOption> _allOptions = null;
    private CommandLine           _cmdLine    = null;


    /**
     * Only used internally.
     */
    private CliOptions(
            final Collection<CliOption> allOptions,
            final CommandLine cmdLine) {

        Collection<CliOption> allOptionsCopy =
                new HashSet<CliOption>(allOptions);

        _allOptions = Collections.unmodifiableCollection(allOptionsCopy);
        _cmdLine    = cmdLine;
    }


    /**
     *
     */
    public static CliOptions parse(
            final Collection<CliOption> options,
            final String[] args)
            throws CliException {

        Options           apacheOptions = buildApacheOptions(options);
        CommandLineParser cliParser     = new GnuParser();
        CommandLine       cmdLine       = null;

        try {
            cmdLine = cliParser.parse(apacheOptions, args, false);
        } catch ( ParseException e ) {
            raiseCliException(apacheOptions, e);
        }

        if ( cmdLine.getArgList().size() > 0 ) {
            // There were arguments that were not options. We require
            // all arguments to be options, thus this is an invalid
            // command line.
            String   msg     = "unknown option {0}";
            Object[] fmtArgs = { cmdLine.getArgList().get(0) };
            CliException.raise(msg, fmtArgs);
        }

        CliOptions result = new CliOptions(options, cmdLine);

        return result;
    }


    /**
     *
     */
    private static Options buildApacheOptions(
            final Collection<CliOption> options) {

        Options apacheOptions = new Options();

        options.stream()
            .map(CliOption::apacheOption)
            .forEach(o -> apacheOptions.addOption(o));

        return apacheOptions;
    }


    /**
     *
     */
    private static void raiseCliException(
            final Options options,
            final ParseException e)
            throws CliException {

        String   msg     = null;
        Object[] fmtArgs = null;

        if ( e instanceof MissingOptionException ) {
            MissingOptionException ex = (MissingOptionException)e;
            String shortOpt = (String)ex.getMissingOptions().get(0);
            Option option   = options.getOption(shortOpt);
            String longOpt  = "--" + option.getLongOpt();
            msg     = "missing mandatory option {0}";
            fmtArgs = new Object[]{ longOpt };
        } else if ( e instanceof UnrecognizedOptionException ) {
            UnrecognizedOptionException ex = (UnrecognizedOptionException)e;
            msg     = "unknown option {0}";
            fmtArgs = new Object[]{ ex.getOption() };
        } else if ( e instanceof MissingArgumentException ) {
            MissingArgumentException ex = (MissingArgumentException)e;
            msg     = "argument for option {0} is missing";
            fmtArgs = new Object[]{ ex.getOption() };
        } else {
            msg     = "failed to parse options - {0} - {1}";
            fmtArgs = new Object[]{ e.getClass().getName(), e.getMessage() };
        }

        CliException.raise(msg, fmtArgs);
    }


    /**
     *
     */
    public Collection<CliOption> supportedOptions() {

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
     * Retrieves the value of a mandatory option.
     *
     * @param option The name (short or long) of the option whose
     * value is to be retrieved.
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





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

