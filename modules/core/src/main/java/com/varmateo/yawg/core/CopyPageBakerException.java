/**************************************************************************
 *
 * Copyright (c) 2019-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.function.Function;

import io.vavr.control.Try;

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


    /**
     *
     */
    public static <T> Try<T> copyFailureTry(
            final Path sourcePath,
            final Path targetDir,
            final Throwable cause) {

        return Try.failure(copyFailure(sourcePath, targetDir, cause));
    }


    /**
     *
     */
    public static <T> Function<Throwable, Try<T>> copyFailureTry(
            final Path sourcePath,
            final Path targetDir) {

        return (Throwable cause) -> copyFailureTry(sourcePath, targetDir, cause);
    }
}
