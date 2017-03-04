/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import com.varmateo.yawg.api.PageVars;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.core.DirBaker;
import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.logging.LogWithUtils;


/**
 *
 */
/* package private */ final class SourceTreeBaker {


    private static final String DEFAULT_TEMPLATE_NAME = "default.ftlh";


    private final LogWithUtils _log;
    private final Path _sourceDir;
    private final Path _targetDir;
    private final PageVars _externalPageVars;
    private final DirBaker _dirBaker;


    SourceTreeBaker(
            final LogWithUtils log,
            final Path sourceDir,
            final Path targetDir,
            final PageVars externalPageVars,
            final DirBaker dirBaker) {

        _log = log;
        _sourceDir = sourceDir;
        _targetDir = targetDir;
        _externalPageVars = externalPageVars;
        _dirBaker = dirBaker;
    }


    /**
     *
     */
    public void bake()
            throws YawgException {

        _log.logDelay("baking source tree", this::doBake);
    }


    /**
     *
     */
    private void doBake()
            throws YawgException {

        DirBakerConf dirBakerConf =
                DirBakerConf.builder()
                .setTemplateName(DEFAULT_TEMPLATE_NAME)
                .setPageVars(_externalPageVars)
                .build();

        _dirBaker.bakeDirectory(_sourceDir, _targetDir, dirBakerConf);
    }


}
