/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.nio.file.Path;

import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.TemplateServiceFactory;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.freemarker.FreemarkerTemplateService;


/**
 *
 */
public final class FreemarkerTemplateServiceFactory
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
    public TemplateService newTemplateService(final Path templatesDir)
            throws YawgException {

        TemplateService result = FreemarkerTemplateService.build(templatesDir);

        return result;
    }


}
