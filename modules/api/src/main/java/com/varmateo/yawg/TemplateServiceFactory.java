/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;

import com.varmateo.yawg.TemplateService;


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
     */
    TemplateService newTemplateService(Path templatesDir);


}
