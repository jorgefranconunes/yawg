/**************************************************************************
 *
 * Copyright (c) 2015-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import com.varmateo.yawg.cli.BakerCli;
import com.varmateo.yawg.cli.BakerCliConf;


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
     public static void main(final String[] args) {

         String argv0 = System.getProperty(PROP_ARGV, DEFAULT_ARGV0);
         BakerCliConf conf =
                 BakerCliConf.builder()
                 .setArgv0(argv0)
                 .addArgs(args)
                 .setOutput(System.out)
                 .build();
         BakerCli bakerCli = new BakerCli(conf);
         int exitStatus = bakerCli.run();

         System.exit(exitStatus);
    }


}
