/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Optional;

import com.varmateo.yawg.api.BakeOptions;
import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.core.SingleSiteBaker;
import com.varmateo.yawg.core.SiteBakerModule;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;


/**
 * Factory for <code>SiteBaker</code> objects.
 */
public final class CoreSiteBaker
        implements SiteBaker {


    private final Log _log;


    private CoreSiteBaker(final Log log) {

        _log = log;
    }


    /**
     *
     */
    public static SiteBaker create() {

        final Log log = LogFactory.createFor(CoreSiteBaker.class);

        return new CoreSiteBaker(log);
    }


    /**
     *
     */
    @Override
    public void bake(final BakeOptions options) {

        final SiteBakerModule domain = new SiteBakerModule(options);
        final SingleSiteBaker singleSiteBaker = domain.singleSiteBaker();

        final Path sourceDir = options.sourceDir();
        final Path targetDir = options.targetDir();
        final Optional<Path> templatesDir = options.templatesDir();
        final Optional<Path> assetsDir = options.assetsDir();

        _log.info("{0} {1}", YawgInfo.PRODUCT_NAME, YawgInfo.VERSION);
        _log.info("    Source    : {0}", sourceDir);
        _log.info("    Target    : {0}", targetDir);
        _log.info(
                "    Templates : {0}",
                templatesDir.map(Path::toString).orElse("NONE"));
        _log.info(
                "    Assets    : {0}",
                assetsDir.map(Path::toString).orElse("NONE"));

        singleSiteBaker.bake(options);
    }


}
