/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;

import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.util.Lists;


/**
 *
 */
public interface TemplateServiceFactory {


    /**
     *
     */
    TemplateService newTemplateService(Path templatesDir);


    /**
     *
     */
    static Collection<TemplateServiceFactory> getAllFactories() {

        ServiceLoader<TemplateServiceFactory> loader =
                ServiceLoader.load(TemplateServiceFactory.class);
        Iterator<TemplateServiceFactory> allFactories = loader.iterator();
        Collection<TemplateServiceFactory> result = Lists.newList(allFactories);

        return result;
    }


}
