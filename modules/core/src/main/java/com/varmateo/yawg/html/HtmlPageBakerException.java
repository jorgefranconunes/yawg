/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;

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
    public static HtmlPageBakerException bakeFailure(
            final Path sourcePath,
            final Path targetDir,
            final IOException cause) {

        final String msg = String.format(
                "Failed to bake HTML file \"%s\" into directory \"%s\"- %s - %s",
                sourcePath,
                targetDir,
                cause.getClass().getName(),
                cause.getMessage());

        return new HtmlPageBakerException(msg, cause);
    }

}
