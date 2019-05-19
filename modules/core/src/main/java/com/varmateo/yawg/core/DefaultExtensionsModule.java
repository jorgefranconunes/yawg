/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import io.vavr.Lazy;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import com.varmateo.yawg.asciidoctor.AsciidoctorPageBaker;
import com.varmateo.yawg.breadcrumbs.BreadcrumbsBakeListener;
import com.varmateo.yawg.freemarker.FreemarkerTemplateServiceFactory;
import com.varmateo.yawg.html.HtmlPageBaker;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.TemplateServiceFactory;


/**
 *
 */
/* default */ final class DefaultExtensionsModule {


    private final Lazy<Seq<PageBaker>> _pageBakers = Lazy.of(this::createPageBakers);

    private final Lazy<Seq<TemplateServiceFactory>> _templateServiceFactories =
            Lazy.of(this::createTemplateServiceFactories);

    private final Lazy<Seq<DirBakeListener>> _dirBakeListeners =
            Lazy.of(this::createDirBakeListeners);


    private DefaultExtensionsModule() {
        // Nothiong to do.
    }


    /**
     *
     */
    public static DefaultExtensionsModule create() {

        return new DefaultExtensionsModule();
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
    public Seq<TemplateServiceFactory> templateServiceFactories() {

        return _templateServiceFactories.get();
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
                HtmlPageBaker.create());
    }


    private Seq<TemplateServiceFactory> createTemplateServiceFactories() {

        return List.of(
                FreemarkerTemplateServiceFactory.create());
    }


    private Seq<DirBakeListener> createDirBakeListeners() {

        return List.of(
                BreadcrumbsBakeListener.create());
    }

}
