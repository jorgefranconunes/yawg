/**************************************************************************
 *
 * Copyright (c) 2015-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.InputStream;
import java.io.IOException;
import java.util.logging.LogManager;


/**
 * Program for baking a site from a directory tree.
 */
public final class Main {


    private static final String DEFAULT_ARGV0 = "yawg";

    // Name of system property whose value is to be used as argv0.
    private static final String PROP_ARGV = Main.class.getName() + ".argv0";


    /**
     * No instances of this class are to be created.
     */
    private Main() {
        // Nothing to do.
    }


    /**
     * Main program.
     *
     * @param args Command line arguments.
     */
     public static void main(final String[] args)
             throws IOException {

         setupLogging();

         final String argv0 = System.getProperty(PROP_ARGV, DEFAULT_ARGV0);
         final BakerCliRunOptions options = BakerCliRunOptions.builder()
                 .argv0(argv0)
                 .addArgs(args)
                 .output(System.out)
                 .build();
         final BakerCli bakerCli = BakerCli.create();
         final int exitStatus = bakerCli.run(options);

         System.exit(exitStatus);
    }


    private static void setupLogging()
            throws IOException {

        try ( final InputStream input = Main.class.getResourceAsStream("/logging.properties") ) {
            LogManager.getLogManager().readConfiguration(input);
        }
    }


}
