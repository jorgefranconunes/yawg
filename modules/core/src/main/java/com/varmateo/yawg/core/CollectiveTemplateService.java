/**************************************************************************
 *
 * Copyright (c) 2016-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.Optional;
import java.util.function.Function;

import io.vavr.collection.Seq;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateService;

final class CollectiveTemplateService
        implements TemplateService {

    private final Function<String, Optional<Template>> _templateFetcher;

    private CollectiveTemplateService(final Seq<TemplateService> services) {
        if ( services.isEmpty() ) {
            _templateFetcher = name -> Optional.empty();
        } else {
            _templateFetcher = name -> prepareTemplateFromServices(services, name);
        }
    }

    public static TemplateService create(final Seq<TemplateService> services) {
        return new CollectiveTemplateService(services);
    }

    @Override
    public Optional<Template> prepareTemplate(final String name) {
        return _templateFetcher.apply(name);
    }

    private static Optional<Template> prepareTemplateFromServices(
            final Seq<TemplateService> services,
            final String name
    ) {
        final Optional<Template> result = services
                .map(service -> service.prepareTemplate(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .headOption()
                .toJavaOptional();

        if ( result.isEmpty() ) {
            throw CollectiveTemplateServiceException.unsupportedTemplateFormat(name);
        }

        return result;
    }
}
