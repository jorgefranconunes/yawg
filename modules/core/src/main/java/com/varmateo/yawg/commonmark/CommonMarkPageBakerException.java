/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.commonmark;

import java.io.IOException;
import java.nio.file.Path;

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
    public static CommonMarkPageBakerException bakeFailure(
            final Path sourcePath,
            final Path targetDir,
            final IOException cause) {

        final String msg = String.format(
                "Failed to bake markdown file \"%s\" into directory \"%s\"- %s - %s",
                sourcePath,
                targetDir,
                cause.getClass().getName(),
                cause.getMessage());

        return new CommonMarkPageBakerException(msg, cause);
    }
}
