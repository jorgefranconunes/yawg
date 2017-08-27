/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import javaslang.control.Option;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateException;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.freemarker.FreemarkerDataModel;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.spi.TemplateService;
import com.varmateo.yawg.util.Exceptions;


/**
 * Creates templates based on the <a
 * href="http://freemarker.org/">Freemarker</a> template engine.
 */
public final class FreemarkerTemplateService
        implements TemplateService {


    private static final Pattern RE_FTLH = Pattern.compile(".*\\.ftlh$");

    private final Configuration _fmConfig;


    /**
     * @param templatesDir The directory containing the Freemarker
     * template files.
     */
    private FreemarkerTemplateService(final Configuration fmConfig) {

        _fmConfig = fmConfig;
    }


    /**
     *
     */
    /* default */ static FreemarkerTemplateService build(
            final Path templatesDir)
            throws YawgException {

        Configuration fmConfig = null;

        try {
            fmConfig = buildConfiguration(templatesDir);
        } catch ( IOException e ) {
            Exceptions.raise(
                    e,
                    "Failed to initialize {0} - {1} - {2}",
                    FreemarkerTemplateService.class.getSimpleName(),
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

        return new FreemarkerTemplateService(fmConfig);
    }


    /**
     *
     */
    private static Configuration buildConfiguration(final Path templatesDir)
            throws IOException{

        Configuration fmConfig =
                new Configuration(Configuration.VERSION_2_3_24);

        fmConfig.setDirectoryForTemplateLoading(templatesDir.toFile());
        fmConfig.setDefaultEncoding("UTF-8");
        fmConfig.setTemplateExceptionHandler(
                TemplateExceptionHandler.RETHROW_HANDLER);
        fmConfig.setLogTemplateExceptions(false);

        return fmConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Template> getTemplate(final String name)
            throws YawgException {

        return Option.of(name)
                .filter(x -> RE_FTLH.matcher(x).matches())
                .map(this::getFreemarkerTemplate)
                .map(FreemarkerTemplate::new)
                .map(FreemarkerTemplate::asTemplate) // HACK
                .toJavaOptional();
    }


    /**
     *
     */
    private freemarker.template.Template getFreemarkerTemplate(
            final String name)
            throws YawgException {

        freemarker.template.Template fmTemplate = null;

        try {
            fmTemplate = _fmConfig.getTemplate(name);
        } catch ( IOException e ) {
            Exceptions.raise(
                    e,
                    "Failed to fetch template \"{0}\" - {1} - {2}",
                    name,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

        return fmTemplate;
    }


    /**
     *
     */
    private static final class FreemarkerTemplate
            implements Template {


        private final freemarker.template.Template _fmTemplate;


        /**
         *
         */
        FreemarkerTemplate(final freemarker.template.Template fmTemplate) {

            _fmTemplate = fmTemplate;
        }


        /**
         *
         */
        @Override
        public void process(
                final TemplateDataModel dataModel,
                final Writer writer)
                throws YawgException {

            FreemarkerDataModel fmDataModel =
                    new FreemarkerDataModel(dataModel);

            try {
                _fmTemplate.process(fmDataModel, writer);
            } catch ( TemplateException | IOException e ) {
                Exceptions.raise(
                        e,
                        "Failed to process template \"{0}\" - {1} - {2}",
                        _fmTemplate.getName(),
                        e.getClass().getSimpleName(),
                        e.getMessage());
            }
        }


        /**
         * HACK.
         */
        public Template asTemplate() {

            return this;
        }


    }

}
