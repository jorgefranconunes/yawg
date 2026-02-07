/**************************************************************************
 *
 * Copyright (c) 2019-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Optional;

import com.varmateo.yawg.api.SiteBaker;

/**
 * Entry point for obtaining a {@code SiteBaker} instance with default
 * functionalities.
 */
public final class DefaultSiteBaker {

    private DefaultSiteBaker() {
        // Nothing to do.
    }

    /**
     * Creates a new {@code SiteBaker} without any templates.
     */
    public static SiteBaker create() {
        final Optional<Path> noneTemplatesDir = Optional.empty();
        final DefaultSiteBakerModule module = DefaultSiteBakerModule.create(noneTemplatesDir);

        return module.siteBaker();
    }

    /**
     * Creates a new {@code SiteBaker} that uses templates from the
     * specified directory.
     */
    public static SiteBaker create(final Path templatesDir) {
        final Optional<Path> someTemplatesDir = Optional.of(templatesDir);
        final DefaultSiteBakerModule module = DefaultSiteBakerModule.create(someTemplatesDir);

        return module.siteBaker();
    }

}
