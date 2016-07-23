/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateException;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateDataModel;
import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.freemarker.FreemarkerDataModel;


/**
 * Creates templates based on <a
 * href="http://freemarker.org/">Freemarker</a> template engine.
 */
public final class FreemarkerTemplateService
        extends Object
        implements TemplateService {


    private static final Pattern RE_FTLH = Pattern.compile(".*\\.ftlh$");

    private final Configuration _fmConfig;
    private final IOException _initializationError;


    /**
     * @param templatesDir The directory containing the Freemarker
     * template files.
     */
    public FreemarkerTemplateService(final Path templatesDir) {

        Configuration fmConfig = null;
        IOException initializationError = null;

        try {
            fmConfig = buildConfiguration(templatesDir);
        } catch ( IOException e ) {
            initializationError = e;
        }

        _fmConfig = fmConfig;
        _initializationError = initializationError;
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
     *
     */
    private void checkInitialization()
            throws YawgException {

        if ( _initializationError != null ) {
            YawgException.raise(
                    _initializationError,
                    "Failed to initialize template service - {0} - {1}",
                    _initializationError.getClass().getSimpleName(),
                    _initializationError.getMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Template> getTemplate(final String name)
            throws YawgException {

        checkInitialization();

        Optional<Template> result = null;

        if ( RE_FTLH.matcher(name).matches() ) {
            freemarker.template.Template fmTemplate =
                    getFreemarkerTemplate(name);
            Template template =
                    new FreemarkerTemplate(fmTemplate);
            result = Optional.of(template);
        } else {
            result = Optional.empty();
        }

        return result;
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
            YawgException.raise(
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
            extends Object
            implements Template {


        private final freemarker.template.Template _fmTemplate;


        /**
         *
         */
        public FreemarkerTemplate(
                final freemarker.template.Template fmTemplate) {

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
                YawgException.raise(
                        e,
                        "Failed to process template \"{0}\" - {1} - {2}",
                        _fmTemplate.getName(),
                        e.getClass().getSimpleName(),
                        e.getMessage());
            }
        }


    }

}
