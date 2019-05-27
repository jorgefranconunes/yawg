/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import io.vavr.Lazy;
import io.vavr.control.Option;

import com.varmateo.yawg.api.SiteBaker;


/**
 *
 */
/* default */ final class MainSiteBakerModule {


    private final Option<Path> _templatesDir;

    private final Lazy<CoreSiteBakerModule> _defaultSiteBakerModule =
            Lazy.of(this::createCoreSiteBakerModule);

    private final Lazy<DefaultExtensionsModule> _defaultExtensionsModule =
            Lazy.of(this::createDefaultExtensionsModule);


    private MainSiteBakerModule(final Option<Path> templatesDir) {

        _templatesDir = templatesDir;
    }


    /**
     *
     */
    public static MainSiteBakerModule create(final Option<Path> templatesDir) {

        return new MainSiteBakerModule(templatesDir);
    }


    /**
     *
     */
    public SiteBaker siteBaker() {

        return _defaultSiteBakerModule.get().siteBaker();
    }


    private CoreSiteBakerModule createCoreSiteBakerModule() {

        return CoreSiteBakerModule.create(
                () -> _defaultExtensionsModule.get().pageBakers(),
                () -> _defaultExtensionsModule.get().templateServices(),
                () -> _defaultExtensionsModule.get().dirBakeListeners());
    }


    private DefaultExtensionsModule createDefaultExtensionsModule() {

        return DefaultExtensionsModule.create(_templatesDir);
    }

}
