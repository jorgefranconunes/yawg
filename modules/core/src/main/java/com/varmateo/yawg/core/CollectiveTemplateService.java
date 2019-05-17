/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.Optional;
import java.util.function.Function;

import io.vavr.collection.Seq;
import io.vavr.control.Option;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateService;

import com.varmateo.yawg.util.Exceptions;


/**
 * 
 */
/* default */ final class CollectiveTemplateService
        implements TemplateService {


    private final Function<String,Optional<Template>> _templateFetcher;


    /**
     *
     */
    CollectiveTemplateService(final Seq<TemplateService> services) {

        if ( services.isEmpty() ) {
            _templateFetcher = name -> Optional.empty();
        } else {
            _templateFetcher = name -> getTemplateFromServices(services, name);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Template> getTemplate(final String name)
            throws YawgException {

        return _templateFetcher.apply(name);
    }


    /**
     *
     */
    private static Optional<Template> getTemplateFromServices(
            final Seq<TemplateService> services,
            final String name)
            throws YawgException {

        final Option<Template> result = services
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
