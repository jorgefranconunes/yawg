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
import com.varmateo.yawg.logging.Logs;
import com.varmateo.yawg.spi.PageVars;


/**
 *
 */
/* default */ final class SourceTreeBaker {


    private static final String DEFAULT_TEMPLATE_NAME = "default.ftlh";


    private final Path _sourceDir;
    private final Path _targetDir;
    private final PageVars _externalPageVars;
    private final DirBaker _dirBaker;

    private final Runnable _bakeAction;


    /* default */ SourceTreeBaker(
            final Log log,
            final Path sourceDir,
            final Path targetDir,
            final PageVars externalPageVars,
            final DirBaker dirBaker) {

        _sourceDir = sourceDir;
        _targetDir = targetDir;
        _externalPageVars = externalPageVars;
        _dirBaker = dirBaker;

        _bakeAction = Logs.decorateWithLogDuration(
                log,
                "baking source tree",
                this::doBake);
    }


    /**
     *
     */
    public void bake()
            throws YawgException {

        _bakeAction.run();
    }


    /**
     *
     */
    private void doBake()
            throws YawgException {

        final DirBakeOptions dirBakeOptions = DirBakeOptions.builder()
                .templateName(DEFAULT_TEMPLATE_NAME)
                .pageVars(_externalPageVars)
                .build();

        _dirBaker.bakeDirectory(_sourceDir, _targetDir, dirBakeOptions);
    }


}
