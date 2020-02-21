/**************************************************************************
 *
 * Copyright (c) 2019-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Map;

import io.vavr.control.Option;
import io.vavr.control.Try;

import com.varmateo.yawg.api.BakeOptions;
import com.varmateo.yawg.api.Result;
import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.logging.Logs;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
import com.varmateo.yawg.util.Results;


/**
 * 
 */
/* default */ final class CoreSiteBaker
        implements SiteBaker {


    private static final String DEFAULT_TEMPLATE_NAME = "default.ftlh";

    private final Log _log;
    private final AssetsCopier _assetsCopier;
    private final DirBaker _dirBaker;


    private CoreSiteBaker(
            final Log log,
            final AssetsCopier assetsCopier,
            final DirBaker dirBaker) {

        _log = log;
        _assetsCopier = assetsCopier;
        _dirBaker = dirBaker;
    }


    /**
     *
     */
    public static SiteBaker create(
            final AssetsCopier assetsCopier,
            final DirBaker dirBaker) {

        final Log log = LogFactory.createFor(CoreSiteBaker.class);

        return new CoreSiteBaker(log, assetsCopier, dirBaker);
    }


    /**
     *
     */
    @Override
    public Result<Void> bake(final BakeOptions options) {

        return Results.fromTry(
                Try.run(
                        () -> Logs.logDuration(
                                _log,
                                "baking",
                                () -> doBake(options, _dirBaker))));
    }


    /**
     *
     */
    private void doBake(
            final BakeOptions options,
            final DirBaker dirBaker)
            throws YawgException {

        final Path sourceDir = options.sourceDir();
        final Path targetDir = options.targetDir();
        final PageVars externalPageVars = mapToPageVars(options.externalPageVars());
        final DirBakeOptions dirBakeOptions = DirBakeOptions.builder()
                .templateName(DEFAULT_TEMPLATE_NAME)
                .pageVars(externalPageVars)
                .build();

        dirBaker.bakeDirectory(sourceDir, targetDir, dirBakeOptions);
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
