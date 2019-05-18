/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import io.vavr.control.Option;

import com.varmateo.yawg.api.BakeOptions;
import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.logging.Logs;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;

import java.util.function.Function;


/**
 * 
 */
/* default */ final class DefaultSiteBaker
        implements SiteBaker {


    private static final String DEFAULT_TEMPLATE_NAME = "default.ftlh";

    private final Log _log;
    private final AssetsCopier _assetsCopier;
    private final Function<Option<Path>, DirBaker> _dirBakerFactory;


    private DefaultSiteBaker(
            final Log log,
            final AssetsCopier assetsCopier,
            final Function<Option<Path>, DirBaker> dirBakerFactory) {

        _log = log;
        _assetsCopier = assetsCopier;
        _dirBakerFactory = dirBakerFactory;
    }


    /**
     *
     */
    public static SiteBaker create(
            final AssetsCopier assetsCopier,
            final Function<Option<Path>, DirBaker> dirBakerFactory) {

        final Log log = LogFactory.createFor(DefaultSiteBaker.class);

        return new DefaultSiteBaker(log, assetsCopier, dirBakerFactory);
    }


    /**
     *
     */
    @Override
    public void bake(final BakeOptions options) {

        final DirBaker dirBaker = _dirBakerFactory.apply(Option.ofOptional(options.templatesDir()));

        Logs.logDuration(
                _log,
                "baking",
                () -> doBake(options, dirBaker));
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
        final String templatesDir = options.templatesDir().map(Path::toString).orElse("NONE");
        final String assetsDir = options.assetsDir().map(Path::toString).orElse("NONE");

        _log.info("    Source    : {0}", sourceDir);
        _log.info("    Target    : {0}", targetDir);
        _log.info("    Templates : {0}", templatesDir);
        _log.info("    Assets    : {0}", assetsDir);

        copyAssets(options);
        bakeSourceTree(options, dirBaker);
    }


    private void copyAssets(final BakeOptions options) {

        Option.ofOptional(options.assetsDir())
                .peek(assetsDir -> _assetsCopier.copy(assetsDir, options.targetDir()))
                .onEmpty(() -> _log.debug("No assets directory to copy"));
    }


    private void bakeSourceTree(
            final BakeOptions options,
            final DirBaker dirBaker)
            throws YawgException {

        Logs.logDuration(
                _log,
                "baking source tree",
                () -> doBakeSourceTree(options, dirBaker));
    }


    private void doBakeSourceTree(
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
