/**************************************************************************
 *
 * Copyright (c) 2015 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import com.varmateo.yawg.cli.BakerCli;





/**************************************************************************
 *
 * Program for baking a site from a directory tree.
 *
 **************************************************************************/

public final class Main
    extends Object {





    private static final String DEFAULT_ARGV0 = "yawg";

    // Name of system property whose value is to be used as argv0.
    private static final String PROP_ARGV = Main.class.getName() + ".argv0";





 /**************************************************************************
  *
  * 
  *
  **************************************************************************/

     public static void main(final String[] args) {

         BakerCli bakerCli = new BakerCli();

         String argv0      = System.getProperty(PROP_ARGV, DEFAULT_ARGV0);
         int    exitStatus = bakerCli.main(argv0, args);

         System.exit(exitStatus);
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

