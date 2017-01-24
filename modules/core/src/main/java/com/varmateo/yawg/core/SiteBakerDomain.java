/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import javaslang.collection.List;
import javaslang.collection.Seq;
import javaslang.control.Option;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.BakerFactory;
import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.DirBakeListenerFactory;
import com.varmateo.yawg.SiteBakerConf;
import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.TemplateServiceFactory;

import com.varmateo.yawg.core.CollectiveDirBakeListener;
import com.varmateo.yawg.core.CollectiveTemplateService;
import com.varmateo.yawg.core.CopyBaker;
import com.varmateo.yawg.core.DirBaker;
import com.varmateo.yawg.core.DirBakerConfDao;
import com.varmateo.yawg.core.FileBaker;
import com.varmateo.yawg.core.SingleSiteBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.util.Holder;
import com.varmateo.yawg.util.Services;


/**
 * Factory for all the objects required by the main baker.
 */
/* package private */ final class SiteBakerDomain {


    private final SiteBakerConf _conf;

    private final Holder<Seq<Baker>> _bakers =
            Holder.of(this::newBakers);

    private final Holder<Baker> _copyBaker =
            Holder.of(this::newCopyBaker);

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
    private Seq<Baker> newBakers() {

        return getAllServices(BakerFactory.class)
                .map(BakerFactory::newBaker);
    }


    /**
     *
     */
    private Baker newCopyBaker() {

        return new CopyBaker();
    }


    /**
     *
     */
    private DirBakeListener newDirBakeListener() {

        Seq<DirBakeListener> allListeners =
                getAllServices(DirBakeListenerFactory.class)
                .map(DirBakeListenerFactory::newDirBakeListener);
        DirBakeListener result = new CollectiveDirBakeListener(allListeners);

        return result;
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
        Seq<Baker> bakers = _bakers.get();
        Baker defaultBaker = _copyBaker.get();
        FileBaker result = new FileBaker(log, bakers, defaultBaker);

        return result;
    }


    /**
     *
     */
    private TemplateService newTemplateService() {

        Seq<TemplateService> allServices =
                Option.ofOptional(_conf.getTemplatesDir())
                .map(dirPath ->
                     getAllServices(TemplateServiceFactory.class)
                     .map(f -> f.newTemplateService(dirPath)))
                .getOrElse(List::of);
        TemplateService result = new CollectiveTemplateService(allServices);

        return result;
    }


    /**
     *
     */
    private Log newLog() {

        Log result = LogFactory.createFor(this);

        return result;
    }


    /**
     *
     */
    private SingleSiteBaker newSiteBaker() {

        return new SingleSiteBaker(_log.get(), _conf, _dirBaker.get());
    }


    /**
     *
     */
    private <T> Seq<T> getAllServices(final Class<T> klass) {

        return Services.getAll(klass);
    }


}
