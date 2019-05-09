/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import com.varmateo.yawg.api.SiteBakerOptions;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.core.DirBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.Logs;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;


/**
 * Baker for one specific site.
 */
/* default */ final class SingleSiteBaker {


    private final Log _log;
    private final SiteBakerOptions _conf;
    private final AssetsCopier _assetsCopier;
    private final SourceTreeBaker _sourceTreeBaker;

    private final Runnable _bakeAction;


    /**
     * @param conf All the parameters needed for performing a bake.
     */
    SingleSiteBaker(
            final Log log,
            final SiteBakerOptions conf,
            final DirBaker dirBaker) {

        PageVars pageVars = mapToPageVars(conf.externalPageVars());

        _log = log;
        _conf = conf;
        _assetsCopier = new AssetsCopier(
                log,
                conf.assetsDir(),
                conf.targetDir());
        _sourceTreeBaker = new SourceTreeBaker(
                log,
                conf.sourceDir(),
                conf.targetDir(),
                pageVars,
                dirBaker);

        _bakeAction = Logs.decorateWithLogDuration(
                log,
                "bake",
                this::doBake);
    }


    /**
     *
     */
    private static PageVars mapToPageVars(
            final Map<String,Object> pageVarsMap) {

        PageVarsBuilder builder = PageVarsBuilder.create();
        pageVarsMap.entrySet().stream()
                .forEach(x -> builder.addVar(x.getKey(), x.getValue()));

        return builder.build();
    }


    /**
     * Performs the baking of one directory tree into another.
     *
     * @exception YawgException Thrown if the baking could not be
     * completed for whatever reason.
     */
    public void bake()
            throws YawgException {

        Path sourceDir = _conf.sourceDir();
        Path targetDir = _conf.targetDir();
        Optional<Path> templatesDir = _conf.templatesDir();
        Optional<Path> assetsDir = _conf.assetsDir();

        _log.info("{0} {1}", YawgInfo.PRODUCT_NAME, YawgInfo.VERSION);
        _log.info("    Source    : {0}", sourceDir);
        _log.info("    Target    : {0}", targetDir);
        _log.info(
                "    Templates : {0}",
                templatesDir.map(Path::toString).orElse("NONE"));
        _log.info(
                "    Assets    : {0}",
                assetsDir.map(Path::toString).orElse("NONE"));

        _bakeAction.run();
    }


    /**
     *
     */
    private void doBake()
            throws YawgException {

        _assetsCopier.copy();
        _sourceTreeBaker.bake();
    }


}
