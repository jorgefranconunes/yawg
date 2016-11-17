/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.Collection;
import java.util.Optional;

import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.commons.util.Lists;
import com.varmateo.yawg.util.Exceptions;


/**
 * 
 */
/* package private */ final class CollectiveTemplateService
        extends Object
        implements TemplateService {


    private final Collection<TemplateService> _services;


    /**
     *
     */
    CollectiveTemplateService(final Collection<TemplateService> services) {

        _services = Lists.readOnlyCopy(services);
    }


    /**
     * {@inheritDoc}
     */
    public Optional<Template> getTemplate(final String name)
            throws YawgException {

        Optional<Template> result =
                _services.stream()
                .map(srv -> srv.getTemplate(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        if ( !result.isPresent() ) {
            Exceptions.raise("Unsupported template format \"{0}\"", name);
        }

        return result;
    }


}
