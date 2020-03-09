/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;


/**
 * Provides static information about the Yawg software.
 *
 */
public final class YawgInfo {


    /** Version of the Yawg software. */
    public static final String VERSION = YawgInfo.class.getPackage().getImplementationVersion();

    /** The Yawg product name as it should be displayed to end users. */
    public static final String PRODUCT_NAME = "Yawg";

    /** Copyright string to be displayed to end users. */
    public static final String COPYRIGHT_HEADER = "Copyright (c) 2020 Yawg project contributors.";


    /**
     * No instances of this class are to be created.
     */
    private YawgInfo() {

        // Nothing to do.
    }


}
