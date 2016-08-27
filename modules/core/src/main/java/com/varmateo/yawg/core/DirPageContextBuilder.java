/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.core.DirBakerConf;


/**
 * Builds the <code>PageContext</code> to be used in the baking of
 * files in one given directory.
 */
/* package private */ final class DirPageContextBuilder
        extends Object {


    private final Path _sourceRootDir;
    private final Optional<TemplateService> _templateService;


    /**
     *
     */
    public DirPageContextBuilder(
            final Path sourceRootDir,
            final Optional<TemplateService> templateService) {

        _sourceRootDir = sourceRootDir;
        _templateService = templateService;
    }


    /**
     *
     */
    public PageContext buildPageContext(
            final Path sourceDir,
            final DirBakerConf dirBakerConf,
            final PageVars extensionVars)
            throws YawgException {

        Optional<Template> template =
                dirBakerConf.templateName
                .flatMap(name ->
                         _templateService.flatMap(
                                 srv -> srv.getTemplate(name)));
        String dirUrl = buildRelativeUrl(sourceDir, _sourceRootDir);
        String rootRelativeUrl = buildRelativeUrl(_sourceRootDir, sourceDir);
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
                .setTemplate(template)
                .setPageVars(allPageVars)
                .build();

        return context;
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
