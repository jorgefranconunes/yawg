/**************************************************************************
 *
 * Copyright (c) 2017-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.core.DirBaker;
import com.varmateo.yawg.core.DirBakeOptions;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.logging.Logs;
import com.varmateo.yawg.spi.PageVars;


/**
 *
 */
/* default */ final class SourceTreeBaker {


    private static final String DEFAULT_TEMPLATE_NAME = "default.ftlh";

    private final Log _log;
    private final DirBaker _dirBaker;


    /* default */ SourceTreeBaker(
            final Log log,
            final DirBaker dirBaker) {

        _log = log;
        _dirBaker = dirBaker;
    }


    /**
     *
     */
    public static SourceTreeBaker create(final DirBaker dirBaker) {

        final Log log = LogFactory.createFor(SourceTreeBaker.class);

        return new SourceTreeBaker(log, dirBaker);
    }


    /**
     *
     */
    public void bake(
            final Path sourceDir,
            final Path targetDir,
            final PageVars externalPageVars)
            throws YawgException {

        Logs.logDuration(
                _log,
                "baking source tree",
                () -> doBake(sourceDir, targetDir, externalPageVars));
    }


    /**
     *
     */
    private void doBake(
            final Path sourceDir,
            final Path targetDir,
            final PageVars externalPageVars)
            throws YawgException {

        final DirBakeOptions dirBakeOptions = DirBakeOptions.builder()
                .templateName(DEFAULT_TEMPLATE_NAME)
                .pageVars(externalPageVars)
                .build();

        _dirBaker.bakeDirectory(sourceDir, targetDir, dirBakeOptions);
    }


}
