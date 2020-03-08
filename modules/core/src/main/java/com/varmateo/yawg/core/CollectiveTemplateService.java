/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
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


/**
 * 
 */
/* default */ final class CollectiveTemplateService
        implements TemplateService {


    private final Function<String, Optional<Template>> _templateFetcher;


    private CollectiveTemplateService(final Seq<TemplateService> services) {

        if ( services.isEmpty() ) {
            _templateFetcher = name -> Optional.empty();
        } else {
            _templateFetcher = name -> prepareTemplateFromServices(services, name);
        }
    }


    /**
     *
     */
    public static TemplateService create(final Seq<TemplateService> services) {
        return new CollectiveTemplateService(services);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Template> prepareTemplate(final String name) {

        return _templateFetcher.apply(name);
    }


    /**
     *
     */
    private static Optional<Template> prepareTemplateFromServices(
            final Seq<TemplateService> services,
            final String name)
            throws YawgException {

        final Option<Template> result = services
                .map(service -> service.prepareTemplate(name))
                .map(Option::ofOptional)
                .filter(Option::isDefined)
                .map(Option::get)
                .headOption();

        if ( !result.isDefined() ) {
            throw CollectiveTemplateServiceException.unsupportedTemplateFormat(name);
        }

        return result.toJavaOptional();
    }


}
