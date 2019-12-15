/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import io.vavr.control.Option;
import io.vavr.control.Try;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateException;

import com.varmateo.yawg.api.Result;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.freemarker.FreemarkerDataModel;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.spi.TemplateService;
import com.varmateo.yawg.util.Results;


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
    public static TemplateService create(final Path templatesDir)
            throws YawgException {

        final Configuration fmConfig;

        try {
            fmConfig = buildConfiguration(templatesDir);
        } catch ( IOException e ) {
            throw FreemarkerTemplateServiceException.initializationFailure(templatesDir, e);
        }

        return new FreemarkerTemplateService(fmConfig);
    }


    /**
     *
     */
    private static Configuration buildConfiguration(final Path templatesDir)
            throws IOException{

        final Configuration fmConfig = new Configuration(Configuration.VERSION_2_3_24);

        fmConfig.setDirectoryForTemplateLoading(templatesDir.toFile());
        fmConfig.setDefaultEncoding("UTF-8");
        fmConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        fmConfig.setLogTemplateExceptions(false);

        return fmConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Template> prepareTemplate(final String name)
            throws YawgException {

        return Option.of(name)
                .filter(x -> RE_FTLH.matcher(x).matches())
                .map(this::prepareFreemarkerTemplate)
                .map(FreemarkerTemplate::new)
                .map(FreemarkerTemplate::asTemplate) // HACK
                .toJavaOptional();
    }


    /**
     *
     */
    private freemarker.template.Template prepareFreemarkerTemplate(
            final String name)
            throws YawgException {

        freemarker.template.Template fmTemplate = null;

        try {
            fmTemplate = _fmConfig.getTemplate(name);
        } catch ( IOException e ) {
            throw FreemarkerTemplateServiceException.fetchFailure(name, e);
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
        public Result<Void> process(
                final TemplateContext context,
                final Writer writer) {

            final FreemarkerDataModel fmDataModel = new FreemarkerDataModel(context);
            final Try<Void> result = Try.run(() -> _fmTemplate.process(fmDataModel, writer))
                    .recoverWith(this::processingFailure);

            return Results.fromTry(result);
        }


        /**
         * HACK.
         */
        public Template asTemplate() {

            return this;
        }


        private <T> Try<T> processingFailure(final Throwable rootCause) {

            final YawgException cause = FreemarkerTemplateServiceException.processingFailure(
                    _fmTemplate.getName(), rootCause);

            return Try.failure(cause);
        }


    }

}
