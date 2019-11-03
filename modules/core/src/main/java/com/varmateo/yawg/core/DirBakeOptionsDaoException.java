/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Path;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
/* default */ final class DirBakeOptionsDaoException
        extends YawgException {


    private DirBakeOptionsDaoException(final String msg) {

        super(msg);
    }


    private DirBakeOptionsDaoException(
            final String msg,
            final Throwable cause) {

        super(msg, cause);
    }


    /**
     *
     */
    public static DirBakeOptionsDaoException loadConfigFailure(
            final Path confFile,
            final IOException cause) {

        final String msg = String.format(
                "Failed to load config file \"%s\" - %s - %s",
                confFile,
                cause.getClass().getName(),
                cause.getMessage());

        return new DirBakeOptionsDaoException(msg, cause);
    }


    /**
     *
     */
    public static DirBakeOptionsDaoException invalidGlob(
            final String glob,
            final Integer index,
            final String key) {

        final String msg = String.format(
                "Invalid glob \"%s\" on item %d of %s",
                glob,
                index,
                key);

        return new DirBakeOptionsDaoException(msg);
    }


    /**
     *
     */
    public static DirBakeOptionsDaoException invalidPath(
            final String pathStr,
            final Integer index,
            final String key) {

        final String msg = String.format(
                "Invalid path \"%s\" on item %d of %s",
                pathStr,
                index,
                key);

        return new DirBakeOptionsDaoException(msg);
    }

}
