/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.nio.file.Path;
import java.util.function.Function;

import io.vavr.control.Try;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
public final class AsciidoctorPageBakerException
        extends YawgException {


    private AsciidoctorPageBakerException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     *
     */
    public static AsciidoctorPageBakerException bakeFailure(
            final Path sourcePath,
            final Path targetDir,
            final Exception cause) {

        final String msg = String.format(
                "Failed to bake asciidoc file \"%s\" into directory \"%s\"- %s - %s",
                sourcePath,
                targetDir,
                cause.getClass().getName(),
                cause.getMessage());

        return new AsciidoctorPageBakerException(msg, cause);
    }


    /**
     *
     */
    public static AsciidoctorPageBakerException asciidocFailure(
            final Path sourcePath,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to process asciidoc file \"%s\" - %s - %s",
                sourcePath, cause.getClass().getName(), cause.getMessage());

        return new AsciidoctorPageBakerException(msg, cause);
    }


    /**
     *
     */
    public static <T> Try<T> asciidocFailureTry(
            final Path sourcePath,
            final Throwable cause) {

        return Try.failure(asciidocFailure(sourcePath, cause));
    }


    /**
     *
     */
    public static <T> Function<Throwable, Try<T>> asciidocFailureTry(final Path sourcePath) {

        return (Throwable cause) -> asciidocFailureTry(sourcePath, cause);
    }


    /**
     *
     */
    public static AsciidoctorPageBakerException templateFailure(
            final Path sourcePath,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to process template for \"%s\" - %s - %s",
                sourcePath, cause.getClass().getName(), cause.getMessage());

        return new AsciidoctorPageBakerException(msg, cause);
    }
}
