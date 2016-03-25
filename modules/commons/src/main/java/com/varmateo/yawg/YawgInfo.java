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

    private static Properties _props = new Properties();


/**
 * Static initialization
 */
    static {
        String resourcePath = RESOURCE;

        try ( InputStream input =
              YawgInfo.class.getResourceAsStream(resourcePath) ) {

            if ( input != null ) {
                _props.load(input);
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
    }





/**************************************************************************
 *
 * No instances of this class are to be created.
 *
 **************************************************************************/

    private YawgInfo() {

        // Nothing to do.
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    public static String version() {

        String result = _props.getProperty(PROP_VERSION);

        return result;
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

