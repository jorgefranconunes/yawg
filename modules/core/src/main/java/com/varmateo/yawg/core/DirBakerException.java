/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
/* default */ final class DirBakerException
        extends YawgException {


    private DirBakerException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     *
     */
    public static DirBakerException directoryCreationFailure(
            final Path dirPath,
            final IOException cause) {

        final String msg = String.format(
                "Failed to create directory \"%s\" - %s - %s",
                dirPath,
                cause.getClass().getName(),
                cause.getMessage());

        return new DirBakerException(msg, cause);
    }


    /**
     *
     */
    public static Function<IOException, DirBakerException> directoryCreationFailure(
            final Path dirPath) {

        return cause -> directoryCreationFailure(dirPath, cause);
    }


    /**
     *
     */
    public static DirBakerException directoryListFailure(
            final Path dirPath,
            final IOException cause) {

        final String msg = String.format(
                "Failed to list directory \"%s\" - %s - %s",
                dirPath,
                cause.getClass().getName(),
                cause.getMessage());

        return new DirBakerException(msg, cause);
    }


    /**
     *
     */
    public static Function<IOException, DirBakerException> directoryListFailure(
            final Path dirPath) {

        return cause -> directoryListFailure(dirPath, cause);
    }

}
