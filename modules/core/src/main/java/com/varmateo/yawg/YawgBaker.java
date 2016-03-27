/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import com.varmateo.commons.logging.Log;
import com.varmateo.commons.logging.LogWithUtils;

import com.varmateo.yawg.YawgBakeConf;


/**
 *
 */
public final class YawgBaker
    extends Object {


    private final LogWithUtils _log;


    /**
     *
     */
    public YawgBaker(final Log log) {

        _log = LogWithUtils.from(log);
    }


    /**
     *
     */
    public void bake(final YawgBakeConf conf) {

        _log.logDelay("bake", () -> doBake(conf));
    }


    private void doBake(final YawgBakeConf conf) {

        _log.debug("    Source dir : {0}", conf.sourceDir);
        _log.debug("    Target dir : {0}", conf.targetDir);

        // TBD
    }


}