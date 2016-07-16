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


    /**
     * Retrieves all <code>TemplateServiceFactory</code> instances
     * available through the standard Java service loading facility
     * (https://docs.oracle.com/javase/tutorial/ext/basics/spi.html).
     *
     * @return All available <code>TemplateServiceFactory</code>
     * instances.
     */
    static Collection<TemplateServiceFactory> getAllFactories() {

        ServiceLoader<TemplateServiceFactory> loader =
                ServiceLoader.load(TemplateServiceFactory.class);
        Iterator<TemplateServiceFactory> allFactories = loader.iterator();
        Collection<TemplateServiceFactory> result = Lists.newList(allFactories);

        return result;
    }


}
