/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.function.Function;

import io.vavr.Lazy;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

import com.varmateo.yawg.api.SiteBaker;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageBakerFactory;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.DirBakeListenerFactory;
import com.varmateo.yawg.spi.TemplateService;
import com.varmateo.yawg.spi.TemplateServiceFactory;
import com.varmateo.yawg.util.Services;


/**
 * Factory for all the objects required by the main baker.
 */
/* default */ final class DefaultSiteBakerModule {


    private final Lazy<SiteBaker> _siteBaker = Lazy.of(this::newSiteBaker);

    private final Lazy<Seq<TemplateServiceFactory>> _templateServiceFactories =
            Lazy.of(this::newTemplateServiceFactories);

    private final Lazy<FileBaker> _fileBaker = Lazy.of(this::newFileBaker);

    private final Lazy<DirBakeOptionsDao> _dirBakerOptionsDao =
            Lazy.of(this::newDirBakeOptionsDao);

    private final Lazy<DirBakeListener> _dirBakeListener = Lazy.of(this::newDirBakeListener);

    private final Lazy<Seq<PageBaker>> _pageBakers = Lazy.of(this::newPageBakers);

    private final Lazy<PageBaker> _defaultPageBaker = Lazy.of(this::newDefaultPageBaker);


    /**
     * @param options Configuration parameters.
     */
    DefaultSiteBakerModule() {
        // Nothing to do.
    }


    /**
     * @return The baker object.
     */
    public SiteBaker siteBaker() {

        return _siteBaker.get();
    }


    private SiteBaker newSiteBaker() {

        final AssetsCopier assetsCopier = AssetsCopier.create();
        final Function<Option<Path>, DirBaker> dirBakerFactory = this::newDirBaker;

        return DefaultSiteBaker.create(assetsCopier, dirBakerFactory);
    }


    /**
     *
     */
    private DirBaker newDirBaker(final Option<Path> templatesDir) {

        final Function<Path, Seq<TemplateService>> createTemplateServices =
                path -> _templateServiceFactories.get().map(f -> f.newTemplateService(path));
        final Seq<TemplateService> allTemplateServices = templatesDir
                .map(createTemplateServices)
                .getOrElse(List::empty);
        final TemplateService templateService = new CollectiveTemplateService(allTemplateServices);

        return new DirBaker(
                _fileBaker.get(),
                templateService,
                _dirBakerOptionsDao.get(),
                _dirBakeListener.get());
    }


    private Seq<TemplateServiceFactory> newTemplateServiceFactories() {

        return Services.getAll(TemplateServiceFactory.class);
    }


    /**
     *
     */
    private FileBaker newFileBaker() {

        final Seq<PageBaker> bakers = _pageBakers.get();
        final PageBaker defaultBaker = _defaultPageBaker.get();

        return new FileBaker(bakers, defaultBaker);
    }


    /**
     *
     */
    private DirBakeOptionsDao newDirBakeOptionsDao() {

        return new DirBakeOptionsDao();
    }


    /**
     *
     */
    private DirBakeListener newDirBakeListener() {

        final Seq<DirBakeListener> allListeners = Services.getAll(DirBakeListenerFactory.class)
                .map(DirBakeListenerFactory::newDirBakeListener);

        return new CollectiveDirBakeListener(allListeners);
    }


    /**
     *
     */
    private Seq<PageBaker> newPageBakers() {

        return Services.getAll(PageBakerFactory.class)
                .map(PageBakerFactory::createPageBaker);
    }


    /**
     *
     */
    private PageBaker newDefaultPageBaker() {

        return new CopyPageBaker();
    }


}
