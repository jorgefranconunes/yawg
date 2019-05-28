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
 * Entry point for obtaining a {@code SiteBaker} instance.
 */
public final class DefaultSiteBaker {


    private DefaultSiteBaker() {
        // Nothing to do.
    }


    /**
     * Creates a new {@code SiteBaker} without any templates.
     */
    public static SiteBaker create() {

        final Option<Path> noneTemplatesDir = Option.none();
        final DefaultSiteBakerModule module = DefaultSiteBakerModule.create(noneTemplatesDir);

        return module.siteBaker();
    }


    /**
     * Creates a new {@code SiteBaker} that uses templates from the
     * specified directory.
     */
    public static SiteBaker create(final Path templatesDir) {

        final Option<Path> someTemplatesDir = Option.some(templatesDir);
        final DefaultSiteBakerModule module = DefaultSiteBakerModule.create(someTemplatesDir);

        return module.siteBaker();
    }

}
