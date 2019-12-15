/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.io.IOException;
import java.nio.file.Path;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
/* default */ final class FreemarkerTemplateServiceException
        extends YawgException {


    private FreemarkerTemplateServiceException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     *
     */
    public static FreemarkerTemplateServiceException initializationFailure(
            final Path templatesDir,
            final IOException cause) {

        final String msg = String.format(
                "Failed to initialize template engine with \"%s\"- %s - %s",
                templatesDir,
                cause.getClass().getName(),
                cause.getMessage());

        return new FreemarkerTemplateServiceException(msg, cause);
    }


    /**
     *
     */
    public static FreemarkerTemplateServiceException fetchFailure(
            final String templateName,
            final IOException cause) {

        final String msg = String.format(
                "Failed to fetch template \"%s\" - %s - %s",
                templateName,
                cause.getClass().getName(),
                cause.getMessage());

        return new FreemarkerTemplateServiceException(msg, cause);
    }


    /**
     *
     */
    public static FreemarkerTemplateServiceException processingFailure(
            final String templateName,
            final Throwable cause) {

        final String msg = String.format(
                "Failed to process template \"%s\" - %s - %s",
                templateName,
                cause.getClass().getName(),
                cause.getMessage());

        return new FreemarkerTemplateServiceException(msg, cause);
    }

}
