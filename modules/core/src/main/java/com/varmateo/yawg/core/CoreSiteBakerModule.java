/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.function.Supplier;

import io.vavr.Lazy;
import io.vavr.collection.Seq;

import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.TemplateService;


/**
 * Module for all the objects required by the main baker.
 */
/* default */ final class CoreSiteBakerModule {


    private final Lazy<SiteBaker> _siteBaker = Lazy.of(this::newSiteBaker);

    private final Supplier<Seq<PageBaker>> _pageBakers;
    private final Supplier<Seq<TemplateService>> _templateServices;
    private final Supplier<Seq<DirBakeListener>> _dirBakeListeners;


    private CoreSiteBakerModule(
            final Supplier<Seq<PageBaker>> pageBakers,
            final Supplier<Seq<TemplateService>> templateServices,
            final Supplier<Seq<DirBakeListener>> dirBakeListeners) {

        _pageBakers = pageBakers;
        _templateServices = templateServices;
        _dirBakeListeners = dirBakeListeners;
    }


    /**
     *
     */
    public static CoreSiteBakerModule create(
            final Supplier<Seq<PageBaker>> pageBakers,
            final Supplier<Seq<TemplateService>> templateServices,
            final Supplier<Seq<DirBakeListener>> dirBakeListeners) {

        return new CoreSiteBakerModule(pageBakers, templateServices, dirBakeListeners);
    }


    /**
     * @return The baker object.
     */
    public SiteBaker siteBaker() {

        return _siteBaker.get();
    }


    private SiteBaker newSiteBaker() {

        final DirBaker dirBaker = newDirBaker();

        return CoreSiteBaker.create(dirBaker);
    }


    /**
     *
     */
    private DirBaker newDirBaker() {

        final FileBaker fileBaker =
                newFileBaker();
        final TemplateService collectiveTemplateService =
                new CollectiveTemplateService(_templateServices.get());
        final DirBakeOptionsDao dirBakerOptionsDao =
                new DirBakeOptionsDao();
        final DirBakeListener dirBakeListener =
                new CollectiveDirBakeListener(_dirBakeListeners.get());

        return new DirBaker(
                fileBaker,
                collectiveTemplateService,
                dirBakerOptionsDao,
                dirBakeListener);
    }


    /**
     *
     */
    private FileBaker newFileBaker() {

        final Seq<PageBaker> bakers = _pageBakers.get();
        final PageBaker defaultBaker = CopyPageBaker.create();

        return new FileBaker(bakers, defaultBaker);
    }


}
