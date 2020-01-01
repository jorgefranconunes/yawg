/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.nio.file.Path;
import java.util.function.Function;

import io.vavr.control.Try;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
public final class HtmlPageBakerException
        extends YawgException {


    private HtmlPageBakerException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     *
     */
    public static HtmlPageBakerException copyFailure(
            final Path sourcePath,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to copy HTML file \"%s\" - %s - %s",
                sourcePath,
                cause.getClass().getName(),
                cause.getMessage());

        return new HtmlPageBakerException(msg, cause);
    }


    /**
     *
     */
    public static <T> Try<T> copyFailureTry(
            final Path sourcePath,
            final Throwable cause) {

        return Try.failure(copyFailure(sourcePath, cause));
    }


    /**
     *
     */
    public static <T> Function<Throwable, Try<T>> copyFailureTry(final Path sourcePath) {

        return (Throwable cause) -> copyFailureTry(sourcePath, cause);
    }


    /**
     *
     */
    public static HtmlPageBakerException templateFailure(
            final Path sourcePath,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to process template for \"%s\" - %s - %s",
                sourcePath, cause.getClass().getName(), cause.getMessage());

        return new HtmlPageBakerException(msg, cause);
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
    public static HtmlPageBakerException parseFailure(
            final Path sourcePath,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to parse markdown file \"%s\" - %s - %s",
                sourcePath, cause.getClass().getName(), cause.getMessage());

        return new HtmlPageBakerException(msg, cause);
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
