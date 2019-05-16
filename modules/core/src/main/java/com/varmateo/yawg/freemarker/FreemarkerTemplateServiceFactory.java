/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.nio.file.Path;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.TemplateService;
import com.varmateo.yawg.spi.TemplateServiceFactory;
import com.varmateo.yawg.freemarker.FreemarkerTemplateService;


/**
 *
 */
public final class FreemarkerTemplateServiceFactory
        implements TemplateServiceFactory {


    /**
     * {@inheritDoc}
     */
    public TemplateService newTemplateService(final Path templatesDir)
            throws YawgException {

        return FreemarkerTemplateService.create(templatesDir);
    }


}
