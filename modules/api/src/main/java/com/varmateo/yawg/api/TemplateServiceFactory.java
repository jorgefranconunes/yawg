/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.nio.file.Path;

import com.varmateo.yawg.api.TemplateService;
import com.varmateo.yawg.api.YawgException;


/**
 * A factory of <code>TemplateService</code> instances.
 */
public interface TemplateServiceFactory {


    /**
     * Returns a new new <code>TemplateService</code> object.
     *
     * @param templatesDir Directory where the template files reside.
     *
     * @return A newly created <code>TemplateService</code> instance.
     *
     * @throws YawgException If it was not possible to create the
     * template service. For instance, if the given directory does not
     * exist.
     */
    TemplateService newTemplateService(Path templatesDir)
            throws YawgException;


}
