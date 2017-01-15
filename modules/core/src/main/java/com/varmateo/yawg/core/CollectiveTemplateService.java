/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.Optional;

import javaslang.collection.Seq;
import javaslang.control.Option;

import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.util.Exceptions;


/**
 * 
 */
/* package private */ final class CollectiveTemplateService
        implements TemplateService {


    private final Seq<TemplateService> _services;


    /**
     *
     */
    CollectiveTemplateService(final Seq<TemplateService> services) {

        _services = services;
    }


    /**
     * {@inheritDoc}
     */
    public Optional<Template> getTemplate(final String name)
            throws YawgException {

        Option<Template> result = _services
                .map(service -> service.getTemplate(name))
                .map(Option::ofOptional)
                .filter(Option::isDefined)
                .map(Option::get)
                .headOption();

        if ( !result.isDefined() ) {
            throw Exceptions.raise("Unsupported template format \"{0}\"", name);
        }

        return result.toJavaOptional();
    }


}
