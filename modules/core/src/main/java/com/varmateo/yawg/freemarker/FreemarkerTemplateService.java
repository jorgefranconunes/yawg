/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Optional;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateException;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.PageTemplateDataModel;
import com.varmateo.yawg.PageTemplateService;


/**
 * Creates templates based on Freemarker template files.
 */
public final class FreemarkerTemplateService
        extends Object
        implements PageTemplateService {


    private static final String DEFAULT_TEMPLATE_NAME = "default.ftlh";


    private final Configuration _fmConfig;
    private final boolean _isDefaultTemplateAvailable;
    private final IOException _initializationError;


    /**
     *
     */
    public FreemarkerTemplateService(final Optional<Path> templatesDir) {

        Configuration fmConfig = null;
        IOException initializationError = null;

        if ( templatesDir.isPresent() ) {
            try {
                fmConfig = buildConfiguration(templatesDir.get());
            } catch ( IOException e ) {
                initializationError = e;
            }
        }

        _fmConfig = fmConfig;
        _isDefaultTemplateAvailable = templatesDir.isPresent();
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
    public Optional<PageTemplate> getDefaultTemplate()
            throws YawgException {

        checkInitialization();

        Optional<PageTemplate> result = null;

        if ( _isDefaultTemplateAvailable ) {
            PageTemplate template = getTemplate(DEFAULT_TEMPLATE_NAME);
            result = Optional.of(template);
        } else {
            result = Optional.empty();
        }

        return result;
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
     *
     */
    @Override
    public PageTemplate getTemplate(final String name)
            throws YawgException {

        checkInitialization();

        Template fmTemplate = getFreemarkerTemplate(name);
        PageTemplate result = new FreemarkerTemplate(fmTemplate);

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
            implements PageTemplate {


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
                final PageTemplateDataModel dataModel,
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
