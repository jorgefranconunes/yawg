/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.CopyBaker;
import com.varmateo.yawg.DirBaker;
import com.varmateo.yawg.DirBakerConfDao;
import com.varmateo.yawg.PageTemplateService;
import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.BakerConf;
import com.varmateo.yawg.asciidoctor.AsciidoctorBaker;
import com.varmateo.yawg.freemarker.FreemarkerTemplateService;
import com.varmateo.yawg.html.HtmlBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.util.Holder;


/**
 * Factory for all the objects required by the main baker.
 */
public final class Yawg
        extends Object {


    private final BakerConf _conf;

    private final Holder<ItemBaker> _asciidocBaker =
            Holder.of(this::newAsciidoctorBaker);

    private final Holder<Baker> _baker =
            Holder.of(this::newBaker);

    private final Holder<ItemBaker> _copyBaker =
            Holder.of(this::newCopyBaker);

    private final Holder<DirBaker> _dirBaker =
            Holder.of(this::newDirBaker);

    private final Holder<DirBakerConfDao> _dirBakerConfDao =
            Holder.of(this::newDirBakerConfDao);

    private final Holder<ItemBaker> _fileBaker =
            Holder.of(this::newFileBaker);

    private final Holder<PageTemplateService> _templateService =
            Holder.of(this::newFreemarkerTemplateService);

    private final Holder<ItemBaker> _htmlBaker =
            Holder.of(this::newHtmlBaker);

    private final Holder<Log> _log =
            Holder.of(this::newLog);


    /**
     *
     */
    public Yawg(final BakerConf conf) {

        _conf = conf;
    }


    /**
     *
     */
    public Baker getBaker() {

        return _baker.get();
    }


    /**
     *
     */
    private ItemBaker newAsciidoctorBaker() {

        ItemBaker result = new AsciidoctorBaker();

        return result;
    }


    /**
     *
     */
    private Baker newBaker() {

        Baker result = new Baker(_log.get(), _conf, _dirBaker.get());

        return result;
    }


    /**
     *
     */
    private ItemBaker newCopyBaker() {

        ItemBaker result = new CopyBaker();

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
    private ItemBaker newFileBaker() {

        Log log = _log.get();
        Path sourceRootDir = _conf.sourceDir;
        Collection<ItemBaker> bakers =
                Arrays.asList(
                        _asciidocBaker.get(),
                        _htmlBaker.get());
        ItemBaker defaultBaker = _copyBaker.get();
        ItemBaker result =
                new FileBaker(log, sourceRootDir, bakers, defaultBaker);

        return result;
    }


    /**
     *
     */
    private PageTemplateService newFreemarkerTemplateService() {

        PageTemplateService result =
                new FreemarkerTemplateService(_conf.templatesDir);

        return result;
    }


    /**
     *
     */
    private ItemBaker newHtmlBaker() {

        ItemBaker result = new HtmlBaker();

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
