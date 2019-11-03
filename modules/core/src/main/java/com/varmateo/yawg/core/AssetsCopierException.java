/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
/* default */ final class AssetsCopierException
        extends YawgException {


    private AssetsCopierException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     *
     */
    public static AssetsCopierException copyFailure(
            final Path sourceDir,
            final Path targetDir,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to copy assets from \"%s\" to \"%s\" - %s - %s",
                sourceDir, targetDir, cause.getClass().getName(), cause.getMessage());

        return new AssetsCopierException(msg, cause);
    }

}
