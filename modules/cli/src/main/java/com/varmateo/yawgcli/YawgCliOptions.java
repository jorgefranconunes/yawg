/**************************************************************************
 *
 * Copyright (c) 2015 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawgcli;

import java.util.Arrays;
import java.util.Collection;

import com.varmateo.commons.cli.CliException;
import com.varmateo.commons.cli.CliOption;
import com.varmateo.commons.cli.CliOptions;





/**************************************************************************
 *
 * Represents the set of supported command line options.
 *
 **************************************************************************/

public final class YawgCliOptions
    extends Object {





    public static final CliOption HELP =
        new CliOption.Builder()
        .setLongName("help")
        .setDescription("show this help text and exit")
        .setShortName("h")
        .build();

    public static final CliOption VERBOSE =
        new CliOption.Builder()
        .setLongName("verbose")
        .setDescription("show abundant logging")
        .build();

    public static final CliOption VERSION =
        new CliOption.Builder()
        .setShortName("v")
        .setLongName("version")
        .setDescription("show version and exit")
        .build();





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    private static final Collection<CliOption> _options;

    static {
        CliOption[] allOptions = {
            HELP,
            VERBOSE,
            VERSION,
        };

        _options = Arrays.asList(allOptions);
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    public static Collection<CliOption> options() {

        return _options;
    }





/**************************************************************************
 *
 * No instances of this class are to be created.
 *
 **************************************************************************/

    private YawgCliOptions() {
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    public static CliOptions parse(final String[] args)
        throws CliException {

        CliOptions options = CliOptions.parse(_options, args);

        return options;
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

