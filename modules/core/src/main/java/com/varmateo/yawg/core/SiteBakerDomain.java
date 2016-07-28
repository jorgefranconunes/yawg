/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

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
import com.varmateo.yawg.util.Lists;
import com.varmateo.yawg.util.Services;


/**
 * Factory for all the objects required by the main baker.
 */
/* package private */ final class SiteBakerDomain
        extends Object {


    private final SiteBakerConf _conf;

    private final Holder<Collection<Baker>> _bakers =
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

    private final Holder<Optional<TemplateService>> _templateService =
            Holder.of(this::newTemplateService);

    private final Holder<Log> _log =
            Holder.of(this::newLog);


    /**
     * @param conf Configuration parameters.
     */
    public SiteBakerDomain(final SiteBakerConf conf) {

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
    private Collection<Baker> newBakers() {

        Collection<Baker> result =
                Lists.map(
                        getAllServices(BakerFactory.class),
                        BakerFactory::newBaker);

        return result;
    }


    /**
     *
     */
    private Baker newCopyBaker() {

        Baker result = new CopyBaker();

        return result;
    }


    /**
     *
     */
    private DirBakeListener newDirBakeListener() {

        Collection<DirBakeListener> allListeners =
                Lists.map(
                        getAllServices(DirBakeListenerFactory.class),
                        f -> f.newDirBakeListener());
        DirBakeListener result =
                new CollectiveDirBakeListener(allListeners);

        return result;
    }


    /**
     *
     */
    private DirBaker newDirBaker() {

        DirBaker result =
                new DirBaker(
                        _log.get(),
                        _conf.getSourceDir(),
                        _fileBaker.get(),
                        _templateService.get(),
                        _dirBakerConfDao.get(),
                        _dirBakeListener.get());
        return result;
    }


    /**
     *
     */
    private DirBakerConfDao newDirBakerConfDao() {

        DirBakerConfDao result = new DirBakerConfDao();

        return result;
    }


    /**
     *
     */
    private FileBaker newFileBaker() {

        Log log = _log.get();
        Path sourceRootDir = _conf.getSourceDir();
        Collection<Baker> bakers = _bakers.get();
        Baker defaultBaker = _copyBaker.get();
        FileBaker result =
                new FileBaker(log, sourceRootDir, bakers, defaultBaker);

        return result;
    }


    /**
     *
     */
    private Optional<TemplateService> newTemplateService() {

        Optional<TemplateService> result = null;
        Optional<Path> dirPath = _conf.getTemplatesDir();

        if ( dirPath.isPresent() ) {
            Collection<TemplateService> allServices =
                    Lists.map(
                            getAllServices(TemplateServiceFactory.class),
                            f -> f.newTemplateService(dirPath.get()));
            TemplateService service =
                    new CollectiveTemplateService(allServices);
            result = Optional.of(service);
        } else {
            result = Optional.empty();
        }

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

        SingleSiteBaker result =
                new SingleSiteBaker(_log.get(), _conf, _dirBaker.get());

        return result;
    }


    /**
     *
     */
    private <T> Collection<T> getAllServices(final Class<T> klass) {

        Collection<T> result = Services.getAll(klass);

        return result;
    }


}
