/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Optional;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.YawgTemplate;


/**
 *
 */
public interface YawgTemplateService {


    /**
     *
     */
    Optional<YawgTemplate> getDefaultTemplate()
            throws YawgException;


    /**
     *
     */
    YawgTemplate getTemplate(String name)
            throws YawgException;
}