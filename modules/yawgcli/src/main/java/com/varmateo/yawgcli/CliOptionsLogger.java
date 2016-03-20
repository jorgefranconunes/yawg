/**************************************************************************
 *
 * Copyright (c) 2015 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawgcli;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

import com.varmateo.commons.cli.CliOption;
import com.varmateo.commons.cli.CliOptions;
import com.varmateo.commons.logging.Log;





/**************************************************************************
 *
 * Provides utility methods for logging info related with command line
 * options.
 *
 **************************************************************************/

final class CliOptionsLogger
    extends Object {





/**************************************************************************
 *
 * No instances of this class are to be created.
 *
 **************************************************************************/

    private CliOptionsLogger() {
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    public static void log(final Log        log,
                           final CliOptions cliOptions) {

        log.info("CLI options:");
        
        cliOptions.supportedOptions().stream()
            .filter( option -> cliOptions.hasOption(option) )
            .sorted(Comparator.comparing( CliOption::getLiteral ))
            .forEach( option -> logOption(log, cliOptions, option) );
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    private static void logOption(final Log        log,
                                  final CliOptions cliOptions,
                                  final CliOption  option) {

        String name  = option.getLiteral();
        String value = cliOptions.get(option, null);

        if ( value != null ) {
            log.info("\t{0} {1}", name, value);
        } else {
            log.info("\t{0}", name);
        }

        
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

