/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.nio.file.Path;

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
}
