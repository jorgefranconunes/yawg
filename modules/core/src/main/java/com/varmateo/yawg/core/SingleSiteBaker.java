/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import io.vavr.control.Option;

import com.varmateo.yawg.api.BakeOptions;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.core.DirBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.logging.Logs;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;


/**
 * Baker for one specific site.
 */
/* default */ final class SingleSiteBaker {


    private static final String DEFAULT_TEMPLATE_NAME = "default.ftlh";

    private final Log _log;
    private final AssetsCopier _assetsCopier;
    private final DirBaker _dirBaker;


    /**
     * @param options All the parameters needed for performing a bake.
     */
    SingleSiteBaker(final DirBaker dirBaker) {

        _log = LogFactory.createFor(SingleSiteBaker.class);
        _assetsCopier = AssetsCopier.create();
        _dirBaker = dirBaker;
    }


    /**
     * Performs the baking of one directory tree into another.
     *
     * @exception YawgException Thrown if the baking could not be
     * completed for whatever reason.
     */
    public void bake(final BakeOptions options)
            throws YawgException {

        Logs.logDuration(
                _log,
                "baking",
                () -> doBake(options));
    }


    /**
     *
     */
    private void doBake(final BakeOptions options)
            throws YawgException {

        copyAssets(options);
        bakeSourceTree(options);
    }


    private void copyAssets(final BakeOptions options) {

        Option.ofOptional(options.assetsDir())
                .peek(assetsDir -> _assetsCopier.copy(assetsDir, options.targetDir()))
                .onEmpty(() -> _log.debug("No assets directory to copy"));
    }


    private void bakeSourceTree(final BakeOptions options)
            throws YawgException {

        Logs.logDuration(
                _log,
                "baking source tree",
                () -> doBakeSourceTree(options));
    }


    private void doBakeSourceTree(final BakeOptions options)
            throws YawgException {

        final Path sourceDir = options.sourceDir();
        final Path targetDir = options.targetDir();
        final PageVars externalPageVars = mapToPageVars(options.externalPageVars());
        final DirBakeOptions dirBakeOptions = DirBakeOptions.builder()
                .templateName(DEFAULT_TEMPLATE_NAME)
                .pageVars(externalPageVars)
                .build();

        _dirBaker.bakeDirectory(sourceDir, targetDir, dirBakeOptions);
    }


    /**
     *
     */
    private static PageVars mapToPageVars(
            final Map<String,Object> pageVarsMap) {

        final PageVarsBuilder builder = PageVarsBuilder.create();
        pageVarsMap.entrySet().stream()
                .forEach(x -> builder.addVar(x.getKey(), x.getValue()));

        return builder.build();
    }


}
