/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Optional;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateException;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.YawgTemplate;
import com.varmateo.yawg.YawgTemplateService;


/**
 *
 */
public final class FreemarkerTemplateService
        extends Object
        implements YawgTemplateService {


    private static final String DEFAULT_TEMPLATE_NAME = "default.ftl";


    private final Configuration _fmConfig;
    private final boolean _isDefaultTemplateAvailable;
    private final IOException _initializationError;


    /**
     *
     */
    public FreemarkerTemplateService(final Path templatesDir) {

        Configuration fmConfig = null;
        IOException initializationError = null;

        if ( templatesDir != null ) {
            try {
                fmConfig = buildConfiguration(templatesDir);
            } catch ( IOException e ) {
                initializationError = e;
            }
        }

        _fmConfig = fmConfig;
        _isDefaultTemplateAvailable = (fmConfig != null);
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
     * {@inheritDoc}
     */
    @Override
    public Optional<YawgTemplate> getDefaultTemplate()
            throws YawgException {

        Optional<YawgTemplate> result = null;

        if ( _isDefaultTemplateAvailable ) {
            YawgTemplate template = getTemplate(DEFAULT_TEMPLATE_NAME);
            result = Optional.of(template);
        } else {
            result = Optional.empty();
        }

        return result;
    }


    /**
     *
     */
    @Override
    public YawgTemplate getTemplate(final String name)
            throws YawgException {

        if ( _initializationError != null ) {
            YawgException.raise(
                    _initializationError,
                    "Failed to initialize template service - {0} - {1}",
                    _initializationError.getClass().getSimpleName(),
                    _initializationError.getMessage());
        }

        Template fmTemplate = getFreemarkerTemplate(name);
        YawgTemplate result = new FreemarkerTemplate(fmTemplate);

        return result;
    }


    /**
     *
     */
    private Template getFreemarkerTemplate(final String name)
            throws YawgException {

        Template fmTemplate = null;

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
            implements YawgTemplate {


        private final Template _fmTemplate;


        /**
         *
         */
        public FreemarkerTemplate(final Template fmTemplate) {

            _fmTemplate = fmTemplate;
        }


        /**
         *
         */
        @Override
        public void process(
                final YawgTemplateDataModel dataModel,
                final Writer writer)
                throws YawgException {

            try {
                _fmTemplate.process(dataModel, writer);
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
