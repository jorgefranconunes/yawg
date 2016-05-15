/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

import com.varmateo.yawg.util.Exceptions;


/**
 * Provides static information about the Yawg software.
 *
 */
public final class YawgInfo
    extends Object {





    private static final String RESOURCE =
        YawgInfo.class.getSimpleName() + ".properties";

    private static final String PROP_VERSION = "version";


    /** Version of the Yawg software. */
    public static final String VERSION;

    /** The Yawg product name as it should be displayed to end users. */
    public static final String PRODUCT_NAME = "Yawg";

    /** Copyright string to be displayed to end users. */
    public static final String COPYRIGHT_HEADER =
        "Copyright (c) 2016 Jorge Nunes, All Rights Reserved.";


    /**
     * Initialization of some static constants.
     */
    static {
        String resourcePath = RESOURCE;
        Properties props = new Properties();

        try ( InputStream input =
              YawgInfo.class.getResourceAsStream(resourcePath) ) {

            if ( input != null ) {
                props.load(input);
            } else {
                Exceptions.raise(
                        IllegalStateException.class,
                        "Missing resource \"{0}\"",
                        resourcePath);
            }
        } catch ( IOException e ) {
            Exceptions.raise(
                    IllegalStateException.class,
                    e,
                    "Failed to read resource \"{0}\"",
                    resourcePath);
        }

        VERSION = props.getProperty(PROP_VERSION);
    }


    /**
     * No instances of this class are to be created.
     */
    private YawgInfo() {

        // Nothing to do.
    }


}
