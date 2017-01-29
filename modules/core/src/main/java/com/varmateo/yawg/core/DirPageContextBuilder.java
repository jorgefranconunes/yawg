/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import javaslang.control.Option;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.TemplateNameMatcher;


/**
 * Builds the <code>PageContext</code> to be used in the baking of
 * files in one given directory.
 */
/* package private */ final class DirPageContextBuilder {


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
            final DirBakerConf dirBakerConf,
            final PageVars extensionVars)
            throws YawgException {

        Function<Path,Optional<Template>> templateFetcher =
                buildTemplateFetcher(dirBakerConf);
        String dirUrl = buildRelativeUrl(targetDir, _targetRootDir);
        String rootRelativeUrl = buildRelativeUrl(_targetRootDir, targetDir);
        PageVars allPageVars =
                PageVars.builder()
                .addPageVars(extensionVars)
                .addPageVars(dirBakerConf.pageVars)
                .addPageVars(dirBakerConf.pageVarsHere)
                .build();
        PageContext context =
                PageContext.builder()
                .setDirUrl(dirUrl)
                .setRootRelativeUrl(rootRelativeUrl)
                .setTemplateFetcher(templateFetcher)
                .setPageVars(allPageVars)
                .build();

        return context;
    }


    /**
     * @throws YawgException When the template service throws this
     * exception.
     */
    private Function<Path,Optional<Template>> buildTemplateFetcher(
            final DirBakerConf dirBakerConf)
            throws YawgException {

        Option<String> templateName = dirBakerConf.templateName;
        TemplateNameMatcher templateNameMatcher = dirBakerConf.templatesHere;
        TemplateService templateService = _templateService;

        Function<Path,Optional<Template>> fetcher =
                path ->
                getTemplate(
                        path,
                        templateNameMatcher,
                        templateName,
                        templateService)
                .toJavaOptional();

        return fetcher;
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
        String result = relDir.toString().replace(File.separatorChar, '/');

        if ( result.length() == 0 ) {
            result = ".";
        }

        return result;
    }


}