/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Optional;

import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.BakerConf;
import com.varmateo.yawg.YawgDomain;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.YawgInfo;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.PageTemplateService;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogWithUtils;


/**
 * Copies the contents of a source directory tree to a target
 * directory, transforming some files on the way.
 */
public final class Baker
    extends Object {


    private final BakerConf _conf;
    private final YawgDomain _domain;


    /**
     * @param conf All the parameters needed for performing a bake.
     */
    public Baker(final BakerConf conf) {

        _conf = conf;
        _domain = new YawgDomain(conf);
    }


    /**
     * Performs the baking of one directory tree into another.
     *
     * @exception YawgException Thrown if the baking could not be
     * completed for whatever reason.
     */
    public void bake()
            throws YawgException {

        LogWithUtils log = LogWithUtils.from(_domain.getLog());
        Path sourceDir = _conf.sourceDir;
        Path targetDir = _conf.targetDir;
        Path templatesDir = _conf.templatesDir;

        log.info("{0} {1}", YawgInfo.PRODUCT_NAME, YawgInfo.VERSION);
        log.info("    Source    : {0}", sourceDir);
        log.info("    Target    : {0}", targetDir);
        log.info(
                "    Templates : {0}",
                (templatesDir==null) ? "NONE" : templatesDir.toString());

        log.logDelay("bake", () -> doBake(log));
    }


    /**
     *
     */
    private void doBake(final Log log)
            throws YawgException {

        Path sourceDir = _conf.sourceDir;
        Path targetDir = _conf.targetDir;
        ItemBaker fileBaker = _domain.getFileBaker();
        ItemBaker dirBaker = new DirBaker(log, sourceDir, fileBaker);
        PageTemplateService templateService = _domain.getTemplateService();
        Optional<PageTemplate> template = templateService.getDefaultTemplate();

        dirBaker.bake(sourceDir, template, targetDir);
    }


}
