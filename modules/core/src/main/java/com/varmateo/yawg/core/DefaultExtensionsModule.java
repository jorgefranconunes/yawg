/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.function.Function;

import io.vavr.Lazy;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

import com.varmateo.yawg.asciidoctor.AsciidoctorPageBaker;
import com.varmateo.yawg.breadcrumbs.BreadcrumbsBakeListener;
import com.varmateo.yawg.freemarker.FreemarkerTemplateService;
import com.varmateo.yawg.commonmark.CommonMarkPageBaker;
import com.varmateo.yawg.html.HtmlPageBaker;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.TemplateService;


/**
 *
 */
/* default */ final class DefaultExtensionsModule {


    private final Option<Path> _templatesDir;

    private final Lazy<Seq<PageBaker>> _pageBakers =
            Lazy.of(this::createPageBakers);

    private final Lazy<Seq<TemplateService>> _templateServices =
            Lazy.of(this::createTemplateServices);

    private final Lazy<Seq<DirBakeListener>> _dirBakeListeners =
            Lazy.of(this::createDirBakeListeners);


    private DefaultExtensionsModule(final Option<Path> templatesDir) {


        _templatesDir = templatesDir;
    }


    /**
     *
     */
    public static DefaultExtensionsModule create(final Option<Path> templatesDir) {

        return new DefaultExtensionsModule(templatesDir);
    }


    /**
     *
     */
    public Seq<PageBaker> pageBakers() {

        return _pageBakers.get();
    }


    /**
     *
     */
    public Seq<TemplateService> templateServices() {

        return _templateServices.get();
    }


    /**
     *
     */
    public Seq<DirBakeListener> dirBakeListeners() {

        return _dirBakeListeners.get();
    }


    private Seq<PageBaker> createPageBakers() {

        return List.of(
                AsciidoctorPageBaker.create(),
                HtmlPageBaker.create(),
                CommonMarkPageBaker.create());
    }


    private Seq<TemplateService> createTemplateServices() {

        final List<Function<Path, TemplateService>> templateServiceFactories = List.of(
                FreemarkerTemplateService::create);
        final Function<Path, List<TemplateService>> createTemplateServices =
                path -> templateServiceFactories.map(factory -> factory.apply(path));

        return _templatesDir
                .map(createTemplateServices)
                .getOrElse(List::empty);
    }


    private Seq<DirBakeListener> createDirBakeListeners() {

        return List.of(
                BreadcrumbsBakeListener.create());
    }

}
