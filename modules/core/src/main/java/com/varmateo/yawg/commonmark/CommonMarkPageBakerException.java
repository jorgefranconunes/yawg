/**************************************************************************
 *
 * Copyright (c) 2019-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.commonmark;

import java.nio.file.Path;
import java.util.function.Function;

import io.vavr.control.Try;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
public final class CommonMarkPageBakerException
        extends YawgException {


    private CommonMarkPageBakerException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     *
     */
    public static CommonMarkPageBakerException commonMarkFailure(
            final Path sourcePath,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to process markdown file \"%s\" - %s - %s",
                sourcePath,
                cause.getClass().getName(),
                cause.getMessage());

        return new CommonMarkPageBakerException(msg, cause);
    }


    /**
     *
     */
    public static <T> Try<T> commonMarkFailureTry(
            final Path sourcePath,
            final Throwable cause) {

        return Try.failure(commonMarkFailure(sourcePath, cause));
    }


    /**
     *
     */
    public static <T> Function<Throwable, Try<T>> commonMarkFailureTry(final Path sourcePath) {

        return (Throwable cause) -> commonMarkFailureTry(sourcePath, cause);
    }


    /**
     *
     */
    public static CommonMarkPageBakerException templateFailure(
            final Path sourcePath,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to process template for \"%s\" - %s - %s",
                sourcePath, cause.getClass().getName(), cause.getMessage());

        return new CommonMarkPageBakerException(msg, cause);
    }


    /**
     *
     */
    public static <T> Try<T> templateFailureTry(
            final Path sourcePath,
            final Throwable cause) {

        return Try.failure(templateFailure(sourcePath, cause));
    }


    /**
     *
     */
    public static <T> Function<Throwable, Try<T>> templateFailureTry(final Path sourcePath) {

        return (Throwable cause) -> templateFailureTry(sourcePath, cause);
    }


    /**
     *
     */
    public static CommonMarkPageBakerException parseFailure(
            final Path sourcePath,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to parse markdown file \"%s\" - %s - %s",
                sourcePath, cause.getClass().getName(), cause.getMessage());

        return new CommonMarkPageBakerException(msg, cause);
    }


    /**
     *
     */
    public static <T> Try<T> parseFailureTry(
            final Path sourcePath,
            final Throwable cause) {

        return Try.failure(parseFailure(sourcePath, cause));
    }


    /**
     *
     */
    public static <T> Function<Throwable, Try<T>> parseFailureTry(final Path sourcePath) {

        return (Throwable cause) -> parseFailureTry(sourcePath, cause);
    }
}
