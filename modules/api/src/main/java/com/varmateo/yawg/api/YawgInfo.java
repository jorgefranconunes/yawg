/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Provides static information about the Yawg software.
 *
 */
public final class YawgInfo {


    private static final String RESOURCE =
        YawgInfo.class.getSimpleName() + ".properties";

    private static final String PROP_VERSION = "version";


    /** Version of the Yawg software. */
    public static final String VERSION;

    /** The Yawg product name as it should be displayed to end users. */
    public static final String PRODUCT_NAME = "Yawg";

    /** Copyright string to be displayed to end users. */
    public static final String COPYRIGHT_HEADER =
        "Copyright (c) 2019 Yawg project contributors.";


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
                String msg = "Missing resource \"" + resourcePath + "\"";
                throw new IllegalStateException(msg);
            }
        } catch ( IOException e ) {
            String msg = "Failed to read resource \"" + resourcePath + "\"";
            throw new IllegalStateException(msg, e);
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
