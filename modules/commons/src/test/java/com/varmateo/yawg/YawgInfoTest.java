/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.yawg.YawgInfo;

import com.varmateo.commons.util.Exceptions;





/**
 *
 */
public final class YawgInfoTest
    extends Object {


    /**
     *
     */
    @Test
    public void checkVersion() {

        String expectedVersion = getYawgVersion();
        String actualVersion   = YawgInfo.version();

        assertEquals(expectedVersion, actualVersion);
    }


    /**
     *
     */
    private String getYawgVersion() {

        String key = YawgInfoTest.class.getSimpleName() + ".version";
        String version = System.getProperty(key);

        if ( version == null ) {
            String msgFmt = "System property \"{0}\" is not defined";
            Exceptions.raise(IllegalStateException.class, msgFmt, key);
        }

        return version;
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

