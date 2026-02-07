/**************************************************************************
 *
 * Copyright (c) 2016-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateService;

/**
 * Builds the <code>PageContext</code> to be used in the baking of
 * files in one given directory.
 */
/* package private */ final class DirPageContextBuilder {

    private final Path _targetRootDir;
    private final TemplateService _templateService;
    private final String _bakeId;

    public DirPageContextBuilder(
            final Path targetRootDir,
            final TemplateService templateService,
            final String bakeId
    ) {
        _targetRootDir = targetRootDir;
        _templateService = templateService;
        _bakeId = bakeId;
    }

    public PageContext buildPageContext(
            final Path targetDir,
            final DirBakeOptions dirBakeOptions,
            final PageVars extensionVars
    ) {
        final Function<Path, Optional<Template>> templateFetcher =
                buildTemplateFetcher(dirBakeOptions);
        final String dirUrl = buildRelativeUrl(targetDir, _targetRootDir);
        final String rootRelativeUrl = buildRelativeUrl(_targetRootDir, targetDir);
        final PageVars allPageVars =
                PageVarsBuilder.create()
                .addPageVars(dirBakeOptions.pageVars)
                .addPageVars(dirBakeOptions.pageVarsHere)
                .addPageVars(extensionVars)
                .build();

        return PageContextBuilder.create()
                .dirUrl(dirUrl)
                .rootRelativeUrl(rootRelativeUrl)
                .templateFetcher(templateFetcher)
                .pageVars(allPageVars)
                .bakeId(_bakeId)
                .build();
    }

    /**
     * @throws YawgException When the template service throws this
     * exception.
     */
    private Function<Path, Optional<Template>> buildTemplateFetcher(
            final DirBakeOptions dirBakeOptions
    ) {
        final Optional<String> templateName = dirBakeOptions.templateName;
        final TemplateNameMatcher templateNameMatcher = dirBakeOptions.templatesHere;
        final TemplateService templateService = _templateService;

        return path -> prepareTemplate(
                path,
                templateNameMatcher,
                templateName,
                templateService);
    }

    private static Optional<Template> prepareTemplate(
            final Path path,
            final TemplateNameMatcher templateNameMatcher,
            final Optional<String> templateName,
            final TemplateService templateService
    ) {
        return prepareTemplateForPath(path, templateNameMatcher, templateService)
                .or(() -> prepareDefaultTemplate(templateName, templateService));
    }

    private static Optional<Template> prepareTemplateForPath(
            final Path path,
            final TemplateNameMatcher templateNameMatcher,
            final TemplateService templateService
    ) {
        return templateNameMatcher.templateNameFor(path)
                .flatMap(name -> templateService.prepareTemplate(name));
    }

    private static Optional<Template> prepareDefaultTemplate(
            final Optional<String> templateName,
            final TemplateService templateService
    ) {
        return templateName.flatMap(name -> templateService.prepareTemplate(name));
    }

    /**
     * Generates the URL for the given <code>dir</code> relative to
     * the given <code>baseDir</code>.
     */
    private String buildRelativeUrl(
            final Path dir,
            final Path baseDir
    ) {
        final Path relDir = baseDir.relativize(dir);
        final String relativeUrl = relDir.toString().replace(File.separatorChar, '/');

        return relativeUrl.length() == 0
                ? "."
                : relativeUrl;
    }
}
