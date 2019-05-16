/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.core.DirBakeOptions;
import com.varmateo.yawg.core.TemplateNameMatcher;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateService;
import io.vavr.control.Option;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;


/**
 * Builds the <code>PageContext</code> to be used in the baking of
 * files in one given directory.
 */
final class DirPageContextBuilder {


    private final Path _targetRootDir;
    private final TemplateService _templateService;


    /**
     *
     */
    DirPageContextBuilder(
            final Path targetRootDir,
            final TemplateService templateService) {

        _targetRootDir = targetRootDir;
        _templateService = templateService;
    }


    /**
     *
     */
    public PageContext buildPageContext(
            final Path targetDir,
            final DirBakeOptions dirBakeOptions,
            final PageVars extensionVars)
            throws YawgException {

        final Function<Path,Optional<Template>> templateFetcher =
                buildTemplateFetcher(dirBakeOptions);
        final String dirUrl = buildRelativeUrl(targetDir, _targetRootDir);
        final String rootRelativeUrl = buildRelativeUrl(_targetRootDir, targetDir);
        final PageVars allPageVars = PageVarsBuilder.create()
                .addPageVars(dirBakeOptions.pageVars)
                .addPageVars(dirBakeOptions.pageVarsHere)
                .addPageVars(extensionVars)
                .build();

        return PageContextBuilder.create()
                .dirUrl(dirUrl)
                .rootRelativeUrl(rootRelativeUrl)
                .templateFetcher(templateFetcher)
                .pageVars(allPageVars)
                .build();
    }


    /**
     * @throws YawgException When the template service throws this
     * exception.
     */
    private Function<Path,Optional<Template>> buildTemplateFetcher(
            final DirBakeOptions dirBakeOptions)
            throws YawgException {

        final Option<String> templateName = dirBakeOptions.templateName;
        final TemplateNameMatcher templateNameMatcher = dirBakeOptions.templatesHere;
        final TemplateService templateService = _templateService;

        return path -> getTemplate(
                path,
                templateNameMatcher,
                templateName,
                templateService)
                .toJavaOptional();
    }


    /**
     *
     */
    private static Option<Template> getTemplate(
            final Path path,
            final TemplateNameMatcher templateNameMatcher,
            final Option<String> templateName,
            final TemplateService templateService) {

        return getTemplateForPath(path, templateNameMatcher, templateService)
                .orElse(() -> getDefaultTemplate(templateName, templateService));
    }


    /**
     *
     */
    private static Option<Template> getTemplateForPath(
            final Path path,
            final TemplateNameMatcher templateNameMatcher,
            final TemplateService templateService) {

        return templateNameMatcher
                .getTemplateNameFor(path)
                .flatMap(name ->
                         Option.ofOptional(templateService.getTemplate(name)));
    }


    /**
     *
     */
    private static Option<Template> getDefaultTemplate(
            final Option<String> templateName,
            final TemplateService templateService) {

        return templateName.flatMap(
                name -> Option.ofOptional(templateService.getTemplate(name)));
    }


    /**
     * Generates the URL for the given <code>dir</code> relative to
     * the given <code>baseDir</code>.
     */
    private String buildRelativeUrl(
            final Path dir,
            final Path baseDir) {

        final Path relDir = baseDir.relativize(dir);
        final String relativeUrl = relDir.toString().replace(File.separatorChar, '/');

        return relativeUrl.length() == 0
                ? "."
                : relativeUrl;
    }


}
