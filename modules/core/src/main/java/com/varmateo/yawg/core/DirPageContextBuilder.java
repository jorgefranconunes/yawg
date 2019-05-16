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
            final DirBakeOptions dirBakerConf,
            final PageVars extensionVars)
            throws YawgException {

        Function<Path,Optional<Template>> templateFetcher =
                buildTemplateFetcher(dirBakerConf);
        String dirUrl = buildRelativeUrl(targetDir, _targetRootDir);
        String rootRelativeUrl = buildRelativeUrl(_targetRootDir, targetDir);
        PageVars allPageVars =
                PageVarsBuilder.create()
                .addPageVars(extensionVars)
                .addPageVars(dirBakerConf.pageVars)
                .addPageVars(dirBakerConf.pageVarsHere)
                .build();

        return PageContextBuilder.create()
                .setDirUrl(dirUrl)
                .setRootRelativeUrl(rootRelativeUrl)
                .setTemplateFetcher(templateFetcher)
                .setPageVars(allPageVars)
                .build();
    }


    /**
     * @throws YawgException When the template service throws this
     * exception.
     */
    private Function<Path,Optional<Template>> buildTemplateFetcher(
            final DirBakeOptions dirBakerConf)
            throws YawgException {

        Option<String> templateName = dirBakerConf.templateName;
        TemplateNameMatcher templateNameMatcher = dirBakerConf.templatesHere;
        TemplateService templateService = _templateService;

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

        Path relDir = baseDir.relativize(dir);
        String relativeUrl = relDir.toString().replace(File.separatorChar, '/');

        return relativeUrl.length() == 0
                ? "."
                : relativeUrl;
    }


}
