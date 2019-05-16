/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

import com.varmateo.yawg.api.BakeOptions;
import com.varmateo.yawg.core.CollectiveDirBakeListener;
import com.varmateo.yawg.core.CollectiveTemplateService;
import com.varmateo.yawg.core.CopyPageBaker;
import com.varmateo.yawg.core.DirBaker;
import com.varmateo.yawg.core.DirBakeOptionsDao;
import com.varmateo.yawg.core.FileBaker;
import com.varmateo.yawg.core.SingleSiteBaker;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageBakerFactory;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.DirBakeListenerFactory;
import com.varmateo.yawg.spi.TemplateService;
import com.varmateo.yawg.spi.TemplateServiceFactory;
import com.varmateo.yawg.util.Holder;
import com.varmateo.yawg.util.Services;


/**
 * Factory for all the objects required by the main baker.
 */
/* package private */ final class SiteBakerModule {


    private final BakeOptions _options;

    private final Holder<Seq<PageBaker>> _bakers = Holder.of(this::newBakers);

    private final Holder<PageBaker> _copyBaker = Holder.of(this::newCopyPageBaker);

    private final Holder<DirBakeListener> _dirBakeListener = Holder.of(this::newDirBakeListener);

    private final Holder<DirBaker> _dirBaker = Holder.of(this::newDirBaker);

    private final Holder<DirBakeOptionsDao> _dirBakerConfDao = Holder.of(this::newDirBakeOptionsDao);

    private final Holder<FileBaker> _fileBaker = Holder.of(this::newFileBaker);

    private final Holder<SingleSiteBaker> _siteBaker = Holder.of(this::newSiteBaker);

    private final Holder<TemplateService> _templateService = Holder.of(this::newTemplateService);


    /**
     * @param options Configuration parameters.
     */
    SiteBakerModule(final BakeOptions options) {

        _options = options;
    }


    /**
     * @return The baker object.
     */
    public SingleSiteBaker singleSiteBaker() {

        return _siteBaker.get();
    }


    /**
     *
     */
    private Seq<PageBaker> newBakers() {

        return Services.getAll(PageBakerFactory.class)
                .map(PageBakerFactory::createPageBaker);
    }


    /**
     *
     */
    private PageBaker newCopyPageBaker() {

        return new CopyPageBaker();
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
    private DirBaker newDirBaker() {

        return new DirBaker(
                _fileBaker.get(),
                _templateService.get(),
                _dirBakerConfDao.get(),
                _dirBakeListener.get());
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
    private FileBaker newFileBaker() {

        final Seq<PageBaker> bakers = _bakers.get();
        final PageBaker defaultBaker = _copyBaker.get();

        return new FileBaker(bakers, defaultBaker);
    }


    /**
     *
     */
    private TemplateService newTemplateService() {

        final Seq<TemplateService> allServices = Option.ofOptional(_options.templatesDir())
                .map(dirPath ->
                     Services.getAll(TemplateServiceFactory.class)
                     .map(f -> f.newTemplateService(dirPath)))
                .getOrElse(List::of);

        return new CollectiveTemplateService(allServices);
    }


    /**
     *
     */
    private SingleSiteBaker newSiteBaker() {

        return new SingleSiteBaker(_dirBaker.get());
    }


}
