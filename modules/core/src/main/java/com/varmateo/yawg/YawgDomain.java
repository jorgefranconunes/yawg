/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import com.varmateo.yawg.CopyBaker;
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
/* package private */ final class YawgDomain
        extends Object {


    private final BakerConf _conf;

    private final Holder<ItemBaker> _asciidocBaker =
            Holder.of(this::newAsciidoctorBaker);

    private final Holder<ItemBaker> _copyBaker =
            Holder.of(this::newCopyBaker);

    private final Holder<ItemBaker> _fileBaker =
            Holder.of(this::newFileBaker);

    private final Holder<PageTemplateService> _fmTemplateService =
            Holder.of(this::newFreemarkerTemplateService);

    private final Holder<ItemBaker> _htmlBaker =
            Holder.of(this::newHtmlBaker);

    private final Holder<Log> _log =
            Holder.of(this::newLog);


    /**
     *
     */
    public YawgDomain(final BakerConf conf) {

        _conf = conf;
    }


    /**
     *
     */
    public ItemBaker getFileBaker() {

        return _fileBaker.get();
    }


    /**
     *
     */
    public Log getLog() {

        return _log.get();
    }


    /**
     *
     */
    public PageTemplateService getTemplateService() {

        return _fmTemplateService.get();
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
    private ItemBaker newCopyBaker() {

        ItemBaker result = new CopyBaker();

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

        Path templatesDir = _conf.templatesDir;
        PageTemplateService result =
                new FreemarkerTemplateService(templatesDir);

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
