/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import com.varmateo.commons.logging.Log;
import com.varmateo.commons.logging.LogFactory;
import com.varmateo.commons.util.Holder;

import com.varmateo.yawg.AsciidocBaker;
import com.varmateo.yawg.CopyBaker;
import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.YawgBakerConf;


/**
 * Factory for all the objects required by the main baker.
 */
/* package private */ final class YawgDomain
        extends Object {


    private final YawgBakerConf _conf;

    private final Holder<ItemBaker> _asciidocBaker =
            Holder.of(this::newAsciidocBaker);

    private final Holder<ItemBaker> _copyBaker =
            Holder.of(this::newCopyBaker);

    private final Holder<ItemBaker> _fileBaker =
            Holder.of(this::newFileBaker);

    private final Holder<Log> _log =
            Holder.of(this::newLog);


    /**
     *
     */
    public YawgDomain(final YawgBakerConf conf) {

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
    private ItemBaker newAsciidocBaker() {

        ItemBaker result = new AsciidocBaker();

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
        Collection<ItemBaker> bakers = Arrays.asList(_asciidocBaker.get());
        ItemBaker defaultBaker = _copyBaker.get();
        ItemBaker result =
                new FileBaker(log, sourceRootDir, bakers, defaultBaker);

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