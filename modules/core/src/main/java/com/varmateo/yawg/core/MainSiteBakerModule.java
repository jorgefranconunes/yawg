/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import io.vavr.Lazy;

import com.varmateo.yawg.api.SiteBaker;


/**
 *
 */
final class MainSiteBakerModule {


    private final Lazy<DefaultSiteBakerModule> _defaultSiteBakerModule =
            Lazy.of(this::createDefaultSiteBakerModule);

    private final Lazy<DefaultExtensionsModule> _defaultExtensionsModule =
            Lazy.of(this::createDefaultExtensionsModule);


    private MainSiteBakerModule() {
        // Nothing to do.
    }


    /**
     *
     */
    public static MainSiteBakerModule create() {

        return new MainSiteBakerModule();
    }


    /**
     *
     */
    public SiteBaker siteBaker() {

        return _defaultSiteBakerModule.get().siteBaker();
    }


    private DefaultSiteBakerModule createDefaultSiteBakerModule() {

        return DefaultSiteBakerModule.create(
                () -> _defaultExtensionsModule.get().pageBakers(),
                () -> _defaultExtensionsModule.get().templateServiceFactories(),
                () -> _defaultExtensionsModule.get().dirBakeListeners());
    }


    private DefaultExtensionsModule createDefaultExtensionsModule() {

        return DefaultExtensionsModule.create();
    }

}
