/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Function;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
import com.varmateo.yawg.spi.TemplateService;


/**
 *
 */
/* default */ final class DirBakerContext {


    private final Path _sourceRootDir;
    private final Path _targetRootDir;
    private final DirPageContextBuilder _dirPageContextBuilder;
    private final DirBakeOptions _dirBakeOptions;
    private final PageContext _pageContext;
    private final PageVars _extensionVars;


    private DirBakerContext(
            final Path sourceRootDir,
            final Path targetRootDir,
            final DirPageContextBuilder dirPageContextBuilder,
            final DirBakeOptions dirBakeOptions,
            final PageContext pageContext,
            final PageVars extensionVars) {

        _sourceRootDir = sourceRootDir;
        _targetRootDir = targetRootDir;
        _dirPageContextBuilder = dirPageContextBuilder;
        _dirBakeOptions = dirBakeOptions;
        _pageContext = pageContext;
        _extensionVars = extensionVars;
    }


    /**
     *
     */
    public static DirBakerContext create(
            final Path sourceRootDir,
            final Path targetRootDir,
            final TemplateService templateService,
            final DirBakeOptions dirBakeOptions) {

        final String bakeId = UUID.randomUUID().toString();
        final DirPageContextBuilder dirPageContextBuilder = new DirPageContextBuilder(
                targetRootDir, templateService, bakeId);
        final PageVars extensionVars = PageVars.empty();
        final PageContext pageContext = dirPageContextBuilder.buildPageContext(
                targetRootDir, dirBakeOptions, extensionVars);

        return new DirBakerContext(
                sourceRootDir,
                targetRootDir,
                dirPageContextBuilder,
                dirBakeOptions,
                pageContext,
                extensionVars);
    }


    /**
     *
     */
    public Path sourceRootDir() {
        return _sourceRootDir;
    }


    /**
     *
     */
    public DirBakeOptions dirBakeOptions() {
        return _dirBakeOptions;
    }


    /**
     *
     */
    public PageContext pageContext() {
        return _pageContext;
    }


    /**
     *
     */
    public DirBakerContext buildForChildDir(
            final Path childTargetDir,
            final DirBakeOptions childSpecificDirBakeOptions,
            final Function<PageContext, PageVars> contextEnricher) {

        final DirBakeOptions childDirBakeOptions =
                childSpecificDirBakeOptions.mergeOnTopOf(_dirBakeOptions);
        final PageContext childPageContext = _dirPageContextBuilder.buildPageContext(
                childTargetDir, childDirBakeOptions, _extensionVars);
        final PageVars childNewExtensionVars = contextEnricher.apply(childPageContext);
        final PageVars childExtensionVars = PageVarsBuilder.create(_extensionVars)
                .addPageVars(childNewExtensionVars)
                .build();
        final PageContext extendedChildPageContext = PageContextBuilder.create(childPageContext)
                .addPageVars(childExtensionVars)
                .build();

        return new DirBakerContext(
                _sourceRootDir,
                _targetRootDir,
                _dirPageContextBuilder,
                childDirBakeOptions,
                extendedChildPageContext,
                childExtensionVars);
    }

}
