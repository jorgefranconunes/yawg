/**************************************************************************
 *
 * Copyright (c) 2016 JOrge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

import com.varmateo.commons.util.Exceptions;


/**
 * Provides static information about the Yawg software.
 *
 */
public final class YawgInfo
    extends Object {





    private static final String RESOURCE =
        YawgInfo.class.getSimpleName() + ".properties";

    private static final String PROP_VERSION = "version";


    public static final String VERSION;

    public static final String PRODUCT_NAME = "Yawg";

    public static final String COPYRIGHT_HEADER =
        "Copyright (c) 2016 Jorge Nunes";


/**
 * Static initialization
 */
    static {
        String resourcePath = RESOURCE;
        Properties props = new Properties();

        try ( InputStream input =
              YawgInfo.class.getResourceAsStream(resourcePath) ) {

            if ( input != null ) {
                props.load(input);
            } else {
                Exceptions.raise(IllegalStateException.class,
                                 "Missing resource \"{0}\"",
                                 resourcePath);
            }
        } catch ( IOException e ) {
            Exceptions.raise(IllegalStateException.class,
                             e,
                             "Failed to read resource \"{0}\"",
                             resourcePath);
        }

        VERSION = props.getProperty(PROP_VERSION);
    }





/**************************************************************************
 *
 * No instances of this class are to be created.
 *
 **************************************************************************/

    private YawgInfo() {

        // Nothing to do.
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

