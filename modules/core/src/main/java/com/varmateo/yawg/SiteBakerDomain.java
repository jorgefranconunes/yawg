/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.CopyBaker;
import com.varmateo.yawg.DirBaker;
import com.varmateo.yawg.DirBakerConfDao;
import com.varmateo.yawg.FileBaker;
import com.varmateo.yawg.PageTemplateService;
import com.varmateo.yawg.SiteBakerConf;
import com.varmateo.yawg.SingleSiteBaker;
import com.varmateo.yawg.asciidoctor.AsciidoctorBaker;
import com.varmateo.yawg.freemarker.FreemarkerTemplateService;
import com.varmateo.yawg.html.HtmlBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.util.Holder;


/**
 * Factory for all the objects required by the main baker.
 */
/* package private */ final class SiteBakerDomain
        extends Object {


    private final SiteBakerConf _conf;

    private final Holder<Baker> _asciidocBaker =
            Holder.of(this::newAsciidoctorBaker);

    private final Holder<SingleSiteBaker> _siteBaker =
            Holder.of(this::newSiteBaker);

    private final Holder<Baker> _copyBaker =
            Holder.of(this::newCopyBaker);

    private final Holder<DirBaker> _dirBaker =
            Holder.of(this::newDirBaker);

    private final Holder<DirBakerConfDao> _dirBakerConfDao =
            Holder.of(this::newDirBakerConfDao);

    private final Holder<FileBaker> _fileBaker =
            Holder.of(this::newFileBaker);

    private final Holder<Optional<PageTemplateService>> _templateService =
            Holder.of(this::newFreemarkerTemplateService);

    private final Holder<Baker> _htmlBaker =
            Holder.of(this::newHtmlBaker);

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
    private Baker newAsciidoctorBaker() {

        Baker result = new AsciidoctorBaker();

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
    private Baker newCopyBaker() {

        Baker result = new CopyBaker();

        return result;
    }


    /**
     *
     */
    private DirBaker newDirBaker() {

        DirBaker result =
                new DirBaker(
                        _log.get(),
                        _conf.sourceDir,
                        _fileBaker.get(),
                        _templateService.get(),
                        _dirBakerConfDao.get());
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
        Path sourceRootDir = _conf.sourceDir;
        Collection<Baker> bakers =
                Arrays.asList(
                        _asciidocBaker.get(),
                        _htmlBaker.get());
        Baker defaultBaker = _copyBaker.get();
        FileBaker result =
                new FileBaker(log, sourceRootDir, bakers, defaultBaker);

        return result;
    }


    /**
     *
     */
    private Optional<PageTemplateService> newFreemarkerTemplateService() {

        Optional<PageTemplateService> result =
                _conf.templatesDir
                .map(FreemarkerTemplateService::new);

        return result;
    }


    /**
     *
     */
    private Baker newHtmlBaker() {

        Baker result = new HtmlBaker();

        return result;
    }


    /**
     *
     */
    private Log newLog() {

        Log result = LogFactory.createFor(this);

        return result;
    }


}
