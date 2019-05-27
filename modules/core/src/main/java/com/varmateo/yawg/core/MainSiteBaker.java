/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import io.vavr.control.Option;

import com.varmateo.yawg.api.SiteBaker;


/**
 *
 */
public final class MainSiteBaker {


    private MainSiteBaker() {
        // Nothing to do.
    }


    /**
     *
     */
    public static SiteBaker create() {

        final Option<Path> noneTemplatesDir = Option.none();
        final MainSiteBakerModule module = MainSiteBakerModule.create(noneTemplatesDir);

        return module.siteBaker();
    }


    /**
     *
     */
    public static SiteBaker create(final Path templatesDir) {

        final Option<Path> someTemplatesDir = Option.some(templatesDir);
        final MainSiteBakerModule module = MainSiteBakerModule.create(someTemplatesDir);

        return module.siteBaker();
    }

}
