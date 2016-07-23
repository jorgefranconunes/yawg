/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.nio.file.Path;

import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.TemplateServiceFactory;
import com.varmateo.yawg.freemarker.FreemarkerTemplateService;


/**
 *
 */
public final class FreemarkerTemplateServiceFactory
        extends Object
        implements TemplateServiceFactory {


    /**
     *
     */
    public FreemarkerTemplateServiceFactory() {
        // Nothing to do.
    }


    /**
     * {@inheritDoc}
     */
    public TemplateService newTemplateService(final Path templatesDir) {

        TemplateService result = new FreemarkerTemplateService(templatesDir);

        return result;
    }


}
