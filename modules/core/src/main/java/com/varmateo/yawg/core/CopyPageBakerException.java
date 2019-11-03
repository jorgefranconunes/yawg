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
/* default */ final class CopyPageBakerException
        extends YawgException {


    private CopyPageBakerException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     *
     */
    public static CopyPageBakerException copyFailure(
            final Path sourcePath,
            final Path targetDir,
            final Throwable cause) {

        final String msg = String.format(
                "Failed copying \"%s\" to \"%s\" - %s - %s",
                sourcePath, targetDir, cause.getClass().getName(), cause.getMessage());

        return new CopyPageBakerException(msg, cause);
    }
}
