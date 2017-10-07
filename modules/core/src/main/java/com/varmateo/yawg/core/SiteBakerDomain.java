/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

import com.varmateo.yawg.api.SiteBakerConf;
import com.varmateo.yawg.core.CollectiveDirBakeListener;
import com.varmateo.yawg.core.CollectiveTemplateService;
import com.varmateo.yawg.core.CopyBakerService;
import com.varmateo.yawg.core.DirBaker;
import com.varmateo.yawg.core.DirBakerConfDao;
import com.varmateo.yawg.core.FileBaker;
import com.varmateo.yawg.core.SingleSiteBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.spi.BakerService;
import com.varmateo.yawg.spi.BakerServiceFactory;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.DirBakeListenerFactory;
import com.varmateo.yawg.spi.TemplateService;
import com.varmateo.yawg.spi.TemplateServiceFactory;
import com.varmateo.yawg.util.Holder;
import com.varmateo.yawg.util.Services;


/**
 * Factory for all the objects required by the main baker.
 */
/* package private */ final class SiteBakerDomain {


    private final SiteBakerConf _conf;

    private final Holder<Seq<BakerService>> _bakers =
            Holder.of(this::newBakers);

    private final Holder<BakerService> _copyBaker =
            Holder.of(this::newCopyBakerService);

    private final Holder<DirBakeListener> _dirBakeListener =
            Holder.of(this::newDirBakeListener);

    private final Holder<DirBaker> _dirBaker =
            Holder.of(this::newDirBaker);

    private final Holder<DirBakerConfDao> _dirBakerConfDao =
            Holder.of(this::newDirBakerConfDao);

    private final Holder<FileBaker> _fileBaker =
            Holder.of(this::newFileBaker);

    private final Holder<SingleSiteBaker> _siteBaker =
            Holder.of(this::newSiteBaker);

    private final Holder<TemplateService> _templateService =
            Holder.of(this::newTemplateService);

    private final Holder<Log> _log =
            Holder.of(this::newLog);


    /**
     * @param conf Configuration parameters.
     */
    SiteBakerDomain(final SiteBakerConf conf) {

        _conf = conf;
    }


    /**
     * @return The baker object.
     */
    public SingleSiteBaker getSingleSiteBaker() {

        return _siteBaker.get();
    }


    /**
     *
     */
    private Seq<BakerService> newBakers() {

        return Services.getAll(BakerServiceFactory.class)
                .map(BakerServiceFactory::newBakerService);
    }


    /**
     *
     */
    private BakerService newCopyBakerService() {

        return new CopyBakerService();
    }


    /**
     *
     */
    private DirBakeListener newDirBakeListener() {

        Seq<DirBakeListener> allListeners =
                Services.getAll(DirBakeListenerFactory.class)
                .map(DirBakeListenerFactory::newDirBakeListener);

        return new CollectiveDirBakeListener(allListeners);
    }


    /**
     *
     */
    private DirBaker newDirBaker() {

        return new DirBaker(
                _log.get(),
                _conf.getSourceDir(),
                _conf.getTargetDir(),
                _fileBaker.get(),
                _templateService.get(),
                _dirBakerConfDao.get(),
                _dirBakeListener.get());
    }


    /**
     *
     */
    private DirBakerConfDao newDirBakerConfDao() {

        return new DirBakerConfDao();
    }


    /**
     *
     */
    private FileBaker newFileBaker() {

        Log log = _log.get();
        Seq<BakerService> bakers = _bakers.get();
        BakerService defaultBaker = _copyBaker.get();

        return new FileBaker(log, bakers, defaultBaker);
    }


    /**
     *
     */
    private TemplateService newTemplateService() {

        Seq<TemplateService> allServices =
                Option.ofOptional(_conf.getTemplatesDir())
                .map(dirPath ->
                     Services.getAll(TemplateServiceFactory.class)
                     .map(f -> f.newTemplateService(dirPath)))
                .getOrElse(List::of);

        return new CollectiveTemplateService(allServices);
    }


    /**
     *
     */
    private Log newLog() {

        return LogFactory.createFor(this);
    }


    /**
     *
     */
    private SingleSiteBaker newSiteBaker() {

        return new SingleSiteBaker(_log.get(), _conf, _dirBaker.get());
    }


}
