/**************************************************************************
 *
 * Copyright (c) 2019-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Optional;

import io.vavr.Lazy;

import com.varmateo.yawg.api.SiteBaker;

final class DefaultSiteBakerModule {

    private final Optional<Path> _templatesDir;

    private final Lazy<CoreSiteBakerModule> _defaultSiteBakerModule =
            Lazy.of(this::createCoreSiteBakerModule);

    private final Lazy<DefaultExtensionsModule> _defaultExtensionsModule =
            Lazy.of(this::createDefaultExtensionsModule);

    private DefaultSiteBakerModule(final Optional<Path> templatesDir) {
        _templatesDir = templatesDir;
    }

    public static DefaultSiteBakerModule create(final Optional<Path> templatesDir) {
        return new DefaultSiteBakerModule(templatesDir);
    }

    public SiteBaker siteBaker() {
        return _defaultSiteBakerModule.get().siteBaker();
    }

    private CoreSiteBakerModule createCoreSiteBakerModule() {
        return CoreSiteBakerModule.create(
                () -> _defaultExtensionsModule.get().pageBakers(),
                () -> _defaultExtensionsModule.get().templateServices(),
                () -> _defaultExtensionsModule.get().dirBakeListeners()
        );
    }

    private DefaultExtensionsModule createDefaultExtensionsModule() {
        return DefaultExtensionsModule.create(_templatesDir);
    }
}
